package Utils.FoodsDB;

public class databaseExc extends FoodsDBException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public databaseExc() {
		this.error="database exception";
	}
	
	public databaseExc(String err) {
		this.error=err;
	}

}
