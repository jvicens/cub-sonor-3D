package Audio3D;



import javax.sound.sampled.AudioFormat;
import utils.math.*;


public abstract class BaseSource {

	private double m_px, m_py, m_pz;
	private double m_ox, m_oy, m_oz;

	private int m_itd, m_delay;
	
	private Filtre m_leftFilter;
	private Filtre m_rightFilter;

	private Filtre m_dst_filter, m_dst_cfilter; 
	private double m_dst_gain;
	protected Hrtf m_hrtf;

	/**
	 * 
	*/
	public BaseSource () {

		m_px = m_py = m_pz = 0;
		m_ox = 1; m_oy = m_oz = 0;

		m_itd = 0; m_delay=0;
		
		m_dst_filter = null; m_dst_cfilter = null; 
		m_dst_gain = 1;
		
		m_hrtf = null;
	}

	/**
	 * 
	*/
	public void init (Hrtf hrir) throws Exception {

		double[] tmp; tmp = new double[1];  
		Vec v_tmp; 

		m_hrtf=hrir;

		tmp[0]=1;  v_tmp = new Vec(tmp);
		m_dst_filter = new Filtre(); m_dst_filter.init(v_tmp);

		tmp[0]=0;  v_tmp = new Vec(tmp);
		m_dst_cfilter = new Filtre(); m_dst_cfilter.init(v_tmp);

		v_tmp = new Vec(m_hrtf.length()); v_tmp.zeros();
		m_leftFilter=new Filtre(); m_leftFilter.init(v_tmp); 

		v_tmp = new Vec(m_hrtf.length()); v_tmp.zeros();
		m_rightFilter=new Filtre(); m_rightFilter.init(v_tmp);

	}

	/**
	 * Els coeficients del filtre de distància NO inclouen l'atenuació a baixes (m_dst_gain)
	*/
	//public void setDstFilter (Vec dst_coef) throws VecException, filterException {
	//
	//	m_dst_filter.setCoef(dst_coef);
	//}

	/**
	 * 
	*/
	protected void checkAudioFormats (AudioFormat audioFormat) throws HrtfException{

		m_hrtf.checkAudioFormats(audioFormat);
	}

	/**
	 * 
	*/
	public void setDstFilter (int order) throws VecException, FilterException, RoomException {
	
		double[] c, c_c;
		
		c = new double[order+1];
		if (order == 0) c[0]=1;
		else if (order == 1) { c[0]=1/2; c[1]=1/2; }
		else if (order == 2) { c[0]=1/4; c[1]=1/2; c[2]=1/4; }
		else if (order == 3) { c[0]=1/8; c[1]=3/8; c[2]=3/8; c[3]= 1/8;}
		else if (order == 4) { c[0]=1/16; c[1]=4/16; c[2]=6/16; c[3]= 4/16; c[4] = 1/16;}
		else throw new RoomException(2);		
		
		c_c = new double[order+1];
		c_c[0]=1-c[0];
		for (int i=1;i<order+1;++i) c_c[i]=-c[i];
		
		m_dst_filter.setCoef(c);
		m_dst_cfilter.setCoef(c_c);
	}

	/**
	 * 
	*/
	public void setPosition (double x, double y, double z) {

		m_px=x; m_py=y; m_pz=z;
	}

	/**
	 * 
	*/
	public void setOrientation (double x, double y, double z) {

		m_ox=x; m_oy=y; m_oz=z;
	}

	/**
	 * 
	*/
	public double getPX () {

		return (m_px);
	}

	/**
	 * 
	*/
	public double getPY () {

		return (m_py);
	}

	/**
	 * 
	*/
	public double getPZ () {

		return (m_pz);
	}

	/**
	 * 
	*/
	public double getOX () {

		return (m_ox);
	}

	/**
	 * 
	*/
	public double getOY () {

		return (m_oy);
	}

	/**
	 * 
	*/
	public double getOZ () {

		return (m_oz);
	}

	/**
	 * 
	*/
	public int getITD () {

		return (m_itd);
	}

	/**
	 * 
	*/
	public int getDelay () {

		return (m_delay);
	}

	/**
	 * 
	*/
	public void calculateParams (Listener listener, double fs) throws Exception {

		double r;
		
		r=Utils.rect2R(getPX()-listener.getPX(), getPY()-listener.getPY(), getPZ()-listener.getPZ());

		m_dst_gain=1/r;
		m_itd=m_hrtf.setAngles(this, listener);
		m_leftFilter.updateCoef(m_hrtf.getHrir_l());
		m_rightFilter.updateCoef(m_hrtf.getHrir_r());
		m_delay=(int) (r*fs/Utils.c);
	}
	
	/**
	 * 
	*/
	protected Vec dstFilter (Vec v_in) throws VecException, FilterException {
		
// El filtre i els guanys
		
		Vec v_l ,v_h;
		
		v_l=m_dst_filter.blockFilter(v_in).mult(m_dst_gain);
		v_h=m_dst_cfilter.blockFilter(v_in).mult(Math.pow(m_dst_gain, 1.5));
		
		return (v_l.add(v_h));		
	}

	/**
	 * 
	*/
	protected Vec hrtfFilter_l (Vec v_in) throws VecException, FilterException {
		
		return (m_leftFilter.blockFilter(v_in));
	}

	/**
	 * 
	*/
	protected Vec hrtfFilter_r (Vec v_in) throws VecException, FilterException{
		
		return (m_rightFilter.blockFilter(v_in));
	}
	
}