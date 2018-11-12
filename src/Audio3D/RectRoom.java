package Audio3D;



public class RectRoom {

	private double m_x1, m_y1, m_z1;
	private double m_x2, m_y2, m_z2;
	
	private int m_order;
	private int m_max_images;
	
	/**
	 * 
	*/
	public RectRoom () {
		m_x1=m_y1=m_z1=m_x2=m_y2=m_z2=0;
	}

	/**
	 * 
	*/
	public void init (double x1, double y1, double z1, double x2, double y2, double z2, int order, int max_images) throws Exception {

		if (max_images>6) throw new RoomException(1);
		if (order > 1) throw new RoomException(2);

		m_order = order;
		m_max_images = max_images;

		m_x1=x1; m_y1=y1; m_z1=z1;
		m_x2=x2; m_y2=y2; m_z2=z2;
	}

	/**
	 * 
	*/
	public void init (double v1[], double v2[], int order, int max_images) throws Exception {

		init (v1[0], v1[1], v1[2], v2[0], v2[1], v2[2], order, max_images);
	}

	public ImageSource[] getImageSources (Source source, double A[]) throws Exception {

		ImageSource imageSources[];
		
		imageSources=new ImageSource[m_max_images];		
		for (int i=0;i<m_max_images;++i) {
			imageSources[i]=new ImageSource();
			imageSources[i].init(source);
			imageSources[i].setAbsFilter(0, A[i]);
			imageSources[i].setDstFilter(0);
		}
		
		return (imageSources);
	}
	
	/**
	 * 
	*/
	public void updateImageSources (Source source, ImageSource imageSources[]) throws Exception {
		
		if (m_order >= 1) {
			
			// Lateral - esquerra
			if (m_max_images > 0) {
				imageSources[0].setPosition(2*m_x1-source.getPX(), source.getPY(), source.getPZ());
				imageSources[0].setOrientation(-source.getOX(), source.getOY(), source.getOZ());
			}
			
			// Lateral - dreta
			if (m_max_images > 1) {
				imageSources[1].setPosition(2*m_x2-source.getPX(), source.getPY(), source.getPZ());
				imageSources[1].setOrientation(-source.getOX(), source.getOY(), source.getOZ());
			}

			// Davant / darrera - aquí
			if (m_max_images > 2) {
				imageSources[2].setPosition(source.getPX(), 2*m_y1-source.getPY(), source.getPZ());
				imageSources[2].setOrientation(source.getOX(), -source.getOY(), source.getOZ());
			}

			// Davant / darrera - allà
			if (m_max_images > 3) {
				imageSources[3].setPosition(source.getPX(), 2*m_y2-source.getPY(), source.getPZ());
				imageSources[3].setOrientation(source.getOX(), -source.getOY(), source.getOZ());
			}

			// Dalt / baix - terra
			if (m_max_images > 4) {
				imageSources[4].setPosition(source.getPX(), source.getPY(), 2*m_z1-source.getPZ());
				imageSources[4].setOrientation(source.getOX(), source.getOY(), -source.getOZ());
			}
		
			// Dalt / baix - sostre
			if (m_max_images > 5) {
				imageSources[5].setPosition(source.getPX(), source.getPY(), 2*m_z2-source.getPZ());
				imageSources[5].setOrientation(source.getOX(), source.getOY(), -source.getOZ());
			}				
		} 
	}
}
