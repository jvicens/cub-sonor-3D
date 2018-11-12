package Audio3D;



import utils.math.*;

public class CircularBuffer extends Vec {

	private int m_cb_index;

	/**
	 * 
	*/
	CircularBuffer (int n) { 

		super(n);
		m_cb_index=0;
	}

	/**
	 * 
	*/
	public void cb_clear() {

		m_cb_index=0;
	}

	/**
	 * 
	*/
	public double cb_read() throws VecException {

		return (cb_read(1));
	}

	/**
	 * 
	*/
	public double cb_read(int m) throws VecException {

		if (m_v.length==0) throw new VecException (1);		

		double tmp;
		tmp=m_v[m_cb_index];
		m_cb_index=(m_cb_index+m)%m_v.length;
		return (tmp);
	}

	/**
	 * 
	*/
	public Vec cb_readVector() throws VecException {

		return (cb_readVector(0, m_v.length, 0));
	}

	/**
	 * 
	*/
	public Vec cb_readVector(int s, int n, int m) throws VecException {

		if (m_v.length==0) throw new VecException (1);		

		int cb_index_tmp;
		cb_index_tmp=(m_cb_index+m)%m_v.length;
		
		if (m_cb_index+s < 0) m_cb_index=m_v.length+m_cb_index+s;
		else if (m_cb_index+s > m_v.length) m_cb_index=(m_cb_index+s)%m_v.length;
		else m_cb_index+=s;		

		int p;
		double d_tmp[]=new double[n];

		p=0;
		if (m_cb_index+n>m_v.length) {
			for (int i=m_cb_index;i<m_v.length;++i, ++p)
				d_tmp[p]=m_v[i];
			for (int i=0;p<n;++i, ++p)
				d_tmp[p]=m_v[i];
		} else 
			for (int i=m_cb_index;p<n;++i, ++p)
				d_tmp[p]=m_v[i];

		Vec v_tmp;
		v_tmp=new Vec(d_tmp);
		
		m_cb_index=cb_index_tmp;
		return(v_tmp);		
	}

	/**
	 * 
	*/
	public void cb_write(double d) throws VecException {

		cb_write (d, 1);
	}

	/**
	 * 
	*/
	public void cb_write(double d, int m) throws VecException {

		if (m_v.length==0) throw new VecException (1);		

		m_v[m_cb_index]=d;
		m_cb_index=(m_cb_index+m)%m_v.length;
	}

	/**
	 * 
	*/
	public void cb_writeVector (Vec v) throws VecException {
		
		cb_writeVector(v, v.size());
	}

	/**
	 * 
	*/
	public void cb_writeVector (Vec v, int m) throws VecException {

		if (m_v.length==0 || v==null || v.size()==0) throw new VecException (1);		
		
		int p, n1, n2;
		
		n1=m_v.length;
		n2=v.size();

		p=0;
		if ( m_cb_index+n2 > n1) {
			for (int i=m_cb_index;i<n1;++i, ++p)
				m_v[i]=v.getElement(p);
			for (int i=0;p<n2;++i, ++p)
				m_v[i]=v.getElement(p);
		} else
			for (int i=m_cb_index;p<n2;++i, ++p)
				m_v[i]=v.getElement(p);		
		
	m_cb_index=(m_cb_index+m)%m_v.length;
	}
}