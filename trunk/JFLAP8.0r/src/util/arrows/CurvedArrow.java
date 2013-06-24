/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package util.arrows;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import debug.JFLAPDebug;



/**
 * This is a simple class for storing and drawing a curved line with possible
 * arrow heads on it.
 * 
 * @author Thomas Finley
 */

public class CurvedArrow extends QuadCurve2D.Double {
	
	
	public ArrowHead myHead;
	
	public CurvedArrow(Point2D p1, Point2D ctrl, Point2D p2, double headLength, double headAngle) {
		this(headLength, headAngle);
		this.setCurve(p1, ctrl, p2);
	}
	
	public CurvedArrow(double headLength, double headAngle) {
		myHead = new ArrowHead(headLength, headAngle);
	}

	private double calculateHeadAngle() {
		return GeometryHelper.calculateAngle(this.getP2(), this.getCtrlPt()) - Math.PI/2;
	}

	public ArrowHead getHead(){
		return myHead;
	}
	

	@Override
	public void setCurve(Point2D p1, Point2D cp, Point2D p2) {
		super.setCurve(p1, cp, p2);
		myHead.setLocation(p2);
		myHead.setDirection(calculateHeadAngle());
	}

	@Override
	public void setCurve(QuadCurve2D c) {
		this.setCurve(c.getP1(), c.getCtrlPt(), c.getP2());
		
	}

	
	public void draw(Graphics g) {
		((Graphics2D) g).draw(this);
		myHead.draw(g);
	}
	
	public Point2D getCenterPoint(){
		return GeometryHelper.getCenterPoint(this);
	}
	
	public double getYDisplacement() {
		return GeometryHelper.getYDisplacement(this);
	}

	public double getXDisplacement() {
		return GeometryHelper.getXDisplacement(this);
	}
	
	public static boolean intersects(Point2D point, int fudge, QuadCurve2D.Double c) {
		if (!c.intersects(point.getX() - fudge, point.getY() - fudge, fudge << 1,
				fudge << 1))
			return false;
		if (c.getFlatness() < fudge)
			return true;
		QuadCurve2D.Double f1 = new QuadCurve2D.Double(), f2 = new QuadCurve2D.Double();
		c.subdivide(f1, f2);
		return intersects(point, fudge, f1) || intersects(point, fudge, f2);
	}


}
