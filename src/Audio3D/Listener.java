package Audio3D;



import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import utils.math.*;

public class Listener {

	public static final int ToSpeaker=1;
	public static final int ToFile=2;
	
	private int m_toType;
	public double m_px, m_py, m_pz;
	public double m_ox, m_oy, m_oz;
	private AudioFormat m_audioFormat;

	private SndToSpeaker sndSpeaker;
	private SndToFile m_sndFile;

	private Vec m_left;
	private Vec m_right;
	
	private Path m_path;
	
	/**
	 * 
	*/
	public Listener () {
		m_toType = 0;
		m_audioFormat = null;
		m_px = m_py = m_pz = 0;
		m_ox = 1; m_oy = m_oz = 0;

		sndSpeaker = null;
		m_sndFile = null;
		
		m_left = null;
		m_right = null;
		
		m_path = null;
		
	}

	/**
	 * 
	*/
	public void init (int to_type, AudioFormat audioFormat, float buffer_size_ms) throws LineUnavailableException, Exception {

		if (to_type == ToSpeaker)
			init(to_type, audioFormat, buffer_size_ms, "", 0);
		else if (to_type == ToFile) {
			init(to_type, audioFormat, buffer_size_ms, "out.wav", 120);
		}
	}

	/**
	 * 
	*/
	public void init (int to_type, AudioFormat audioFormat, float buffer_size_ms, String file_name, int sec) throws LineUnavailableException, Exception{

		m_toType=to_type;
		
		if (m_toType == ToSpeaker) {
			sndSpeaker = new SndToSpeaker();
			m_audioFormat=new AudioFormat (audioFormat.getEncoding(), audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), 2, audioFormat.getFrameSize(), audioFormat.getFrameRate(), audioFormat.isBigEndian());
			sndSpeaker.init(m_audioFormat);
		} else if (m_toType == ToFile) {
			m_sndFile = new SndToFile();
			m_audioFormat=new AudioFormat (audioFormat.getEncoding(), audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), 2, audioFormat.getFrameSize(), audioFormat.getFrameRate(), audioFormat.isBigEndian());
			m_sndFile.init(m_audioFormat, file_name, sec);
		}
		
		m_left = new Vec((int)(buffer_size_ms*m_audioFormat.getSampleRate()/1000));
		m_right = new Vec((int)(buffer_size_ms*m_audioFormat.getSampleRate()/1000));
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
	public void setPosition (double x, double y, double z) {

		m_px=x; m_py=y; m_pz=z;
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
	public void setOrientation (double x, double y, double z) {

		m_ox=x; m_oy=y; m_oz=z;
	}

	/**
	 * 
	*/
	public double getPX () {

		return (m_px);
	}

	/**
	 * 
	*/
	public double getPY () {

		return (m_py);
	}

	/**
	 * 
	*/
	public double getPZ () {

		return (m_pz);
	}

	/**
	 * 
	*/
	public double getOX () {

		return (m_ox);
	}

	/**
	 * 
	*/
	public double getOY () {

		return (m_oy);
	}

	/**
	 * 
	*/
	public double getOZ () {

		return (m_oz);
	}
	
	/**
	 * 
	*/
	public boolean end ()  {

		if (m_toType == ToSpeaker)
			return (false);
		else if (m_toType == ToFile)
			return (m_sndFile.end());
		else
			return (false);

	}

	
	/**
	 * 
	*/
	public void mixStereo (Vec left, Vec right) throws IOException, VecException, SoundException {
		
		m_left.acc(left);
		m_right.acc(right);
	}

	/**
	 * 
	*/
	public void writeStereo () throws IOException, VecException, SoundException {

		if (m_toType == ToSpeaker)
			sndSpeaker.writeStereo(m_left, m_right);
		else if (m_toType == ToFile)
			m_sndFile.writeStereo(m_left, m_right);

		m_left.zeros();
		m_right.zeros();
	}

	/**
	 * 
	*/
	public void drain () {

		if (m_toType == ToSpeaker)
			sndSpeaker.drain();
	}

	/**
	 * 
	*/
	public void close () throws Exception {

		if (m_toType == ToSpeaker)
			sndSpeaker.close();
		else if (m_toType == ToFile)
			m_sndFile.close();
	}

	/**
	 * 
	*/
	public String toString () {
		
		String s=new String("Listener. ");
		
		if (m_toType == ToFile)
			s+="ToFile :"+m_sndFile.getFileName()+". ";
		else if (m_toType == ToSpeaker)
			s+="ToSpeaker. ";
		
		s+="Position: ("+m_px+", "+m_py+", "+m_pz+"). ";
		
		s+="Orientation: ("+m_ox+", "+m_oy+", "+m_oz+"). ";

		return (s);
	}
	

}
