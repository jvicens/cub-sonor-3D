package Audio3D;



public class Path {

	private double m_path_px;
	private double m_path_py;
	private double m_path_pz;
	
	
	private double m_px;
	private double m_py;
	private double m_pz;
	
	
	private boolean m_active;
	private boolean m_end;
	/**
	 * 
	*/
	public Path () {
		m_path_px = 0; m_path_py = 0; m_path_pz = 0;
		
		m_px=0; m_py=0; m_pz=0; 
		
		
		m_active = false;
		m_end = false;
	}
	
	/**
	 * 
	*/
	public void init (double px, double py, double pz) {
		
		
			m_path_px=px;
			m_path_py=py;
			m_path_pz=pz;
			
		
		
		m_px=0; m_py=0; m_pz=0; 
		
	}
	
	/**
	 * 
	*/
	public void nextRelPos (Source source, double x_sou, double y_sou, double z_sou) {
		
		m_px = x_sou;
                m_py = y_sou;
                m_pz = z_sou;
                
		source.setActive(m_active);
		source.setEnd(m_end);		
		source.setPosition(m_px, m_py, m_pz);
	}

	/**
	 * Listener NO pot estar inactiu
	*/
	public void nextRelPos (Listener listener, double x_lis, double y_lis, double z_lis) {
		
		m_px = x_lis;
                m_py = y_lis;
		m_pz = z_lis;

		listener.setPosition(m_px, m_py, m_pz);
	}
	
	
	/**
	 * 
	*/
	public void nextPos (Source source) {

	
		source.setActive(m_active);
		source.setEnd(m_end);		
		source.setPosition(m_px, m_py, m_pz);
	}

	/**
	 *  Listener NO pot estar inactiu
	*/
	public void nextPos (Listener listener) {		

		listener.setPosition(m_px, m_py, m_pz);
	}

	/**
	 * 
	*/
        public void setActive(boolean active){
            m_active = active;

        }

}
