package Audio3D;



import utils.math.*;

public class ImageSource extends BaseSource {

	private Filtre m_abs_filter; double m_abs_coef;
	private Source m_source;

	private Vec m_filtered_data;

	/**
	 * 
	*/
	public ImageSource () {

		super();

		m_abs_filter = null; m_abs_coef = 1;
		
		m_source = null;
		m_filtered_data = null;
	}

	/**
	 * 
	*/
	public void init (Source source) throws Exception {

		super.init(source.getHrtf());

		m_source=source;

		double[] tmp; tmp = new double[1]; tmp[0]=1; 
		Vec v_tmp; v_tmp = new Vec(tmp);
		m_abs_filter = new Filtre(); m_abs_filter.init(v_tmp);
	}
	
	/**
	 * Si s'especifica els coeficients d'un filtre, llavors aquest ja inclouen l'absorció
	*/
	public void setAbsFilter (Vec abs_coef) throws VecException, FilterException {
	
		m_abs_filter.setCoef(abs_coef); m_abs_coef=0;
	}

	/**
	 * 
	*/
	public void setAbsFilter (int order) throws VecException, FilterException, RoomException {
	
		double[] c;
		
		c = new double[order+1];
		if (order == 0) c[0]=1;
		else if (order == 1) { c[0]=1/2; c[1]=1/2; }
		else if (order == 2) { c[0]=1/4; c[1]=1/2; c[2]=1/4; }
		else if (order == 3) { c[0]=1/8; c[1]=3/8; c[2]=3/8; c[3]= 1/8;}
		else if (order == 4) { c[0]=1/16; c[1]=4/16; c[2]=6/16; c[3]= 4/16; c[4] = 1/16;}
		else throw new RoomException(2);		
		
		m_abs_filter.setCoef(c);
	}

	/**
	 * 
	*/
	public void setAbsFilter (int order, double abs) throws VecException, FilterException, RoomException {
	
		setAbsFilter(order);
		m_abs_coef=abs;
	}
	
	/**
	 * 
	*/
	public void absDstFilter () throws VecException, FilterException, SoundException {
		
		Vec v1;
		
		v1=dstFilter(m_source.getMono());
		m_filtered_data =  m_abs_filter.blockFilter(v1).mult(1-m_abs_coef);		 
	}

	/**
	 * 
	*/
	public Vec Filter_l () throws Exception {

		if ( m_filtered_data == null ) throw new SourceException(1);

		return (hrtfFilter_l(m_filtered_data));
	}

	/**
	 * 
	*/
	public Vec Filter_r () throws SourceException, Exception {

		if ( m_filtered_data == null ) throw new SourceException(1);
		
		return(hrtfFilter_r(m_filtered_data));
	}
	
	/**
	 * 
	*/
	public String toString () {
		
		String s=new String("Image source. ");
		
		s+="Position: ("+getPX()+", "+getPY()+", "+getPZ()+"). ";
		
		s+="Orientation: ("+getOX()+", "+getOY()+", "+getOZ()+"). ";

		return (s);
	}
	
}