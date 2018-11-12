package Audio3D;



import utils.math.*;

public class ReverbSchroeder implements Reverberation {

	private Filtre m_reverb1; private int m_m1=1323; private double m_g1; 
	private Filtre m_reverb2; private int m_m2=1500; private double m_g2; 
	private Filtre m_reverb3; private int m_m3=1719; private double m_g3; 
	private Filtre m_reverb4; private int m_m4=1985; private double m_g4; 
	private Filtre m_reverb5; private int m_m5=220;  private double m_g5; 
	private Filtre m_reverb6; private int m_m6=75;   private double m_g6; 	

	/**
	 * 
	*/
	public ReverbSchroeder() {

		m_reverb1 = new Filtre(); 
		m_reverb2 = new Filtre(); 
		m_reverb3 = new Filtre(); 
		m_reverb4 = new Filtre(); 
		m_reverb5 = new Filtre(); 
		m_reverb6 = new Filtre(); 		
	}
	
	/* (non-Javadoc)
	 * @see audio3D.reverb#init(double, double)
	 */
	/* (non-Javadoc)
	 * @see audio3D.reverberation#init(double, double)
	 */
	public void init (double fs, double TR60) throws VecException, FilterException {

		m_g1=Math.pow(10,-3*m_m1/fs/TR60);
		m_g2=Math.pow(10,-3*m_m2/fs/TR60);
		m_g3=Math.pow(10,-3*m_m3/fs/TR60);
		m_g4=Math.pow(10,-3*m_m4/fs/TR60);
		m_g5=0.7;
		m_g6=0.7;

		m_reverb1.init(Filtre.COMB, m_m1, m_g1);
		m_reverb2.init(Filtre.COMB, m_m2, m_g2);
		m_reverb3.init(Filtre.COMB, m_m3, m_g3);
		m_reverb4.init(Filtre.COMB, m_m4, m_g4);
		m_reverb5.init(Filtre.ALLPASS, m_m5, m_g5);
		m_reverb6.init(Filtre.ALLPASS, m_m6, m_g6);
	}

	/* (non-Javadoc)
	 * @see audio3D.reverb#reverb(utils.Vec)
	 */
	/* (non-Javadoc)
	 * @see audio3D.reverberation#reverb(utils.Vec)
	 */
	public Vec reverb (Vec in) throws VecException, FilterException {
		
		Vec out1, out2, out3, out4, out5, out6, out_rev;

		out1=m_reverb1.blockFilter(in).mult(1-m_g1);
		out2=m_reverb2.blockFilter(in).mult(1-m_g2);
		out3=m_reverb3.blockFilter(in).mult(1-m_g3);
		out4=m_reverb4.blockFilter(in).mult(1-m_g4);
							
		out5=out1.add(out2.add(out3.add(out4))).mult(0.25);

		out6=m_reverb5.blockFilter(out5);
		out_rev=m_reverb6.blockFilter(out6);
		
		return (out_rev);
	}
}
