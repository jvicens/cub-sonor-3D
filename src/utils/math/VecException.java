package utils.math;

import java.lang.Exception;

public class VecException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public VecException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("Exception: Vec: the size must be a positive integer.");
			case 2:
				return("Exception: Vec: the last element is out of bounds.");
			case 3:
				return("Exception: Vec: both vectors must be the same size.");
			case 4:
				return("Exception: Vec: position must be a positive integer.");
			default:
				return("Exception: Vec: unknown error.");
		}
	}
}
