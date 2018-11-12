package Audio3D;



import utils.math.*;

public class OverlapAndAdd extends Filtre {

	private Vec m_overlap;
	private int m_block_size;
	private boolean OaD;

	/**
	 * 
	*/
	public OverlapAndAdd () { super(); m_overlap=null; m_block_size=0; OaD=false; }

	/**
	 * 
	*/
	public void init (Vec v_B) throws FilterException, VecException {

		super.init(v_B);

		m_overlap=new Vec(v_B.size());
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (Vec v_B, Vec v_A, int overlap_size) throws FilterException, VecException {

		super.init(v_B, v_A);

		m_overlap=new Vec(overlap_size);
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (Vec v_B, int block_size) throws FilterException, VecException {

		super.init(v_B);

		if (block_size<=0) throw new FilterException(4);

		m_overlap=new Vec(v_B.size());
		m_block_size=block_size;
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (Vec v_B, Vec v_A, int overlap_size, int block_size) throws FilterException, VecException {

		super.init(v_B, v_A);

		if (block_size<=0) throw new FilterException(4);

		m_overlap=new Vec(overlap_size);
		m_block_size=block_size;
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (double d_B[]) throws FilterException, VecException {

		super.init(d_B);

		m_overlap=new Vec(d_B.length);
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (double d_B[], double d_A[], int overlap_size) throws FilterException, VecException {

		super.init(d_B, d_A);

		m_overlap=new Vec(overlap_size);
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (double d_B[], int block_size) throws FilterException, VecException {

		super.init(d_B);

		if (block_size<=0) throw new FilterException(4);

		m_overlap=new Vec(d_B.length);
		m_block_size=block_size;
		OaD=false;
	}

	/**
	 * 
	*/
	public void init (double d_B[], double d_A[], int overlap_size, int block_size) throws FilterException, VecException {

		super.init(d_B, d_A);

		if (block_size<=0) throw new FilterException(4);

		m_overlap=new Vec(overlap_size);
		m_block_size=block_size;
		OaD=false;
	}

	/**
	 * 
	*/
	public void blockSize (int block_size) {

		m_block_size=block_size;
	}


	/**
	 * 
	*/
	public int getOverlapSize () throws FilterException {

		if (m_overlap==null) throw new FilterException(8);

		return(m_overlap.size());
	}

	/**
	 * 
	*/
	public Vec OaA_filter (Vec v_in) throws FilterException, VecException {

		if (m_block_size==0) throw new FilterException(3);
		OaD=true;

		int n=m_block_size+m_overlap.size()-1;
		Vec v_out=new Vec(n);

		clearMem();

		if (getFilterType()==FIR) {
			for (int i=0;i<m_block_size;++i) { v_out.setElement(fir(v_in.getElement(i)), i); }
			for (int i=m_block_size;i<n;++i) { v_out.setElement(fir(0), i); }
		} else if (getFilterType()==IIR) {
			for (int i=0;i<m_block_size;++i) { v_out.setElement(iir(v_in.getElement(i)), i); }
			for (int i=m_block_size;i<n;++i) { v_out.setElement(iir(0), i); }
		}

		for (int i=0;i<m_overlap.size()-1;++i)
			v_out.setElement(v_out.getElement(i)+m_overlap.getElement(i), i);

		for (int i=m_block_size, j=0;i<n;++i, ++j)
			m_overlap.setElement(v_out.getElement(i), j);

		OaD=false;
		return (v_out.subVector(0,m_block_size));
	}

	/**
	 * 
	*/
	public double fir (double d_in)  throws FilterException, VecException {

		if (OaD==false) throw new FilterException(5);
		return (super.fir(d_in));
	}

	/**
	 * 
	*/
	public double iir (double d_in)  throws FilterException, VecException {

		if (OaD==false) throw new FilterException(5);
		return (super.iir(d_in));
	}
}
