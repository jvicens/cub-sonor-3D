package utils.others;

public class ParserException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public ParserException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("parserException: variable not found.");
			case 2:
				return("parserException: incorrect array format.");
			case 3:
				return("parserException: the number of lines of the file exceeds the maximum number of lines allowed");
			default:
				return("parserException: unknown error.");
		}
	}
}


