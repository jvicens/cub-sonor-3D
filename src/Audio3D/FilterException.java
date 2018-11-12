package Audio3D;



import java.lang.Exception;

public class FilterException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public FilterException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("filterException: Filter(...): null vector.");
			case 2:
				return("filterException: Filter(...): undefined coefficients.");
			case 3:
				return("filterException: OverlapAndAdd: undefined block size.");
			case 4:
				return("filterException: OverlapAndAdd: the block size must be a positive integer.");
			case 5:
				return("filterException: OverlapAndAdd: fir/iir should not be directly called when using OverlapAndAdd class.");
			case 6:
				return("filterException: Filtre(...): it is not a FIR filter.");
			case 7:
				return("filterException: Filtre(...): it is not an IIR filter.");
			case 8:
				return("filterException: OverlapAndAdd(...): overlap_size not defined.");
			case 9:
				return("filterException: Filtre(...): it is not an COMB filter.");
			case 10:
				return("filterException: Filtre(...): unknown filter type.");
			case 11:
				return("filterException: Filtre(...): it is not an ALLPASS filter.");
			case 12:
				return("filterException: updateCoef(...): the number of coefficients cannot change.");
			default:
				return("Exception: Filter(...): unknown error.");
		}
	}
}
