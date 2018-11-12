

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
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package TUIO;

import java.awt.BasicStroke;
import java.awt.geom.*;

import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import TUIO.*;

public class TuioGrafObject extends TuioObject {

	private Shape square;
        private Shape triangle;
        private TuioSounding bar;
        private Shape barOUT, barIN;
       

	public TuioGrafObject(TuioObject tobj) {
		super(tobj);
                
		int size = TuioGrafInfo.object_size;
                bar = new TuioSounding();
                square = new RoundRectangle2D.Float(-size/2,-size/2,size,size, size/2, size/2);

		AffineTransform transform = new AffineTransform();
		transform.translate(xpos,ypos);
		transform.rotate(angle,xpos,ypos);
		square = transform.createTransformedShape(square);

                //triangle = new Polygon();
                int[] X = {-size/4, 0, size/4};
                int[] Y = {size/3, -size/3, size/3};
                triangle = new Polygon(X,Y,3);
	}

	public void paint(Graphics2D g, int width, int height) {
                int size = TuioGrafInfo.object_size;
		float Xpos = xpos*width;
		float Ypos = (-ypos+1)*height;

                //System.out.println("Xpos: "+Xpos+" xpos"+xpos);
		float scale = height/(float)TuioGrafInfo.table_size;

		AffineTransform trans = new AffineTransform();
		trans.translate(-xpos,-ypos);
		trans.translate(Xpos,Ypos);
		trans.scale(scale,scale);
		Shape s = trans.createTransformedShape(square);
                Stroke k = new BasicStroke(size/10);
                Shape s2 = trans.createTransformedShape(k.createStrokedShape(square));
                barOUT = bar.LoadEmpty();
                barOUT = trans.createTransformedShape(barOUT);
                barIN = bar.LoadFulling();
                barIN = trans.createTransformedShape(barIN);

                Color BlueOUT = new Color(51,51,255,255);
                Color BlueIN = new Color(51,51,100,255);
                Color RedIN = new Color(142,31,13,255);
                Color RedOUT = new Color(206,31,22,255);

       

                if (symbol_id == 3){
                    
                    g.setColor(BlueIN);
                    g.fill(s);
                    
                    g.setColor(BlueOUT);
                    g.fill(s2);

                    g.setColor(BlueOUT);
                    g.fill(barOUT);

                    g.setColor(BlueIN);
                    g.fill(barIN);
                    }
                if (symbol_id == 4){
                    
                    g.setColor(BlueIN);
                    g.fill(s);
                    
                    g.setColor(BlueOUT);
                    g.fill(s2);
                    
                    g.setColor(BlueOUT);
                    g.fill(barOUT);

                    g.setColor(BlueIN);
                    g.fill(barIN);
                    }



                if (symbol_id == 2){
                    
                    g.setColor(RedIN);
                    g.fill(s);
                    
                    g.setColor(RedOUT);
                    g.fill(s2);

                    Shape s3 = trans.createTransformedShape(triangle);
                    g.setColor(Color.BLACK);
                    g.fill(s3);

                    g.setColor(RedOUT);
                    g.fill(barOUT);
                    
                    
                }

		g.setPaint(Color.white);
	}

	public void update(TuioObject tobj) {

		float dx = tobj.getX() - xpos;
		float dy = tobj.getY() - ypos;
		float da = tobj.getAngle() - angle;

		if ((dx!=0) || (dy!=0)) {
			AffineTransform trans = AffineTransform.getTranslateInstance(dx,dy);
			square = trans.createTransformedShape(square);
                        triangle = trans.createTransformedShape(triangle);
                        barOUT = trans.createTransformedShape(barOUT);
                        barIN = trans.createTransformedShape(barIN);
		}

		if (da!=0) {
			AffineTransform trans = AffineTransform.getRotateInstance(da,tobj.getX(),tobj.getY());
			square = trans.createTransformedShape(square);
                        triangle = trans.createTransformedShape(triangle);
                        barOUT = trans.createTransformedShape(barOUT);
                        barIN = trans.createTransformedShape(barIN);
		}

		super.update(tobj);
	}

}

