package Audio3D;



import java.lang.Exception;

public class RoomException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public RoomException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("roomException: the maximum number of first order image sources is 6");
			case 2:
				return("roomException: order not supported");
			default:
				return("roomException: unknown error.");
		}
	}
}
