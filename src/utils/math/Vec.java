package utils.math;

import java.awt.geom.Point2D;

/**
 * Vec és una classe que ofereix mètodes per treballar amb vectors de <code>double</code>
 * 
 * @author Carles Vilella
 *
 */
public class Vec {
	protected double m_v[];

	/**
	* Constructor de la classe
	*/
	public Vec () { m_v=null; }

	/**
	* Constructor de la classe, indicant el tamany del vector
	*
	* @param 	n	tamany del vector
	*/
	public Vec (int n) { 

		m_v=new double[n]; 
		zeros();
	}


	/**
	* Constructor de la classe, indicant el valor inicial del vector
	*
	* @param 	v				valor inicial del vector	
	* @throws 	VecException 	si <code>v==null</code>
	*/
	public Vec (double v[]) throws VecException {

		set(v);
	}

	/**
	 * 
	*/
	public Vec (Point2D[] p, boolean X) throws VecException {

		this(p, p.length, X);
	}

	/**
	 * Pren nomes els primers n_p punts de l'array p
	*/
	public Vec (Point2D[] p, int n_p, boolean X) throws VecException {

		double[] v;
		
		v = new double[n_p];
		for (int i=0;i<n_p;++i)
			v[i]= (X == true) ? p[i].getX() : p[i].getY();
		
		set(v);
	}
	
	
	/**
	 * Genera un nou vector amb el contingut indicat (<code>double</code>)
	 * 
	 * @param	v				contingut del vector (double)
	 * @throws	VecException	si <code>v==null</code>
	*/
	public void set(double v[]) throws VecException {

		if (v==null) throw new VecException (1);

		m_v=new double[v.length];

		for (int i=0;i<v.length;++i)
			m_v[i]=v[i];
	}

	/**
	 * Genera un nou vector amb el contingut indicat (<code>byte</code>). <p>
	 * Converteix de <code>byte</code> a <code>double</code>
	 * 
	 * @param	v				contingut del vector (<code>byte</code>)
	 * @throws	VecException	si <code>v==null</code>
	*/
	public void set(byte v[]) throws VecException {

		if (v==null) throw new VecException (1);

		m_v=new double[v.length];

		for (int i=0;i<v.length;++i)
			m_v[i]=(double)v[i];
	}

	/**
	 * Sobreescriu part del contingut del vector. <p>
	 * El vector ja ha d'haver estat inicialitzat (mitjançant el constructor) o generat (mitjançant <code>set</code>)
	 * 
	 * @param 	p				posició inicial a sobreescriure
	 * @param	v				contingut del vector (<code>double</code>)
	 * @throws	VecException	si <code>v==null</code>, <code>p</code> negatiu, <code>p+v.length</code> major o igual que la longitud del vector 
	*/
	public void setSubvector(int p, double v[]) throws VecException {

		if (v==null) throw new VecException (1);
		if (p+v.length>=m_v.length) throw new VecException (2);
		if (p<=0) throw new VecException (4);

		for (int i=0;i<v.length;++i)
			m_v[p+i]=v[i];
	}

	/**
	 * Sobreescriu un element del vector. <p>
	 * El vector ja ha d'haver estat inicialitzat (mitjançant el constructor) o generat (mitjançant <code>set</code>)
	 * 
	 * @param 	d				valor a sobreescriure
	 * @param	p				posició dins el vector (començant per la 0)
	 * @throws	VecException	si <code>p</code> negatiu, <code>p</code> major que la longitud del vector 
	*/
	public void setElement(double d, int p) throws VecException {

		if (p<0) throw new VecException (4);
		if (p>=m_v.length) throw new VecException (2);

		m_v[p]=d;
	}

	/**
	 * Tamany del vector
	 * 
	 * @return	tamany del vector
	*/
	public int size() { return(m_v.length); }

	/**
	 * Sobreescriu el vector amb tot zeros
	 * El vector ja ha d'haver estat inicialitzat (mitjançant el constructor) o generat (mitjançant <code>set</code>)
	 *  
	*/
	public void zeros() {

		for (int i=0;i<m_v.length;++i)
			m_v[i]=0;
	}

	/**
	 * Sobreescriu el vector amb tot uns. 
	 * <p>
	 * El vector ja ha d'haver estat inicialitzat (mitjançant el constructor) o generat (mitjançant <code>set</code>)
	 *  
	*/
	public void ones() {

		for (int i=0;i<m_v.length;++i)
			m_v[i]=1;
	}

	/**
	 * Retorna tot el contingut del vector (format <code>double[]</code>)
	 * 
	 * @return	contingut del vector  
	 * 
	*/
	public double[] get() { 

		double tmp[]=new double[m_v.length];

		for (int i=0;i<m_v.length;++i)
			tmp[i]=m_v[i];

		return (tmp); 
	}

	/**
	 * Retorna tot el contingut del vector (format <code>byte[]</code>)
	 * Converteix de <code>double</code> a <code>byte</code>
	 * 
	 * @return	contingut del vector  
	 * 
	*/
	public byte[] getBytes() { 

		byte tmp[]=new byte[m_v.length];

		for (int i=0;i<m_v.length;++i)
			tmp[i]=(byte)m_v[i];

		return (tmp); 
	}


	/**
	 * Retorna part del contingut del vector (format <code>double[]</code>)
	 * 
	 * @param 	p				posició inicial
	 * @param 	n				nombre de valors
	 * @return					contingut del vector
	 * @throws	VecException	si <code>n</code> negatiu, <code>p+n</code> major o igual que la longitud del vector 
	 * 
	*/
	public double[] getSubVector(int p, int n) throws VecException { 

		if (n<=0) throw new VecException (1);
		if (p+n>m_v.length) throw new VecException (2);

		double tmp[]=new double[n];

		for (int i=0;i<n;++i)
			tmp[i]=m_v[p+i];
		return (tmp); 
	}

	/**
	 * Retorna part del contingut del vector (format <code>Vec</code>)
	 * 
	 * @param 	p				posició inicial
	 * @param 	n				nombre de valors
	 * @return					contingut del vector
	 * @throws	VecException	si <code>n</code> negatiu, <code>p+n</code> major o igual que la longitud del vector 
	 * 
	*/
	public Vec subVector(int p, int n) throws VecException { 

		if (n<=0) throw new VecException (1);
		if (p+n>m_v.length) throw new VecException (2);

		Vec v=new Vec();

		v.set(getSubVector(p,n));

		return (v); 
	}

	/**
	 * 
	*/
	public double getElement(int p) throws VecException {

		if (p<0) throw new VecException (1);
		if (p>=m_v.length) throw new VecException (2);

		return(m_v[p]);
	}

	/**
	 * 
	*/
	public void concat(double v[]) throws VecException { 

		if (v==null) throw new VecException (1);

		double tmp[]=new double[m_v.length+v.length];

		for (int i=0;i<m_v.length;++i)
			tmp[i]=m_v[i];
		
		for (int i=0;i<v.length;++i)
			tmp[m_v.length+i]=v[i];

		m_v=tmp;
	}

	/**
	 * 
	*/
	public void concat(Vec v) throws VecException { 

		concat(v.getSubVector(0, v.size()));
	}


	/**
	 * 
	*/
	public Vec abs() throws VecException { 

		double tmp[];
		tmp = new double[m_v.length];
		
		for (int i=0;i<m_v.length;++i)
			tmp[i]=Math.abs(m_v[i]);

		return (new Vec(tmp));
	}

	/**
	 * 
	*/
	public void acc(Vec v) throws VecException { 

		if (v.size()!=m_v.length) throw new VecException (3);

		double tmp[];
		tmp=v.get();

		for (int i=0;i<m_v.length;++i)
			m_v[i]+=tmp[i];
	}

	/**
	 * 
	*/
	public Vec add(Vec v) throws VecException { 

		if (v.size()!=m_v.length) throw new VecException (3);

		double tmp[];
		tmp=v.get();

		for (int i=0;i<m_v.length;++i)
			tmp[i]+=m_v[i];

		Vec r=new Vec();
		r.set(tmp);
		return (r);
	}

	/**
	 * Producte punt a punt 
	*/
	public Vec mult(Vec v) throws VecException { 

		if (v.size()!=m_v.length) throw new VecException (3);

		double tmp[];
		tmp=v.get();

		for (int i=0;i<m_v.length;++i)
			tmp[i]*=m_v[i];

		Vec r=new Vec();
		r.set(tmp);
		return (r);
	}
	
	/**
	 * Producte vector per escalar 
	*/
	public Vec mult(double d) throws VecException { 

		double tmp[]=new double[m_v.length];

		for (int i=0;i<m_v.length;++i)
			tmp[i]=m_v[i]*d;

		Vec r=new Vec();
		r.set(tmp);
		return (r);
	}

	/**
	 * Divisió punt a punt ( this ./ v ) 
	*/
	public Vec div(Vec v) throws VecException { 

		if (v.size()!=m_v.length) throw new VecException (3);

		double tmp[]=v.get();
		for (int i=0;i<m_v.length;++i)
			tmp[i]=m_v[i]/tmp[i];

		return (new Vec(tmp));
	}
	
	
	/**
	 * 
	*/
	public double dot(Vec v) throws VecException { 

		if (v.size()!=m_v.length) throw new VecException (3);

		double v_tmp[];
		v_tmp=v.get();
		double r;

		r=0;
		for (int i=0;i<m_v.length;++i)
			r+=v_tmp[i]*m_v[i];

		return (r);
	}

	/**
	 * 
	*/
	public double max() throws VecException { 

		double max;
		
		max=m_v[0];
		for (int i=1;i<m_v.length;++i)
			if (max<m_v[i]) max=m_v[i];

		return (max);
	}
	
	/**
	 * 
	*/
	public double min() throws VecException { 

		double min;
		
		min=m_v[0];
		for (int i=1;i<m_v.length;++i)
			if (min>m_v[i]) min=m_v[i];

		return (min);
	}

	/**
	 * 
	*/
	public double norm2() throws VecException { 

		double mag=0;
		
		for (int i=0;i<m_v.length;++i)
			mag+=m_v[i]*m_v[i];

		return (Math.sqrt(mag));
	}
	
	
	/**
	 * 
	*/
	public String toString() { 
		String s;

		s="[";
		for (int i=0;i<m_v.length-1;++i) {
			s=s+String.valueOf(m_v[i])+", ";
			}

		if (m_v.length==0)
			s+="]";
		else
			s+=String.valueOf(m_v[m_v.length-1])+"]";
		return (s);
	}
}
