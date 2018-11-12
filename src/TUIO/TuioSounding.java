

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Julian
 */
package TUIO;

import java.awt.*;
import java.awt.geom.*;
import java.lang.Math.*;



public class TuioSounding {
    Shape rectOUT, rectIN;
    Area rect1, rect2;
    int size = TuioGrafInfo.object_size;

    public TuioSounding(){
        rectOUT = new RoundRectangle2D.Double(-size/2,size/2 + (size/10) + 1,size,20,5,5);
        Stroke k = new BasicStroke(1);
        rectOUT = k.createStrokedShape(rectOUT);
    }
    public Shape LoadEmpty(){
        return rectOUT;
    }
    public Shape LoadFulling (){
        rectIN = new RoundRectangle2D.Double(-size/2, size/2 + (size/10) + 1, size, 20, 5, 5);
        return rectIN;
    }

}
