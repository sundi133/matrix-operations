import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ErrorTable{

	public static final double MultiCellRef = -1.0;
	public static final String InvalidInput = "INV";

}
/*
 * operations allowed for this program
 */
class Operations{
	static String startsign="=";
	static String add = "+";
	static String sub = "-";
	static String mul = "*";
	static String div = "/";
}

/*
 * max table size allowed for this program
 */
class TableSize{
	static int rows=1000000;
	static int cols=26;
}

/*
 * Table Cell properties
 * value : The String value to be stored,  -1 default
 * reference : if the cell is referencing any other cell for its value
 * evaluated : if the cell value is evaluated for cells with = sign  
 */

class Cell{
	String value="-1";
	// detect cycles in cell references
	boolean startevaluation=false;
	// if the evaluation is done for cells with = sign
	boolean evaluated = false;
	public Cell(String string, boolean b) {
		value= string;
		startevaluation=false;
		evaluated = false;
	}

	public void setEvaluated(boolean value){
		evaluated=value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isReference() {
		return startevaluation;
	}

	public void setReference(boolean reference) {
		this.startevaluation = startevaluation;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

}

@SuppressWarnings("unchecked")
public class kontagent_ss {

	public static void main(String[] args) {

		BufferedReader br = null;
		if(args.length < 1){
			 System.out.println("Input or output filename is missing");
			 System.exit(0);
		}

		try {

			String filename="C:/Users/sundi133/Documents/testing.csv";
			String outFile = "output.txt";
			filename = args[0];

			String sCurrentLine;

			int totalrows=0;

			int totalcols=0;

			br = new BufferedReader(new FileReader(filename));

			Cell[][] inputs = new Cell[TableSize.rows][TableSize.cols];
			//process the file
			while ((sCurrentLine = br.readLine()) != null) {

				String[] line=sCurrentLine.split(",");

				for (int i = 0; i < line.length; i++) {

					inputs[totalrows][i]=new Cell(line[i].trim(),false);

				}

				totalcols=line.length;

				totalrows++;
			}

			//printArray(inputs,totalrows,totalcols);

			processCells(inputs,totalrows,totalcols,0,0);

			
			writeToFile(inputs,outFile,totalrows,totalcols);
			//printArray(inputs,totalrows,totalcols);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)br.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}


	}

	private static void writeToFile(Cell[][] inputs, String outFile, int totalrows, int totalcols) {

		try{
			// Create file 
			FileWriter fstream = new FileWriter(outFile);
			
			BufferedWriter out = new BufferedWriter(fstream);
			
			for (int i = 0; i < totalrows; i++) {
			
				for (int j = 0; j < totalcols; j++) {
				
					Pattern p = Pattern.compile("(\\d+)(\\.[0]+)");
					
					Matcher m = p.matcher(inputs[i][j].value);
					
					String appendToRes=null;
					
					if(j==totalcols-1){
						appendToRes="";
					}else{
						appendToRes=", ";
					}
					
					if(m.matches()){
				
						out.write(m.group(1) +appendToRes);
						
					}else{
						
						out.write(inputs[i][j].value +appendToRes);
						
					}
					
				}
				
				out.write("\n");
			}
			
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}



	}

	private static void printArray(Cell[][] inputs, int totalrows, int totalcols) {

		for(int r=0;r<totalrows;r++){
			for (int c = 0; c < totalcols; c++) {
				printLevels(inputs[r][c].value +" ",1);
			}
			printLevels("",3);

		}

	}

	private static void  printLevels(String print, int value){

		switch (value) {
		case 1:
			System.out.print(print);
			break;

		case 2:
			System.err.print(print);
			break;

		case 3:
			System.out.println();
			break;

		default:
			break;
		}

	}

	/*
	 * process a the input array and evaluates all cells recursively.
	 */

	private static void processCells(Cell[][] inputs,int totalrows,int totalcols, int currRow,int currCol) {

		if(currRow<0 || currRow>=totalrows || currCol <0 || currCol >= totalcols )
			return;

		if(!inputs[currRow][currCol].isEvaluated() && inputs[currRow][currCol].isReference()){
			printLevels("Cyclic references exists",2);
			System.exit(0);
		}

		else if(inputs[currRow][currCol].isEvaluated()){
			return ;
		}

		else{

			inputs[currRow][currCol].setReference(true);

			String startsign="=";

			String expression = inputs[currRow][currCol].value;

			// if the evaluation needs to be done
			if(expression.startsWith(startsign)){

				String[] params=expression.split(" ");

				if(params.length==3){

					//evaluate value
					//handle cells with direct values
					String inputcell =  copy(expression,1,expression.length());

					double cellValue= evaluate(inputcell);

					if(cellValue==ErrorTable.MultiCellRef){
						//handle cells with values pointer to other cells
						inputs[currRow][currCol].value =ErrorTable.InvalidInput;

					}else{

						inputs[currRow][currCol].value = cellValue+"";

					}
				}else if(params.length==1){
					//another cell value pointed at
					String inputcell =  copy(expression,1,expression.length());

					int col=inputcell.toUpperCase().charAt(0)-65;

					int row=Integer.parseInt(copy(expression,2,expression.length()))-1;

					if(row>= 0 && row < totalrows && col >=0 && col < totalcols){

						if(inputs[row][col].isEvaluated() == true){

							inputs[currRow][currCol].value = inputs[row][col].value+"";

						}else{
							//evaluate future cell values and update
							processACell(inputs,totalrows,totalcols,row,col);

							inputs[currRow][currCol].value = inputs[row][col].value+"";
						}

					}else{
						printLevels("Unreferenced cells",2);
						inputs[currRow][currCol].value = ErrorTable.InvalidInput;
					}
				}
				else{
					//handle invalid input params
					//invalid inputs
					//single operator or multiple operator
					inputs[currRow][currCol].value = ErrorTable.InvalidInput;
				}
			}
			// already value is present, do not starts with =
			else{
				//check for valid integer or floating point in cell value
				Pattern p = Pattern.compile("\\d+\\.*\\d*");

				Matcher m = p.matcher(expression);

				if(m.matches()){
					//the value is already stored in the cell
				}else{
					//invalid input, as it does not start with = and not  a number, 
					//so may be a string which is an invalid input
					inputs[currRow][currCol].value = ErrorTable.InvalidInput;
				}


			}

			inputs[currRow][currCol].setEvaluated(true);

			processCells(inputs,totalrows,totalcols,currRow-1,currCol);

			processCells(inputs,totalrows,totalcols,currRow+1,currCol);

			processCells(inputs,totalrows,totalcols,currRow,currCol-1);

			processCells(inputs,totalrows,totalcols,currRow,currCol+1);

		}
	}


	/*
	 * process a single cell based on (row,col) : (r,c) respectively in inputs array
	 */

	private static void processACell(Cell[][] inputs,int totalrows,int totalcols, int r, int c) {

		if(inputs[r][c].isEvaluated()){
			return;
		}
		String expression = inputs[r][c].value;
		// if the evaluation needs to be done
		if(expression.startsWith(Operations.startsign)){

			String[] params=expression.split(" ");

			if(params.length==3){
				//evaluate value
				//handle cells with direct values
				String inputcell =  copy(expression,1,expression.length());

				double cellValue= evaluate(inputcell);

				if(cellValue==ErrorTable.MultiCellRef){
					//handle cells with values pointer to other cells
					inputs[r][c].value =ErrorTable.InvalidInput;

				}else{

					inputs[r][c].value = cellValue+"";

				}
				//handle cells with values pointer to other cells
			}else if(params.length==1){
				//another cell value pointed at
				String inputcell =  copy(expression,1,expression.length());

				int row=inputcell.toUpperCase().charAt(0)-65;

				int col=Integer.parseInt(copy(expression,2,expression.length()))-1;

				inputs[r][c].value = inputs[row][col].value+"";
			}
			else{
				//handle invalid input params
				//invalid inputs
				//single operator or multiple operator
			}
		}
		// already value is present, do not starts with =
		else{
			//check for valid integer or floating point in cell value
			Pattern p = Pattern.compile("\\d+\\.*\\d*");

			Matcher m = p.matcher(expression);

			if(m.matches()){
				//the value is already stored in the cell
			}else{
				//invalid input, as it does not start with = and not  a number, 
				//so may be a string which is an invalid input
				inputs[r][c].value = ErrorTable.InvalidInput;
			}


		}

		inputs[r][c].setEvaluated(true);

		return;

	}

	private static double evaluate(String inputcell) {
		// TODO Auto-generated method stub
		final Deque<Object> s = new LinkedList();
		for (String t : inputcell.split(" ")) {

			if (t.equals(Operations.add)) {

				Double term1 = (Double) s.pop();

				Double term2 = (Double) s.pop();

				s.push(term2+term1);
			}
			else if (t.equals(Operations.sub)) {

				Double term1 = (Double) s.pop();

				Double term2 = (Double) s.pop();

				s.push(term2-term1);
			}
			else if (t.equals(Operations.mul)){

				Double term1 = (Double) s.pop();

				Double term2 = (Double) s.pop();

				s.push(term2*term1);
			}
			else if (t.equals(Operations.div)){

				Double term1 = (Double) s.pop();

				Double term2 = (Double) s.pop();

				s.push(term2/term1);
			}
			else {
				try{

					s.push(Double.parseDouble(t));

				}catch (Exception e) {
					//multi cell reference
					printLevels("multi-cell operations", 2);
					return ErrorTable.MultiCellRef;
				}

			}
		}

		return (Double) s.pop();
	}

	//use this instead of subctring copy
	private static String copy(String expression, int start, int end) {
		// TODO Auto-generated method stub
		StringBuffer s = new StringBuffer();
		for(int k=start; k < end; k++){
			s.append(expression.charAt(k));
		}
		return s.toString();
	}

}
