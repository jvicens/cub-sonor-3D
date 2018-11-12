package Audio3D;



import java.lang.Exception;

public class HrtfException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public HrtfException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("hrtfException: loadHrtfs: minimum phase hrtf's have not been found for that user");
			case 2:
				return("hrtfException: setHrtfs: hrtf doesn't exist.");
			case 3:
				return("hrtfException: audio AudioFormat and hrtf AudioFormat are incompatibles.");
			default:
				return("Exception: Filter(...): unknown error.");
		}
	}
}
