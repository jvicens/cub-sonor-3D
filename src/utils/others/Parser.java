package utils.others;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Parser {

	public static final int MAX_LINES=5000;
	
	private FileReader m_fr;
	private BufferedReader m_br;
	public String m_lines[];
	public int m_nlines;
	
	/*
	 * 
	 */
	public Parser () {
	}
	
	/*
	 * 
	 */	
	public void init (String filename) throws FileNotFoundException, IOException, ParserException {

		 m_fr = new FileReader(filename);
		 m_br = new BufferedReader(m_fr);
		 
		 m_lines=new String[MAX_LINES];

		 m_nlines=0;
		 while ( (m_br.ready()) && (m_nlines < MAX_LINES) )
			 m_lines[m_nlines++]=m_br.readLine();
		
		 if (m_br.ready()) throw new ParserException(3);
		 
	 	 m_br.close();
 		 m_fr.close();
	}

	/*
	 * 
	 */
	public boolean exist (String var) throws ParserException {

		int i=0;
		while (i < m_nlines ) 
			if ( (m_lines[i].startsWith("%")) || ( m_lines[i].indexOf(var) == -1 ) ) ++i;
			else break;		
		
		if ( i == m_nlines ) return (false);
		else return (true);
	}

	
	/*
	 * 
	 */
	public int getInt (String var) throws ParserException {

		int v;
		int sufix=0;
		
		int i=0;
		while (i < m_nlines ) 
			if ( m_lines[i].startsWith("%") ) ++i;
			else if ( ( ! m_lines[i].startsWith(var+" ") ) && (! m_lines[i].startsWith(var+"=") ) ) ++i;
			else break;		
		
		if ( i == m_nlines ) throw new ParserException(1);

		if (!m_lines[i].endsWith(";")) System.out.println(m_lines[i]); 
		else sufix=1;  
		
		int p=m_lines[i].indexOf("=")+1;
		
		v=Integer.parseInt(m_lines[i].substring(p, m_lines[i].length()-sufix).trim());
		
		return (v);
	}

	/*
	 * 
	 */
	public int[] getIntArray (String var) throws ParserException {

		int v[];
		
		int i=0;
		while (i < m_nlines ) 
			if ( m_lines[i].startsWith("%") ) ++i;
			else if ( ( ! m_lines[i].startsWith(var+" ") ) && (! m_lines[i].startsWith(var+"=") ) ) ++i;
			else break;		
		
		if ( i == m_nlines ) throw new ParserException(1);

		if (!m_lines[i].endsWith(";")) System.out.println(m_lines[i]);  

		int p1=m_lines[i].indexOf("[")+1, p2;
		
		if ( p1 == -1) throw new ParserException(2);
		
		int j, n_ints=1;
		for (j=p1;j<m_lines[i].length();++j)
			if (m_lines[i].charAt(j)==',')
				++n_ints;

		v=new int[n_ints];
		
		for (j=0;j<n_ints-1;++j) {
			p2=m_lines[i].indexOf(',', p1);
			v[j]=Integer.parseInt(m_lines[i].substring(p1, p2).trim());
			p1=p2+1;			
		}

		p2=m_lines[i].indexOf(']', p1);
		if ( p2 == -1) throw new ParserException(2);
		
		v[j]=Integer.parseInt(m_lines[i].substring(p1, p2).trim());
		
		return (v);
	}

	/*
	 * 
	 */
	public double getDouble (String var) throws ParserException {

		double v;
		int sufix=0;
		
		int i=0;
		while (i < m_nlines ) 
			if ( m_lines[i].startsWith("%") ) ++i;
			else if ( ( ! m_lines[i].startsWith(var+" ") ) && (! m_lines[i].startsWith(var+"=") ) ) ++i;
			else break;		
		
		if ( i == m_nlines ) throw new ParserException(1);

		if (!m_lines[i].endsWith(";")) System.out.println(m_lines[i]); 
		else sufix=1;  

		int p=m_lines[i].indexOf("=")+1;
		
		v=Double.parseDouble(m_lines[i].substring(p, m_lines[i].length()-sufix).trim());
		
		return (v);
	}

	/*
	 * 
	 */
	public double[] getDoubleArray (String var) throws ParserException {

		double v[];
		
		int i=0;
		while (i < m_nlines ) 
			if ( m_lines[i].startsWith("%") ) ++i;
			else if ( ( ! m_lines[i].startsWith(var+" ") ) && (! m_lines[i].startsWith(var+"=") ) ) ++i;
			else break;		
		
		if ( i == m_nlines ) throw new ParserException(1);

		if (!m_lines[i].endsWith(";")) System.out.println(m_lines[i]);  

		int p1=m_lines[i].indexOf("[")+1, p2;
		
		if ( p1 == -1) throw new ParserException(2);
		
		int j, n_ints=1;
		for (j=p1;j<m_lines[i].length();++j)
			if (m_lines[i].charAt(j)==',')
				++n_ints;

		v=new double[n_ints];
		
		for (j=0;j<n_ints-1;++j) {
			p2=m_lines[i].indexOf(',', p1);
			v[j]=Double.parseDouble(m_lines[i].substring(p1, p2).trim());
			p1=p2+1;			
		}

		p2=m_lines[i].indexOf(']', p1);
		if ( p2 == -1) throw new ParserException(2);
		
		v[j]=Double.parseDouble(m_lines[i].substring(p1, p2).trim());
		
		return (v);
	}

	/*
	 * 
	 */
	public String getString (String var) throws ParserException {

		int i=0;
		while (i < m_nlines ) 
			if ( m_lines[i].startsWith("%") ) ++i;
			else if ( ( ! m_lines[i].startsWith(var+" ") ) && (! m_lines[i].startsWith(var+"=") ) ) ++i;
			else break;		
		
		if ( i == m_nlines ) throw new ParserException(1);

		if (!m_lines[i].endsWith(";")) System.out.println(m_lines[i]);  

		int p1=m_lines[i].indexOf("\"")+1;
		int p2=m_lines[i].lastIndexOf("\"");

		return (m_lines[i].substring(p1,p2).trim());
	}
	
	/**
	 * 
	 * @param bw
	 * @param label
	 * @param intArray
	 * @throws IOException
	 */
	public static void putIntArray (BufferedWriter bw, String label, int[] intArray) throws IOException {

		int i;
		
		bw.write(label+" = [");
		for (i=0;i<intArray.length-1;++i)
			bw.write(intArray[i]+", ");
		bw.write(intArray[i]+"]");
		bw.newLine();
	}

	/**
	 * 
	 * @param bw
	 * @param label
	 * @param doubleArray
	 * @throws IOException
	 */
	public static void putDoubleArray (BufferedWriter bw, String label, double[] doubleArray) throws IOException {

		int i;
		
		bw.write(label+" = [");
		for (i=0;i<doubleArray.length-1;++i)
			bw.write(doubleArray[i]+", ");
		bw.write(doubleArray[i]+"]");
		bw.newLine();
	}
	
	
}
