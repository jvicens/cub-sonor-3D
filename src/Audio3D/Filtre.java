package Audio3D;



import utils.math.*;

public class Filtre {

	static public final int NONE=0;
	static public final int FIR=1;
	static public final int IIR=2;
	static public final int COMB=3;
	static public final int ALLPASS=4;

	private int m_type;
	private Vec m_B;
	private Vec m_A;
	private CircularBuffer m_mem_x;
	private CircularBuffer m_mem_y;

	private double m_g;
	private int m_m;
	
	/**
	 * 
	*/
	public Filtre() { m_B = null; m_A=null; m_type=NONE; m_mem_x=null; m_mem_y=null; }

	/**
	 * 
	*/
	public void init (Vec v_B) throws FilterException, VecException {

		setCoef (v_B);
	}

	/**
	 * 
	*/
	public void init (Vec v_B, Vec v_A) throws FilterException, VecException {

		setCoef (v_B, v_A);
	}

	/**
	 * 
	*/
	public void init (double d_B[]) throws FilterException, VecException {

		setCoef (d_B);
	}

	/**
	 * 
	*/
	public void init (double d_B[], double d_A[]) throws FilterException, VecException {

		setCoef (d_B, d_A);
	}

	/**
	 * 
	*/
	public void init (int type, int m, double g) throws FilterException, VecException {

		if (type!=COMB && type!=ALLPASS) throw new FilterException(10);
		
		m_type=type;
		m_m=m;
		m_g=g;
		
		m_mem_x = new CircularBuffer (m_m+1);
		m_mem_x.zeros();

		m_mem_y = new CircularBuffer (m_m+1);
		m_mem_y.zeros();
		
	}
	
	/**
	 * Inicialitza a zeros la memòria d'entrades
	*/
	public void setCoef (Vec v_B) throws FilterException, VecException {

		if (v_B==null) throw new FilterException(1);
		setCoef(v_B.get());
	}

	/**
	 * 
	*/
	public void setCoef (Vec v_B, Vec v_A) throws FilterException, VecException {

		if (v_B==null) throw new FilterException(1);
		if (v_A==null) throw new FilterException(1);
		setCoef(v_B.get(), v_A.get());
	}

	/**
	 * Inicialitza a zeros la memòria d'entrades
	*/
	public void setCoef (double d_B[]) throws FilterException, VecException {
		
		int n=d_B.length;

		m_B = new Vec(n);

		updateCoef(d_B);
		
		m_mem_x = new CircularBuffer (n);
		m_mem_x.zeros();
		m_type=FIR;
	}

	/**
	 * 
	*/
	public void setCoef (double d_B[], double d_A[]) throws FilterException, VecException {
		
		if (d_B==null) throw new VecException (1);
		if (d_B.length==0) throw new VecException(1);
		if (d_A==null) throw new VecException (1);
		if (d_A.length==0) throw new VecException(1);

		int n=d_B.length;

		m_B = new Vec(n);
		for (int i=0;i<n;++i)
			m_B.setElement(d_B[i], n-i-1);

		m_mem_x = new CircularBuffer (n);
		m_mem_x.zeros();

		n=d_A.length-1;

		if (n == 0)
			m_type=FIR;
		else {

			m_A = new Vec(n);
			for (int i=0;i<n;++i)
				m_A.setElement(-d_A[i+1], n-i-1);
	
			m_mem_y = new CircularBuffer (n);
			m_mem_y.zeros();
	
			m_type=IIR;
		}
	}

	/**
	 * No inicialitza a zeros la memòria d'entrades
	*/
	public void updateCoef (Vec v_B) throws FilterException, VecException {

		if (v_B==null) throw new FilterException(1);
		if (m_B==null) throw new FilterException(1);
		if (v_B.size() != m_B.size()) throw new FilterException(12);
		
		updateCoef(v_B.get());
	}

	/**
	 * No inicialitza a zeros la memòria d'entrades
	*/
	public void updateCoef (double d_B[]) throws FilterException, VecException {
		
		if (d_B==null) throw new VecException (1);
		if (d_B.length==0) throw new VecException(1);

		int n=d_B.length;

		for (int i=0;i<n;++i)
			m_B.setElement(d_B[i], n-i-1);
	}

	/**
	 * 
	*/
	public void clearMem () throws FilterException {

		if (m_B==null) throw new FilterException(2);

		m_mem_x.zeros();

		if (m_type==IIR) m_mem_y.zeros();
	}

	/**
	 * 
	*/
	public int getFilterType () {

		return (m_type);
	}

	/**
	 * 
	*/
	public double fir (double d_in) throws FilterException, VecException {

		if (m_type!=FIR) throw new FilterException(6);

		m_mem_x.cb_write (d_in);

		return (m_B.dot(m_mem_x.cb_readVector()));
	}

	/**
	 * 
	*/
	public double iir (double d_in) throws FilterException, VecException {

		if (m_type!=IIR) throw new FilterException(7);

		double out;

		m_mem_x.cb_write(d_in);
		out=m_B.dot(m_mem_x.cb_readVector())+m_A.dot(m_mem_y.cb_readVector());
		m_mem_y.cb_write(out);

		return (out);
	}

	/**
	 * 
	*/
	public double comb (double d_in) throws FilterException, VecException {

		if (m_type!=COMB) throw new FilterException(9);

		double out;
		double x_m, y_m;
		
		x_m=m_mem_x.cb_read(0);
		m_mem_x.cb_write(d_in);

		y_m=m_mem_y.cb_read(0);

		out=x_m+m_g*y_m;
		
		m_mem_y.cb_write(out);

		return (out);
	}

	/**
	 * 
	*/
	public double allpass (double d_in) throws FilterException, VecException {

		if (m_type!=ALLPASS) throw new FilterException(11);

		double out;
		double x_m, y_m;
		
		x_m=m_mem_x.cb_read(0);
		m_mem_x.cb_write(d_in);

		y_m=m_mem_y.cb_read(0);

		out=-m_g*d_in+x_m+m_g*y_m;
		
		m_mem_y.cb_write(out);

		return (out);
	}

	/**
	 * 
	*/
	public double filter (double d_in) throws FilterException, VecException {
		
		double d_out;
		
		d_out=0;
		if (m_type==FIR) 
			d_out=fir(d_in);
		else if (m_type==IIR)
			d_out=iir(d_in);
		else if (m_type==COMB)
			d_out=comb(d_in);
		else if (m_type==ALLPASS)
			d_out=allpass(d_in);
	
		return (d_out);
	}

	/**
	 * 
	*/
	public Vec blockFilter (Vec v_in) throws FilterException, VecException {

		Vec v_out = null;
		int n;

		n=v_in.size();
		v_out=new Vec(n);
		
		for (int i=0;i<n;++i) 
			v_out.setElement(filter(v_in.getElement(i)), i); 
			
		return (v_out.subVector(0, n));
	}
}

