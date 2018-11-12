package Audio3D;



import java.io.IOException;
import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;

import utils.math.*;

import java.io.File;

/**
 * 
 * Implementa l'escriptura d'un fitxer d'àudio stèreo.<p> 
 * La classe admet dades per blocs de qualsevol tamany (funció <code> writeStereo </code>), que s'emmagatzemen temporalment en memòria. <p>
 * L'escriptura real a fitxer no es realitza fins que:     
 * <ul>
 * <li> S'assoleix el tamany predeterminat del fitxer, especificat mitjançant la funció <code> init </code>
 * <li> Crida a la funció <code> close </code>  
 * </ul> 
 * 
 * @author Carles Vilella
 *
 */

public class SndToFile {

	private File m_soundFile;

	private ByteArrayInputStream m_byteArrayInputStream;
	private AudioInputStream m_audioInputStream;
	private AudioFormat m_audioFormat;

	private byte m_data[];
	private int m_data_index;
	private int m_end_file;
	
	/**
	 * 
	*/
	public SndToFile () {
		
		m_soundFile = null;
		m_byteArrayInputStream = null;
		m_audioInputStream = null;
		m_audioFormat = null;
		m_data = null;
		m_data_index = 0;
		m_end_file = 0;
	}
	
	/**
	 * 
	*/
	public void init (AudioFormat audioFormat, String file_name, int sec) {
		
		m_soundFile=new File(file_name);
		m_audioFormat=new AudioFormat (audioFormat.getEncoding(), audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), audioFormat.isBigEndian());
		m_data = new byte[sec*(int)audioFormat.getSampleRate()*audioFormat.getFrameSize()];
	}
	
	/**
	 * 
	*/
	public void close() throws IOException {


		if ( (m_end_file == 0) || (m_end_file == 1) ) {
			m_end_file=2;			
			writeFile();
			m_audioInputStream.close();
		}
	}
	
	/**
	 * 
	*/
	public String getFileName () {

		return (m_soundFile.getName());
	}

	/**
	 * 
	*/
	public void writeStereo(Vec v_l, Vec v_r) throws IOException, VecException {

		byte data_tmp[];

		if (m_end_file==0) {
			if (m_audioFormat.getChannels()==2) {
	
				int l;
	
				data_tmp=vecToByte (v_l, v_r);
				l=data_tmp.length;
				
				if (m_data_index+l > m_data.length) {
					l=m_data.length-m_data_index;
					m_end_file=1;
				}
	
				for (int i=0;i<l;++i)
						m_data[m_data_index+i]=data_tmp[i];
					
				m_data_index+=l;
			}
		}
		
		if (m_end_file == 1)
			close();
		
	}
	
	/**
	 * 
	*/
	private byte[] vecToByte (Vec v_l, Vec v_r) throws VecException{
		
		byte data[];
		int n; int tmp;
		
		data=null;
		n=v_l.size();
		if (m_audioFormat.getSampleSizeInBits() == 16) {
			data=new byte[4*n];
			
			for (int i=0;i<n;++i) {
				tmp=(int) v_r.getElement(i);
				data[4*i+3]= (byte) ((tmp & 0xff00) >> 8);
				data[4*i+2]= (byte) (tmp & 0x00ff);

				tmp=(int) v_l.getElement(i);
				data[4*i+1]= (byte) ((tmp & 0xff00) >> 8);
				data[4*i+0]= (byte) (tmp & 0x00ff);				
			}
		}
		
		return (data);
	}

	/**
	 * 
	*/
	private void writeFile () throws IOException {
		
		if (m_audioFormat.getChannels()==2) {

			m_byteArrayInputStream=new ByteArrayInputStream(m_data);
			m_audioInputStream=new AudioInputStream(m_byteArrayInputStream, m_audioFormat, m_data_index/m_audioFormat.getFrameSize());	
			AudioSystem.write(m_audioInputStream, AudioFileFormat.Type.WAVE, m_soundFile);
			
			System.out.println("Output file "+m_soundFile+" written.");
		}
	}
	
	public boolean end () {
		
		if ( m_end_file == 2 ) return (true);
		else return (false);
	}
	
}
