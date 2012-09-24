
public class URLShortening {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num=1012121283;
		mod(num);
		
		

	}

	static void mod(long num){

		if(num==0){
			
		}else{
			long mod = num%2;
			mod(num/2);
			System.out.print(mod);
		}
		
	}
}
