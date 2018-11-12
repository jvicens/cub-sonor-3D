package Audio3D;



import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import utils.math.*;

/**
 * 
 * Implementa la sortida d'àudio cap a l'altaveu (tarja de so).<p> 
 * La classe admet dades per blocs de qualsevol tamany (funcions <code> writeMono </code> o <code> writeStereo</code>). Aquests dades s'envien immediatament cap a l'altaveu. <p>
 * Per tal d'alliberar el recurs altaveu cal cridar consecutivament a les funcions <code> drain </code> i <code> close </code>.     
 * 
 * @author Carles Vilella
 *
 */

public class SndToSpeaker {

	private SourceDataLine m_sourceDataLine; 
	private AudioFormat m_audioFormat;

	/**
	 * 
	*/
	public SndToSpeaker () { m_sourceDataLine = null; m_audioFormat = null; }

	/**
	 * 
	*/
	public void init (AudioFormat audioFormat) throws LineUnavailableException, Exception{

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		m_sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
		m_sourceDataLine.open(audioFormat);
		m_sourceDataLine.start();
		m_audioFormat=m_sourceDataLine.getFormat();
	}

	/**
	 * 
	*/
	public int channels() {

		return (m_audioFormat.getChannels());
	}

	/**
	 * 
	*/
	public void writeMono(Vec v) throws VecException, SoundException {

		if (channels()!=1) throw new SoundException(1);

		int nSamples, frameSize;
		nSamples=v.size();
		frameSize=m_audioFormat.getFrameSize();

		double d[];
		d=v.getSubVector(0, nSamples);

		byte data_out[];
		data_out=new byte[nSamples*frameSize];

		int tmp;
		
		if (frameSize==1) {
			for (int i=0;i<nSamples;++i) { 
				tmp=(int) d[i]; data_out[frameSize*i]=(byte) (tmp & 0x00ff);
			}
		} else if (frameSize==2) {
			for (int i=0;i<nSamples;++i) {
				tmp=(int) d[i];
				data_out[frameSize*i]=(byte) (tmp & 0x00ff);
				data_out[frameSize*i+1]=(byte) ((tmp >> 8) & 0x00ff);
			}
		} else throw new SoundException(3);

		m_sourceDataLine.write(data_out, 0, data_out.length);
	}

	/**
	 * 
	*/
	public void writeStereo(Vec vl, Vec vr) throws VecException, SoundException {

		if (channels()!=2) throw new SoundException(2);

		int nSamples, frameSize;
		nSamples=vl.size();
		frameSize=m_audioFormat.getFrameSize();

		double dl[], dr[];
		dl=vl.getSubVector(0, nSamples);
		dr=vr.getSubVector(0, nSamples);

		byte data_out[];
		data_out=new byte[nSamples*frameSize];
		
		int tmp;

		if (frameSize==2)
			for (int i=0;i<nSamples;++i) {
				tmp=(int) dl[i]; data_out[frameSize*i]=(byte) (tmp & 0x00ff);
				tmp=(int) dr[i]; data_out[frameSize*i+1]=(byte) (tmp & 0x00ff);
		} else if (frameSize==4) {
			for (int i=0;i<nSamples;++i) {
				tmp=(int) dl[i];
				data_out[frameSize*i]=(byte) (tmp & 0x00ff);
				data_out[frameSize*i+1]=(byte) ((tmp & 0xff00) >> 8);
				tmp=(int) dr[i];
				data_out[frameSize*i+2]=(byte) (tmp & 0x00ff);
				data_out[frameSize*i+3]=(byte) ((tmp >> 8) & 0x00ff);
			}
		} else throw new SoundException(3);

		m_sourceDataLine.write(data_out, 0, data_out.length);
	}

	/**
	 * 
	*/
	public void drain () {
		m_sourceDataLine.drain();
	}

	/**
	 * 
	*/
	public void close () {
		m_sourceDataLine.close();
	}
}
