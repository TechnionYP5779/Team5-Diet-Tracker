package Utils.FoodsDB;

public class CantSendRequestExc extends FoodsDBException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CantSendRequestExc() {
		this.error="cant read response";
	}

}
