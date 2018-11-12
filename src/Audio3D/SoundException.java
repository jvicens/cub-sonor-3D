package Audio3D;



import java.lang.Exception;

public class SoundException extends Exception {

	int m_err;

	/**
	 * 
	*/
	public SoundException (int err) { m_err=err; }

	/**
	 * 
	*/
	public String toString () {
		switch (m_err) {
			case 1:
				return("Exception: Sound_XXX: getMono() / writeMono is inconsistent with the stereo format");
			case 2:
				return("Exception: Sound_XXX: getStereoXXX() / writeStereo is inconsistent with the mono format");
			case 3:
				return("Exception: Sound_XXX: frameSize not supported");
			case 4:
				return("Exception: Sound_XXX: number of channels not supported");
			default:
				return("Exception: Sound_XXX: unknown error.");
		}
	}
}
