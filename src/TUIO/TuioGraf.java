

/*
	TUIO Java Demo - part of the reacTIVision project
	http://reactivision.sourceforge.net/

	Copyright (c) 2005-2009 Martin Kaltenbrunner <mkalten@iua.upf.edu>

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package TUIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import TUIO.*;

public class TuioGraf  {

	private final int window_width  = 640;
	private final int window_height = 480;

	private boolean fullscreen = true;

	private TuioGrafInfo ginfo;
	private JFrame frame;
	private GraphicsDevice device;

	public TuioGraf() {
		ginfo = new TuioGrafInfo();
		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		setupWindow();
		showWindow();
	}

	public TuioListener getTuioListener() {
		return ginfo;
	}

	public void setupWindow() {

		frame = new JFrame();
		frame.add(ginfo);

		frame.setTitle("TuioDemo");
		frame.setResizable(false);

		frame.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent evt) {
				System.exit(0);
			} });

		frame.addKeyListener( new KeyAdapter() { public void keyPressed(KeyEvent evt) {
			if (evt.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
			else if (evt.getKeyCode()==KeyEvent.VK_F1) {
				destroyWindow();
				setupWindow();
				fullscreen = !fullscreen;
				showWindow();
			}
			else if (evt.getKeyCode()==KeyEvent.VK_V) ginfo.verbose=!ginfo.verbose;
		} });
	}

	public void destroyWindow() {

		frame.setVisible(false);
		if (fullscreen) {
			device.setFullScreenWindow(null);
		}
		frame = null;
	}

	public void showWindow() {

		if (fullscreen) {
			int width  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			ginfo.setSize(width,height);

			frame.setSize(width,height);
			frame.setUndecorated(true);
			device.setFullScreenWindow(frame);
		} else {
			int width  = window_width;
			int height = window_height;
			ginfo.setSize(width,height);

			frame.pack();
			Insets insets = frame.getInsets();
			frame.setSize(width,height +insets.top);

		}

		frame.setVisible(true);
		frame.repaint();

	}
          public boolean[] getObjActive(){
            return (ginfo.getObjActive());
        }

        public float[][] getObjPosition(){
            return (ginfo.getObjPosition());
        }
        public float[][] getObjOrientation(){
            return (ginfo.getObjOrientation());
        }
        

}
