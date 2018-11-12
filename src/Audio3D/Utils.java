package Audio3D;



public class Utils {
	
	public static final double c=344;
	
	/**
	 * 
	*/
	public static double rect2Azim (BaseSource source, Listener listener) {

		return (rect2Azim (source.getPX()-listener.getPX(), source.getPY()-listener.getPY(), source.getPZ()-listener.getPZ()));
	}

	/**
	 * 
	*/
	public static double rect2Azim (double x, double y, double z) {

		double azim;
		azim=Math.atan2(y,x)-Math.PI/2;
		if (azim<0) azim+=2*Math.PI;
                //System.out.println("Azim: "+azim);
		return (azim);
	}

	/**
	 * 
	*/
	public static double rect2Elev (BaseSource source, Listener listener) {

		return (rect2Elev (source.getPX()-listener.getPX(), source.getPY()-listener.getPY(), source.getPZ()-listener.getPZ()));
	}

	/**
	 * 
	*/
	public static double rect2Elev (double x, double y, double z) {

		double elev;
		elev=Math.asin(z/rect2R(x,y,z));
		if (elev<0) elev+=2*Math.PI;
		return (elev);
	}
	
	/**
	 * 
	*/
	public static double rect2R (BaseSource source, Listener listener) {

		return (rect2R (source.getPX()-listener.getPX(), source.getPY()-listener.getPY(), source.getPZ()-listener.getPZ()));
	}

	/**
	 * 
	*/
	public static double rect2R (double x, double y, double z) {

		return (Math.sqrt(x*x+y*y+z*z));
	}

	/**
	 * 
	*/
	public static double[] getG (Source source, ImageSource imageSources[], Listener listener, double A[]) {

		double g[];
		int n_imageSources=imageSources.length;
		double t;
		
		g=new double[1+n_imageSources];
		
		g[0]=1/rect2R(source.getPX()-listener.getPX(), source.getPY()-listener.getPY(), source.getPZ()-listener.getPZ());
		t=g[0];

		for (int i=0;i<n_imageSources;++i) {
			g[i+1]=(1-A[i])/Math.pow(rect2R(imageSources[i].getPX()-listener.getPX(), imageSources[i].getPY()-listener.getPY(), imageSources[i].getPZ()-listener.getPZ()), 1.5);
			t+=g[i+1];
		}
		
//		if ( t>1 )
//			for (int i=0;i<n_imageSources+1;++i)
//				g[i]=g[i]/t;
		
		return(g);
	}
	
	/**
	 * 
	*/
	public static int[] getD (Source source, Listener listener, ImageSource imageSources[], double fs) {
		
		int d[];
		int n_imageSources=imageSources.length;
		
		d=new int[1+n_imageSources];
		
		d[0]=(int) (rect2R(source.getPX()-listener.getPX(), source.getPY()-listener.getPY(), source.getPZ()-listener.getPZ())*fs/Utils.c);
		for (int i=0;i<n_imageSources;++i)
			d[i+1]=(int) (rect2R(imageSources[i].getPX()-listener.getPX(), imageSources[i].getPY()-listener.getPY(), imageSources[i].getPZ()-listener.getPZ())*fs/Utils.c);
			
		return (d);
	}
	
}
