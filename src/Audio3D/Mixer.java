package Audio3D;



import utils.math.*;

public class Mixer {
	
	private int m_n_images;
	private int m_buffer_size;

	private CircularBuffer m_buffer_l[];
	private CircularBuffer m_buffer_r[];
	private CircularBuffer m_buffer_rev_l;
	private CircularBuffer m_buffer_rev_r;

	private int m_d[];
	private int m_itd[];
	private double m_R;
	private double m_D;
	
	/**
	 * 
	*/
	public Mixer() {

		m_n_images=0;
		m_buffer_size=0;
		m_buffer_l=null; m_buffer_r=null;
		m_buffer_rev_l=null; m_buffer_rev_r=null;
		m_d=null;
		m_itd=null; 
		m_R=0; m_D=0;
	}
	
	/**
	 * 
	*/
	public void init (int n_buffers, int buffer_size, double RtoD) {

		m_n_images=n_buffers;
		m_buffer_size=buffer_size;
		m_R=RtoD/(1+RtoD);
		m_D=1/(1+RtoD);
		
		m_buffer_l=new CircularBuffer[n_buffers];
		m_buffer_r=new CircularBuffer[n_buffers];
		m_d=new int[n_buffers];
		m_itd=new int[n_buffers];
		
		for (int i=0;i<n_buffers;++i) {
			m_buffer_l[i]=new CircularBuffer(m_buffer_size);
			m_buffer_r[i]=new CircularBuffer(m_buffer_size);
		}
		
		m_buffer_rev_r=new CircularBuffer(m_buffer_size);
		m_buffer_rev_l=new CircularBuffer(m_buffer_size);
	}

	/**
	 * 
	*/
	public void write (int d[], int itd[], Vec hrtf_l[], Vec hrtf_r[], Vec rev_l, Vec rev_r) throws VecException {

	for (int i=0;i<m_n_images;++i) {
			m_buffer_l[i].cb_writeVector(hrtf_l[i], 0);
			m_buffer_r[i].cb_writeVector(hrtf_r[i], 0);
			m_d[i]=d[i];
			m_itd[i]=itd[i];
		}
		m_buffer_rev_l.cb_writeVector(rev_l, 0);
		m_buffer_rev_r.cb_writeVector(rev_r, 0);
	}
	
	/**
	 * 
	*/
	public Vec read_l (int size) throws VecException {
		
		Vec v_out;
		
		v_out=new Vec(size);

		v_out.zeros();
		
		for (int i=0;i<m_n_images;++i)
			v_out.acc(m_buffer_l[i].cb_readVector(-(m_itd[i]>0?m_d[i]:m_d[i]-m_itd[i]), size, size).mult(m_D));

		v_out.acc(m_buffer_rev_l.cb_readVector(-m_d[0], size, size).mult(m_R));

		return (v_out);
	}

	/**
	 * 
	*/
	public Vec read_r (int size) throws VecException {
		
		Vec v_out;
		
		v_out=new Vec(size);

		v_out.zeros();
		
		for (int i=0;i<m_n_images;++i)
			v_out.acc(m_buffer_r[i].cb_readVector(-(m_itd[i]>0?m_d[i]+m_itd[i]:m_d[i]), size, size).mult(m_D));
			
		v_out.acc(m_buffer_rev_r.cb_readVector(-m_d[0], size, size).mult(m_R));

		return (v_out);
	}

}
