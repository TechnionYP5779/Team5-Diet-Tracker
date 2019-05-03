package Utils.FoodsDB;

public class FoodsDBException extends Exception {

	/**
	 * 
	 */
	
	protected String error;
	
	private static final long serialVersionUID = 1L;
	
	protected FoodsDBException() {}
	
	public String specError() { return new String(error);}
	

}
