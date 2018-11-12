package utils.others;

import java.awt.Rectangle;

public class RotRectangle extends Rectangle {
	// Encapsula una rectangle i un angle de rotació
		
	public double angle=0;	// Per conveni, en radiants
	
	public RotRectangle (int x, int y, int w, int h, double alfa) {
		
		super(x, y, w, h);
		angle = alfa;
	}

	public RotRectangle (Rectangle rectangle, double alfa) {
		
		super(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		angle = alfa;
	}

	public void setBounds (Rectangle rectangle, double alfa) {
		
		super.setBounds(rectangle);
		angle = alfa;
	}

	public void resize (double scale_x, double scale_y) {

		int inc_x, inc_y; 
		
		inc_x = width - (int) ( (double) width * scale_x);
		inc_y = height - (int) ( (double) height * scale_y);
		
		setBounds (x+inc_x/2, y+inc_y/2, width-inc_x, height-inc_y);
		
	}
	
}
