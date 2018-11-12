package Audio3D;



import java.io.File;
import javax.sound.sampled.AudioFormat;
import utils.math.*;

public class Hrtf {
        private static final boolean DEBUG = false;
	private static final String m_path="/Users/Julian/Cubo/JAVA/Cub 6.3/CUB/data/hrir/";

	private int m_hrtf_length;
	String m_path_id;
	String m_file_name;

	double m_azims[];
	double m_elevs[];
	int m_ITD[];
	double m_hrirs_l[][];
	double m_hrirs_r[][];
	
	double m_azim, m_elev;
	
	Vec m_hrir_l, m_hrir_r;

	AudioFormat hrtfAudioFormat;
	/**
	 * 
	*/
	public Hrtf () { 
	
		m_hrtf_length = 0;
		m_hrir_l=new Vec(); 
		m_hrir_r=new Vec(); 

		m_azims=null; m_elevs=null; m_ITD=null; 
		m_hrirs_l=null; m_hrirs_r=null; 
	}

	/**
	 * 
	*/
	public void init(int id, float fs, float hrtf_length_ms) throws HrtfException, Exception { 

		m_path_id=m_path+"IRC_"+id+"/COMPENSATED/WAV/IRC_"+id+"_C/MP/";
		loadHrtfs (fs, hrtf_length_ms);
	}

	/**
	 * 
	*/
	public void checkAudioFormats(AudioFormat audio) throws HrtfException{
		
//System.out.print ("Audio format: "); System.out.println(audio.toString());
//System.out.print ("HRTF format : "); System.out.println(hrtfAudioFormat.toString());
		
		if (audio.getSampleRate()!=hrtfAudioFormat.getSampleRate()) throw new HrtfException(3);

	}
	
	/**
	 * 
	*/
	public int setAngles (BaseSource baseSource, Listener listener) throws VecException, FilterException, HrtfException {
                //System.out.println ("He entrat setAngles");
		double azim, elev;

		azim=Utils.rect2Azim(baseSource, listener)-Utils.rect2Azim(listener.getOX(), listener.getOY(), listener.getOZ());
		if (azim < 0) azim+=2*Math.PI;
		elev=Utils.rect2Elev(baseSource, listener);
		return (setAngles (180/Math.PI*azim, 180/Math.PI*elev));
	}

	/**
	 * 
	*/
	public int setAngles (double azim, double elev) throws VecException, FilterException, HrtfException {
		// Retorna la ITD i calcula m_hrir_l, m_hrir_r
		// Per obtenir m_hrir_l i m_hrir_r cal cridar a les funcions getHrir_l i getHrir_r 
		
		
		m_azim=Math.round(azim);
		m_elev=Math.round(elev);

//System.out.println("Azim: "+m_azim+", Elev: "+m_elev);
		
		int n, i, left, right;
		int A11, A12, A21, A22;

		n=m_azims.length;
//System.out.println("n=m_azims_length: "+n);
//for (i=0;i<n;++i) System.out.print("["+m_azims[i]+", "+m_elevs[i]+"] , ");
		
		double min_elev;		
		min_elev=345;
		if ( (m_elev >= 180) && (m_elev < min_elev) ) m_elev=min_elev;
		if ((m_elev >= min_elev) && (m_elev < 352) ) m_elev=min_elev;
                if ((m_elev >= 352) && (m_elev <=360)) m_elev=0;
                
//System.out.println (m_azim+", "+m_elev);

		boolean end;
		i=left=right=0; end=false;
		while ( end==false ) {
			if ( m_azims[i] == m_azim ) { right=left; end=true; }
			else {
				while ( (i<n-1) && (m_azims[i] == m_azims[i+1]) )
					i++;
				if ( i==n-1 ) { right=0; end=true; }
				else {
					++i; right = i;
					if ( (m_azims[left] < m_azim) && (m_azims[right] > m_azim) ) end=true;
					else left=right;
				}
			}
		}

//System.out.println (left+", "+right);

		i=left;
                //System.out.println ("i_left: "+i);
               

		while ( (i<n) && (m_elevs[i] < m_elev) )
                    ++i;
                        
                //System.out.println ("i_while: "+i);
		if ( m_elevs[i] == m_elev ) A11=A21=i;
		else { A11=i-1; A21=i; }
		
		i=right;
		while ( (i<n) && (m_elevs[i] < m_elev) )
			++i;
		if ( m_elevs[i] == m_elev ) A12=A22=i;
		else { A12=i-1; A22=i; }

//System.out.println (m_azims[left]+", "+m_azims[right]);
//System.out.println (m_elevs[left]+", "+m_elevs[right]);
//System.out.println ("["+A11+", "+A12);
//System.out.println (A21+", "+A22+"]");
//System.out.println ("["+m_azims[A11]+", "+m_azims[A12]);
//System.out.println (m_azims[A21]+", "+m_azims[A22]+"]");
//System.out.println ("["+m_elevs[A11]+", "+m_elevs[A12]);
//System.out.println (m_elevs[A21]+", "+m_elevs[A22]+"]");

		Vec v11, v12, v21, v22, v1, v2;
		v11=new Vec(); v12=new Vec(); v21=new Vec(); v22=new Vec(); v1=new Vec(); v2=new Vec();
		
		v11.set(m_hrirs_l[A11]);
		v12.set(m_hrirs_l[A12]);
		v21.set(m_hrirs_l[A21]);
		v22.set(m_hrirs_l[A22]);

		if ( m_azims[A11] == m_azims[A12] ) v1.set(v11.get());
		else v1.set(v11.mult(m_azims[A12]-m_azim).add(v12.mult(m_azim-m_azims[A11])).mult(1/(m_azims[A12]-m_azims[A11])).get());

		if ( m_azims[A21] == m_azims[A22] ) v2.set(v21.get());
		else v2.set(v21.mult(m_azims[A22]-m_azim).add(v22.mult(m_azim-m_azims[A21])).mult(1/(m_azims[A22]-m_azims[A21])).get());

		if ( m_elevs[A21] == m_elevs[A11] ) m_hrir_l.set(v1.get());
		else m_hrir_l.set(v1.mult(m_elevs[A21]-m_elev).add(v2.mult(m_elev-m_elevs[A11])).mult(1/(m_elevs[A21]-m_elevs[A11])).get());

		v11.set(m_hrirs_r[A11]);
		v12.set(m_hrirs_r[A12]);
		v21.set(m_hrirs_r[A21]);
		v22.set(m_hrirs_r[A22]);

		if ( m_azims[A11] == m_azims[A12] ) v1.set(v11.get());
		else v1.set(v11.mult(m_azims[A12]-m_azim).add(v12.mult(m_azim-m_azims[A11])).mult(1/(m_azims[A12]-m_azims[A11])).get());
		
		if ( m_azims[A21] == m_azims[A22] ) v2.set(v21.get());
		else v2.set(v21.mult(m_azims[A22]-m_azim).add(v22.mult(m_azim-m_azims[A21])).mult(1/(m_azims[A22]-m_azims[A21])).get());

		if ( m_elevs[A21] == m_elevs[A11] ) m_hrir_r.set(v1.get());
		else m_hrir_r.set(v1.mult(m_elevs[A21]-m_elev).add(v2.mult(m_elev-m_elevs[A11])).mult(1/(m_elevs[A21]-m_elevs[A11])).get());
		
//System.out.println(m_hrir_l.toString());
//System.out.println(v11.toString());
//System.out.println(v12.toString());
//System.out.println(v21.toString());
//System.out.println(v22.toString());
//System.out.println(m_hrir_r.toString());
		
		double i11, i12, i21, i22, i1, i2, itd;
		i11=m_ITD[A11];
		i12=m_ITD[A12];
		i21=m_ITD[A21];
		i22=m_ITD[A22];		// !!!! Hi havia m_ITD[A21] ... (11/02/09)

		if ( m_azims[A11] == m_azims[A12] ) i1=i11;
		else i1=(i11*(m_azims[A12]-m_azim)/(m_azims[A12]-m_azims[A11])+i12*(m_azim-m_azims[A11])/(m_azims[A12]-m_azims[A11]));

		if ( m_azims[A21] == m_azims[A22] ) i2=i21;
		else i2=(i21*(m_azims[A22]-m_azim)/(m_azims[A22]-m_azims[A21])+i22*(m_azim-m_azims[A21])/(m_azims[A22]-m_azims[A21]));

		if ( m_elevs[A21] == m_elevs[A11] ) itd=i1;
		else itd=i1*(m_elevs[A21]-m_elev)/(m_elevs[A21]-m_elevs[A11])+i2*(m_elev-m_elevs[A11])/(m_elevs[A21]-m_elevs[A11]);
		
//System.out.println("ITD: "+(m_azim<=180? (int)Math.round(itd):(int)Math.round(-itd)));
//System.out.println();

		return (m_azim<=180? (int)Math.round(itd):(int)Math.round(-itd));	
	}

	/**
	 * 
	*/
	private void loadHrtfs (float fs, float hrtf_length_ms) throws HrtfException, Exception {
                if (DEBUG)System.out.println("m_path_id:" +m_path_id);
		File dir = new File(m_path_id);
                if (DEBUG)System.out.println("isFiles: " +dir.isFile());
                if (DEBUG)System.out.println("isDirectory: " +dir.isDirectory());
                String[] files = dir.list();
		if (files == null) throw new HrtfException(1);
		java.util.Arrays.sort(files);
		
		SndFromFile hrtfFile=new SndFromFile();
		int n;
		double norm;

		m_azims=new double[files.length];
		m_elevs=new double[files.length];
		m_ITD=new int[files.length];		
		m_hrirs_l=new double[files.length][];
		m_hrirs_r=new double[files.length][];
		
		float fs_in_file, new_fs;
                if (DEBUG)System.out.println("m_path_id+files:" +m_path_id+files[0]);
		hrtfFile.init (m_path_id+files[0], fs, 1); 
		hrtfAudioFormat=hrtfFile.getAudioFormat();
		
		new_fs = hrtfAudioFormat.getSampleRate();										// La real, en funció de la desitjada
		fs_in_file = hrtfFile.getAudioFormatinFile(m_path_id+files[0]).getSampleRate();	// La del fitxer d'àudio

		hrtfFile.close();
		
		m_hrtf_length = (int) (hrtf_length_ms*hrtfAudioFormat.getSampleRate()/1000);

		float tmp;
		norm=Math.pow(2,-hrtfFile.getAudioFormat().getSampleSizeInBits()+1);
		for (int i=0; i<files.length; i++) {
			m_azims[i]=(double) Integer.parseInt(files[i].substring(18,21));
			m_elevs[i]=(double) Integer.parseInt(files[i].substring(23,26));
			tmp=(float)Integer.parseInt(files[i].substring(29,32));
			m_ITD[i]=(int) (tmp*new_fs/fs_in_file);		// La ITD obtinguda a partir del nom del fitxer és respecte a la fs del fitxer ...

//System.out.println(m_path_id+files[i]);				
//System.out.println(files[i]+" "+m_r+" "+m_azims[i]+" "+m_elevs[i]+" "+m_ITD[i]);

			hrtfFile.init (m_path_id+files[i], fs, hrtf_length_ms);
			n=hrtfFile.read();

			m_hrirs_l[i]=hrtfFile.getStereoLeft(n).mult(norm).get();
			m_hrirs_r[i]=hrtfFile.getStereoRight(n).mult(norm).get();
			hrtfFile.close();

//System.out.print("[");
//for (int a=0;a<m_hrirs_l[i].length;++a)
//	System.out.print(m_hrirs_l[i][a]+", ");
//System.out.println("]");
			
		}
	}

	/**
	 * 
	*/
	public Vec getHrir_l ()  {
	
		return (m_hrir_l);
	}

	/**
	 * 
	*/
	public Vec getHrir_r ()  {
	
		return (m_hrir_r);
	}
	
	/**
	 * 
	*/
	public int length ()  {
	
		return (m_hrtf_length);
	}
	

}