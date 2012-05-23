package util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.QuadCurve2D.Double;

import model.automata.Transition;




public class GraphHelper {

	public static double calculateAngle(Transition t) {
		return calculateAngle(t.getFromState().getPoint(), t.getToState().getPoint());
	}

//	public static double calculateAngle(CurvedArrow arrow) {
//		return calculateAngle(arrow.getP1(), arrow.getP2());
//	}
//
//	public static double getYDisplacement(CurvedArrow arrow) {
//		return getYDisplacement(arrow.getP1(), arrow.getP2());
//	}
//
//	public static double getXDisplacement(CurvedArrow arrow) {
//		return getXDisplacement(arrow.getP1(), arrow.getP2());
//	}

	public static double calculateAngle(Point2D p1, Point2D p2) {
		return Math.atan2(getYDisplacement(p1, p2), getXDisplacement(p1, p2));
	}

	public static double getYDisplacement(Point2D p1, Point2D p2) {
		return p2.getY() - p1.getY();
	}

	public static double getXDisplacement(Point2D p1, Point2D p2) {
		return p2.getX() - p1.getX();
	}

//	public static Point2D getCenterPoint(CurvedArrow arrow){
//		return getCenterPoint(arrow.getP1(), arrow.getP2());
//	}

	public static Point getCenterPoint(Transition t) {
		return getCenterPoint(t.getFromState().getPoint(), t.getToState().getPoint());
	}

	public static Point getCenterPoint(Point2D p1, Point2D p2) {
		return cloneAndTranslatePoint(p1, getXDisplacement(p1, p2)/2, getYDisplacement(p1, p2)/2);
	}

	public static Point cloneAndTranslatePoint(Point2D point, double dx, double dy){
		Point newPoint = new Point((int) (point.getX()+dx), (int)(point.getY()+dy));
		return newPoint;
	}

	public static Point2D getCurveCenter(Transition t, Point ctrl) {
		return getCurveCenter(t.getFromState().getPoint(), 
				ctrl, 
				t.getToState().getPoint());
	}

	public static Point2D getCurveCenter(QuadCurve2D curve) {
		return getCurveCenter(curve.getP1(), curve.getCtrlPt(), curve.getP2());
	}

	public static Point2D getCurveCenter(Point2D p1, Point2D ctrlPt, Point2D p2) {
		Point2D center = GraphHelper.getCenterPoint(p1, p2);
		center = GraphHelper.getCenterPoint(center, ctrlPt);
		return center;
	}

	public static double calculatePerpendicularAngle(Transition t) {
		return calculateAngle(t) + Math.PI/2;
	}

}
