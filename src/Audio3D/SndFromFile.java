package Audio3D;



import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.UnsupportedAudioFileException;
import utils.math.*;
import utils.sp.*;

/**
 * 
 * Implementa la lectura per blocs d'un fitxer d'àudio.<p>
 * 
 * @author Carles Vilella
 *
 */
public class SndFromFile {

	private File m_soundFile;
	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;
	private int frameSize;
	private byte data_in[];
	private float m_L, m_M;
        private int readata;

       
	private Sample m_sample;	// Resampling

	/**
	 * 
	*/
	public SndFromFile () { 

		m_soundFile = null; 
		audioInputStream = null; 
		audioFormat = null; 
		frameSize=0; 
		data_in = null; 
		m_L = 1; m_M = 1;
		m_sample = new Sample();
		}

	/**
	 * Obre el fitxer d'àudio (lectura)
	 * 
	 * @param 	filename 	nom del fitxer
	 * @params  fs			freqüència de mostreig desitjada
	 * @param 	len_ms		tamany del bloc (en ms)
	*/
	public void init(String filename, float fs, float len_ms) throws Exception {
	// Intenta establir fs com a freqüència de mostreig. Aconseguir-ho dependrà de la freqüència de
	// mostreig del fitxer i de les possibilitats de delmació / interpolació de la classe Sample.
	// Si ho aconsegueix, la variable m_audioFormat contindrà la freqüència de mostreig desitjada; sinó
	// es predrà la del fitxer
		
		
		AudioFormat tmpAudioFormat;
		float L, M;
		

		m_soundFile = new File(filename);
		audioInputStream = AudioSystem.getAudioInputStream(m_soundFile);
		tmpAudioFormat = audioInputStream.getFormat();
		//System.out.println(audioInputStream.markSupported());
		M=tmpAudioFormat.getSampleRate()/fs;
		if ( M < 1 ) {
			L=1/M; M=1;
		} else
			L=1;
		
		if ( Sample.isResampleSupported(L, M) ) {
			audioFormat = new AudioFormat (
					tmpAudioFormat.getEncoding(),
					fs, 
					tmpAudioFormat.getSampleSizeInBits(), 
					tmpAudioFormat.getChannels(), 
					tmpAudioFormat.getFrameSize(), 
					tmpAudioFormat.getFrameRate(), 
					tmpAudioFormat.isBigEndian()
					);					
		} else {
			L = 1; M = 1;
			audioFormat = tmpAudioFormat;
		}			

		m_M = M; m_L = L;
		frameSize=audioFormat.getFrameSize();
		// La longitud del buffer té en compte la dalmació / interpolació
		data_in=new byte[(int)(m_M/m_L*len_ms*audioFormat.getSampleRate()/1000)*frameSize];
	}

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public AudioFormat getAudioFormatinFile (String filename) throws Exception {
	// Retorna l'AudioFormat del fitxer d'àudio indicat per filename
			
			return (AudioSystem.getAudioInputStream(new File(filename)).getFormat());
	}
	/**
	 *
	*/
        public boolean available() throws IOException{
                boolean avl = true;
                if (audioInputStream.available()==0)avl = false;
                return avl;

        }
        /**
	 *
	*/
        
	

	/**
	 *
	*/
	public void close() throws Exception {

		audioInputStream.close();
	}
	
	
	/**
	 * 
	*/
	public int getChannels() {

		return (audioFormat.getChannels());
	}

	/**
	 * 
	*/
	public int getFrameSize() {

		return (audioFormat.getFrameSize());
	}
	
	
	/**
	 * 
	*/
	public AudioFormat getAudioFormat() {
	// Retorna l'AudioFormat de les hrtf. No es necessàriament el dels fitxers, ja que el paràmetre fs passat a init pot modificar-lo

		return (audioFormat);
	}

	/**
	 * 
	*/
	public String getFileName () {

		return (m_soundFile.getName());
	}
	

	/**
	 * Llegeix de fitxer un bloc de dades i l'emmagatzema a la memòria interna
	 * 
	 * @return	nombre de dades llegides (-1 si final de fitxer)
	*/
	public int read() throws IOException {

		int n;
		n=audioInputStream.read(data_in, 0, data_in.length);
                
                //System.out.println(data_in.length);
                //System.out.println(audioInputStream.getFrameLength());
                //System.out.println(readata);



            //    System.out.println(audioInputStream.available());
           /*     if (audioInputStream.available()==0) {
                    audioInputStream.close();
            try {
                audioInputStream = AudioSystem.getAudioInputStream(m_soundFile);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(SndFromFile.class.getName()).log(Level.SEVERE, null, ex);
            }
                    System.out.println(audioInputStream.available());
                }*/
		// Es retorn el nombre de dades que s'obtindran amb les funcions getMono, getStereoXXX
		if (n!=-1)
			return ((int)(n*m_L/(m_M*frameSize)));
		else
			return(n);
	}

	/**
	 * Retorna el bloc de dades emmagatzemat en memòria, en format monofònic. <p> 
	 * Si el fitxer conté dos canals (stèreo), retorna la mescla
	 * 
	 *  @return	vector que conté el canal monofònic
	*/
	public Vec getMono() throws VecException, SoundException {
		return(getMono((int) (data_in.length*m_L/(m_M*frameSize))));
	}

	/**
	 * Retorna <code> nSamples </code> del bloc de dades emmagatzemat en memòria, en format monofònic. <p>
	 * Si el fitxer conté dos canals (stèreo), retorna la mescla
	 * Cal que <code> nSamples </code> sigui menor o igual al tamany del bloc especificat mitjançant la funció <code> init <c/ode> 
	 * 
	 *  @return	vector que conté el canal monofònic
	 * 
	*/
	public Vec getMono(int nSamples) throws VecException, SoundException {

		double d[];
		long tmp;
		Vec v=null;

		nSamples = (int) (nSamples*m_M/m_L);
		d=new double[nSamples];

		if (getChannels()==1) {
			if (frameSize==3)
				for (int i=0;i<nSamples;++i) {
					tmp=(data_in[frameSize*i+2] << 16) + (data_in[frameSize*i+1] << 8) + (data_in[frameSize*i] & 0x00ff);
					d[i]=(double) tmp;
				}
			else if (frameSize==2)
				for (int i=0;i<nSamples;++i) {
					tmp=(data_in[frameSize*i+1] << 8) + (data_in[frameSize*i] & 0x00ff);
					d[i]=(double) tmp;
				}
			else if (frameSize==1)
				for (int i=0;i<nSamples;++i) {
					tmp=data_in[frameSize*i] & 0x00ff;
					d[i]=(double) tmp;
				}
			else throw new SoundException(3);
			v=new Vec(d);			
		} else if (getChannels()==2) {
			v=getStereoLeft(nSamples).add(getStereoRight(nSamples)).mult(0.5);			
		} else throw new SoundException(4);

		return(m_sample.resample(v, m_L, m_M));
	}

	/**
	 * Retorna el canal esquerre del bloc de dades emmagatzemat en memòria.<p>
	 * Si el fitxer només conté un canal, el retorna. 
	 * 
	 *  @return	vector que conté el canal esquerre
	*/
	public Vec getStereoLeft() throws VecException, SoundException {
		
		return(getStereoLeft((int) (data_in.length*m_L/(m_M*frameSize))));
	}
	
	/**
	 * Retorna <code> nSamples </code> del canal esquerre del bloc de dades emmagatzemat en memòria.<p>
	 * Si el fitxer només conté un canal, el retorna. 
	 * Cal que <code> nSamples </code> sigui menor o igual al tamany del bloc especificat mitjançant la funció <code> init <c/ode> 
	 * 
	 *  @return	vector de longitud <code> nSamples </code> que conté el canal esquerre
	*/
	public Vec getStereoLeft(int nSamples) throws VecException, SoundException {

		double d[];
		long tmp;
		Vec v = null;

		nSamples = (int) (nSamples*m_M/m_L);
		d=new double[nSamples];
		
		if (getChannels()==2) {
			if (frameSize==6)
				for (int i=0;i<nSamples;++i) {
					tmp=(data_in[frameSize*i+2] << 16) + (data_in[frameSize*i+1] << 8) + (data_in[frameSize*i] & 0x00ff);
					d[i]=(double) tmp;
				}
			else if (frameSize==4)
				for (int i=0;i<nSamples;++i) {
					tmp=(data_in[frameSize*i+1] << 8) + (data_in[frameSize*i] & 0x00ff);
					d[i]=(double) tmp;
			}
			else if (frameSize==2)
				for (int i=0;i<nSamples;++i)
					d[i]=data_in[frameSize*i]*256;
			else throw new SoundException(3);
			v=new Vec(d);
		} else if (getChannels()==1) {
			v=getMono(nSamples);
		} else throw new SoundException(4);
		
		return(m_sample.resample(v, m_L, m_M));
	}

	/**
	 * Retorna el canal dret del bloc de dades emmagatzemat en memòria.<p>
	 * Si el fitxer només conté un canal, el retorna. 

	 *  @return	vector que conté el canal dret
	*/
	public Vec getStereoRight() throws VecException, SoundException {

		return(getStereoRight((int) (data_in.length*m_L/(m_M*frameSize))));
	}

	/**
	 * Retorna <code> nSamples </code> del canal dret del bloc de dades emmagatzemat en memòria.<p>
	 * Si el fitxer només conté un canal, el retorna.
	 * Cal que <code> nSamples </code> sigui menor o igual al tamany del bloc especificat mitjançant la funció <code> init <c/ode> 
	 * 
	 *  @return	vector de longitud <code> nSamples </code> que conté el canal dret
	*/
	public Vec getStereoRight(int nSamples) throws VecException, SoundException {

		double d[];
		long tmp;
		Vec v = null;

		nSamples = (int) (nSamples*m_M/m_L);
		d=new double[nSamples];

		if (getChannels()==2) {
			if (frameSize==6)
				for (int i=0;i<nSamples;++i) {
					tmp=(data_in[frameSize*i+5] << 16) + (data_in[frameSize*i+4] << 8) + (data_in[frameSize*i+3] & 0x00ff);
					d[i]=(double) tmp;
				}
			else if (frameSize==4)
				for (int i=0;i<nSamples;++i) {
					tmp=(data_in[frameSize*i+3] << 8) + (data_in[frameSize*i+2] & 0x00ff);
					d[i]=(double) tmp;
				}
			else if (frameSize==2)
				for (int i=0;i<nSamples;++i)
					d[i]=data_in[frameSize*i+1]*256;
			else throw new SoundException(3);
			v=new Vec(d);
		} else if (getChannels()==1) {
			v=getMono(nSamples);
		} else throw new SoundException(4);

		return(m_sample.resample(v, m_L, m_M));
	}
}
