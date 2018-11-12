package Audio3D;



import java.lang.Exception;

public class SourceException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public SourceException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("sourceException: ImageSource ... absDstFilter should be called prior to Filter_l / Filter_r");
			case 2:
				return("sourceException: Source ... dstFilter should be called prior to Filter_l / Filter_r");
			default:
				return("Exception: Filter(...): unknown error.");
		}
	}
}
