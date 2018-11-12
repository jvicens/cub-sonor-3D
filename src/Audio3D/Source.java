package Audio3D;



import java.io.IOException;
import javax.sound.sampled.AudioFormat;

import utils.math.*;

public class Source extends BaseSource {
	
	public static final int FromFile=1;
	
	private int m_fromType;
	private int m_bufferSize;
	
	private boolean m_active;
	private boolean m_end;
	
	private SndFromFile m_sndFile;

	private Vec m_clean_data, m_filtered_data;

	private Path m_path;
	
	/**
	 * 
	*/
	public Source () {	

		super();

		m_fromType = 0;
		m_bufferSize = 0;
		
		m_active = false; m_end = false;
		
		m_sndFile = null; 
		m_clean_data = null; m_filtered_data = null;

		m_path = null;
	}

	/**
	 * 
	*/
	public void init (int from_type, Hrtf hrtf) throws Exception {

		super.init(hrtf);
		
		m_fromType=from_type;
	}
	
	/**
	 * 
	*/
	public void setFileName (String file_name, float fs, float buffer_size_ms) throws Exception {

		m_sndFile = new SndFromFile();
		m_sndFile.init(file_name, fs, buffer_size_ms);
		m_bufferSize=(int) (buffer_size_ms*m_sndFile.getAudioFormat().getSampleRate()/1000);

		checkAudioFormats(m_sndFile.getAudioFormat());
	}

	/**
	 * 
	*/
	public void setPath (double px, double py, double pz) throws Exception {

		m_path = new Path();
		m_path.init(px, py, pz);
	}

	/**
	 * 
	*/
	public void nextRelativePosition (double x, double y, double z) {

		m_path.nextRelPos(this, x, y, z);
	}

	/**
	 * 
	*/
	public void nextPosition () {

		m_path.nextPos(this);
	}
	
	/**
	 * 
	*/
	public int getFromType () {

		return (m_fromType);
	}
	
	/**
	 * 
	*/
	public int getBufferSize () {

		return (m_bufferSize);
	}

	/**
	 * 
	*/
	public Hrtf getHrtf () {

		return (m_hrtf);
	}

	/**
	 * 
	*/
	public AudioFormat getAudioFormat () {

		return (m_sndFile.getAudioFormat());
	}

	/**
	 * 
	*/
	public void setActive (boolean active) {

		m_active=active;
                m_path.setActive(active);
	}

	/**
	 * 
	*/
	public void setEnd (boolean end) {

		m_end=end;
                 
	}
		
	/**
	 * 
	*/
	public boolean active () {

		return (m_active);
	}

	/**
	 * 
	*/
	public boolean end () {

		return (m_end);
	}	
	
	/**
	 * 
	*/
	public int read () throws Exception {

		int n;
		
		if (m_fromType == FromFile) {			
			n = m_sndFile.read();
			m_clean_data = m_sndFile.getMono();
			m_filtered_data = null;
			return (n);
		}
		
		return (-1);
	}

	/**
	 * 
	*/
	public Vec getMono () throws VecException, FilterException {

		if (m_fromType == FromFile)
			return(m_clean_data);
		
		return (null);
	}

	/**
	 * 
	*/
	public void dstFilter () throws VecException, FilterException {
		
		m_filtered_data = super.dstFilter (getMono());
	}

	/**
	 * 
	*/
	public Vec Filter_l () throws SourceException, Exception {

		if ( m_filtered_data == null ) throw new SourceException(2);

		return (hrtfFilter_l(m_filtered_data));
			
	}

	/**
	 * 
	*/
	public Vec Filter_r () throws SourceException, Exception {

		if ( m_filtered_data == null ) throw new SourceException(2);

		return (hrtfFilter_r(m_filtered_data));
	}
        /**
	 *
	*/
        public boolean available() throws IOException{
            boolean avl = true;
            if (!m_sndFile.available()) avl=false;
            return avl;

        }

	/**
	 * 
	*/
	public void close () throws Exception {

		if (m_fromType == FromFile)
			m_sndFile.close();
	}
	
	/**
	 * 
	*/
	public String toString () {
		
		String s=new String("Source. ");
		
		if (m_fromType == FromFile)
			s+="FromFile :"+m_sndFile.getFileName()+". ";
		else
			s+="From ... unknown. ";
		
		s+="Buffer size: "+m_bufferSize+". ";
		
		s+="Active: ";
		s+=(m_active == true?"yes. ":"no. ");
				
		s+="End: ";
		s+=(m_end == true?"yes. ":"no. ");
		
		s+="Position: ("+getPX()+", "+getPY()+", "+getPZ()+"). ";
		
		s+="Orientation: ("+getOX()+", "+getOY()+", "+getOZ()+"). ";

		return (s);
	}
}