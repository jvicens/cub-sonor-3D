package Audio3D;



import utils.math.*;

public interface Reverberation {

	/* (non-Javadoc)
	 * @see audio3D.reverb#init(double, double)
	 */
	public abstract void init(double fs, double TR60) throws VecException,
			FilterException;

	/* (non-Javadoc)
	 * @see audio3D.reverb#reverb(utils.Vec)
	 */
	public abstract Vec reverb(Vec in) throws VecException, FilterException;

}