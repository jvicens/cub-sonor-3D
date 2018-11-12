package utils.sp;

import utils.math.*;

public class Sample {

	// Filtres associats als processos de dalmació i interpolació
	// 	Els filtres realment són simètrics; la simetria s'aplica en l'execució

	// Filtre usat per la funció resample de Matlab en delmar / interpolar 2
	// 	Per factor de resample = 2, el filtre en dalmació = filtre interpolació / 2
	// 	A excepció del primer coeficient, només els que ocupen una posició senar són diferents de zero
	public static final double B_2[]={0.5000, 0.3165, 0.0000, -0.1009, -0.0000, 0.0553, 0.0000, -0.0343, -0.0000, 0.0220, 0.0000, -0.0140, -0.0000, 0.0086, 0.0000, -0.0049, -0.0000, 0.0025, 0.0000, -0.0011, -0.0000};	

	// Filtre usat per la funció resample de Matlab en delmar / interpolar 3
	// 	Per factor de resample = 3, el filtre en dalmació = filtre interpolació / 3
	// 	A excepció del primer coeficient, un de cada tres coeficients és zero
	public static final double B_3[]={0.3333, 0.2750, 0.1365, 0.0000, -0.0662, -0.0518, -0.0000, 0.0348, 0.0293, 0.0000, -0.0214, -0.0184, -0.0000, 0.0137, 0.0118, 0.0000, -0.0087, -0.0075, -0.0000, 0.0054, 0.0045, 0.0000, -0.0031, -0.0026, -0.0000, 0.0016, 0.0013, 0.0000, -0.0007, -0.0005, -0.0000};	

	/**
	 * 
	 *
	 */
	public Sample () {		
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public double[] downSample2 (double[] in) {

		double[] out;

		if ( ( in.length % 2 ) == 0 )
			out = new double[(int)(in.length/2)];
		else
			out = new double[(int)((in.length+1)/2)];
		
		for (int i=0;i<out.length;++i) {
			out[i]=in[2*i]*B_2[0]; 
			for (int j=1;j<B_2.length;j+=2) {	// S'aprofita que només els coeficients senars són diferents de zero
				if (2*i-j >= 0)
					out[i]+=in[2*i-j]*B_2[j];
				if (2*i+j < in.length)
					out[i]+=in[2*i+j]*B_2[j];
			}
		}
		
		return (out);
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public double[] downSample3 (double[] in) {

		double[] out;

		if ( ( in.length % 3 ) == 0 )
			out = new double[(int)(in.length/3)];
		else if ( in.length % 3 == 1 )
			out = new double[(int)((in.length+2)/3)];
		else
			out = new double[(int)((in.length+1)/3)];
		
		for (int i=0;i<out.length;++i) {
			out[i]=in[3*i]*B_3[0]; 
			for (int j=1;j<B_3.length;++j) {	// S'aprofita que 1 de cada tres coeficients val zero
				if ( ( j % 3 ) == 0)
					continue;
				if (3*i-j >= 0)
					out[i]+=in[3*i-j]*B_3[j];
				if (3*i+j < in.length)
					out[i]+=in[3*i+j]*B_3[j];
			}
		}
		
		return (out);
	}
	
	
	/**
	 * 
	 * @param in
	 * @return
	 */
	public double[] upSample2 (double[] in) {

		double[] t_out, out;

		t_out = new double[in.length*2];	// Senyal interpolat amb zeros
		out = new double[in.length*2];		// Senyal reconstruït
		
		for (int i=0;i<in.length;++i)
			t_out[2*i]=in[i];
		
		for (int i=0;i<out.length;++i) {
			if ( ( i % 2 ) == 0 )
				out[i]=t_out[i];
			else {
				out[i]=0;		
				for (int j=1;j<B_2.length;j+=2) {	// S'aprofita que només els coeficients senars són diferents de zero
					if (i-j >= 0)
						out[i]+=t_out[i-j]*B_2[j];
					if (i+j < out.length)
						out[i]+=t_out[i+j]*B_2[j];
				}
				out[i]=2*out[i];
			}
		}
		
		return (out);
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public double[] upSample3 (double[] in) {

		double[] t_out, out;

		out = new double[in.length*3];		// Senyal interpolat amb zeros
		t_out = new double[in.length*3];	// Senyal reconstruït
		
		for (int i=0;i<in.length;++i)
			t_out[3*i]=in[i];
		
		for (int i=0;i<out.length;++i) {
			if ( ( i % 3 ) == 0 )
				out[i]=t_out[i];
			else {
				out[i]=0;		
				for (int j=1;j<B_3.length;++j) {	// S'aprofita que un de cada tres coeficients és zero
					if ( ( j % 3 ) == 0 )
						continue;
					if (i-j >= 0)
						out[i]+=t_out[i-j]*B_3[j];
					if (i+j < out.length)
						out[i]+=t_out[i+j]*B_3[j];
				}
				out[i]=3*out[i];
			}
		}
		
		return (out);
	}
	
	
	/**
	 * 
	 * @param L
	 * @param M
	 * @return
	 */
	public static boolean isResampleSupported (double L, double M) {
		
		boolean isSupported;
		
		isSupported = false;

		// Molt millorable, però suficient pel propòsit
		if ( L == 1 )
			if ( M == 1 || M == 2 || M == 3 || M == 4 || M == 6 || M == 8 )
				isSupported = true;
		
		if ( M == 1 )
			if ( L == 1 || L == 2 || L == 3 || L == 4 || L == 6 || L == 8 )
				isSupported = true;

		if ( M == 2 )
			if ( L == 3  )
				isSupported = true;

		if ( L == 2 )
			if ( M == 3  )
				isSupported = true;
		
		
		return(isSupported);
	}

	/**
	 * 
	 * @param L
	 * @param M
	 * @return
	 */
	public double[] resample (double[] in, float L, float M) {
	
		double[] out;

		out = null;
		// Molt millorable, però suficient pel propòsit
		if ( L == 1 ) {
			if ( M == 1 )
				out = in;
			else if ( M == 2 )
				out = downSample2 (in);
			else if ( M == 3 )
				out = downSample3 (in);
			else if ( M == 4 )
				out = downSample2 (downSample2 (in));
			else if ( M == 6 )
				out = downSample3 (downSample2 (in));
			else if ( M == 8 )
				out = downSample2 (downSample2 (downSample2 (in)));
		} else if ( M == 1 ) {
			if ( L == 1 )
				out = in;
			else if ( L == 2 )
				out = upSample2 (in);
			else if ( L == 3 )
				out = upSample3 (in);
			else if ( L == 4 )
				out = upSample2 (upSample2 (in));
			else if ( L == 6 )
				out = upSample3 (upSample2 (in));
			else if ( L == 8 )
				out = upSample2 (upSample2 (upSample2 (in)));
		} else if ( M == 2 ) {
			if ( L == 3 )
				out = upSample3 (downSample2 (in));
		} else if ( L == 2 ) {
			if ( M == 3 )
				out = upSample2 (downSample3 (in));
		}
		
		
		return(out);
	}
	
	/**
	 * 
	 * @param L
	 * @param M
	 * @return
	 */
	public Vec resample (Vec in, float L, float M) throws VecException {
	
		return(new Vec (resample(in.get(), L, M)));
	}
	
}
