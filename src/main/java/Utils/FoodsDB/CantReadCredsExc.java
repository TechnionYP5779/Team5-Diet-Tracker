package Utils.FoodsDB;

public class CantReadCredsExc  extends FoodsDBException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CantReadCredsExc(String path){
		this.error="cant read creds at path "+path;
	}
	
	CantReadCredsExc(){
		this.error="cant read creds";
	}
}
