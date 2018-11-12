package utils.others;

import java.awt.Point;
import java.awt.Rectangle;

public class OpShape {

	/**
	 * 
	 * @param d
	 * @param in
	 * @return
	 */
	public static Rectangle txRectangle(Point d, Rectangle in) {
		
		Rectangle out;

		out =  new Rectangle (in);
		out.x += d.x;
		out.y += d.y;

		return (out);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param in
	 * @return
	 */
	public static Rectangle txRectangle(int x, int y, Rectangle in) {
		
		Rectangle out;

		out =  new Rectangle (in);
		out.x += x;
		out.y += y;

		return (out);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param in
	 * @return
	 */
	public static Point[] txPoint2d(int x, int y, Point[] in) {
		
		Point[] out;
		
		out = new Point[in.length];
		for (int i=0;i<in.length;++i)
			out[i] = new Point(in[i].x+x, in[i].y+y);

		return (out);
	}
	
}
