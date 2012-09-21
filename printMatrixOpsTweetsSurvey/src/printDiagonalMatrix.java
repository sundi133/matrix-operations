import java.text.Normalizer.Form;


public class printDiagonalMatrix {

	public static void main(String[] args) {
		
		int array[][]= {{1,3,5},{2,4,6},{7,9,8}};
		printDArray(array);
		
		
	}
	
	static void printDArray(int[][] array){
		int r=array.length;
		int c=0;
		boolean diagprinted=false;
		for (int i = 0; i < 2*array.length-1; i++) {
			if(r==0 && diagprinted){
				c++;
				int rin=0;
				for(int k=c;k<array.length;k++){
					System.out.print(array[rin][k]+" ");
					rin++;
				}
			}else{
				diagprinted=true;
				r--;
				int cin=0;
				for(int k=r;k<array.length && k>=0;k++){
					System.out.print(array[k][cin] + " ");
					cin++;
				}
			}
			System.out.println();
		}
	}
}
