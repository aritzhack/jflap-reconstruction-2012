package util.arrows;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.QuadCurve2D.Double;




public class CurvedArrowHelper {


	public static double calculateAngle(CurvedArrow arrow) {
		return calculateAngle(arrow.getP1(), arrow.getP2());
	}

	public static double getYDisplacement(CurvedArrow arrow) {
		return getYDisplacement(arrow.getP1(), arrow.getP2());
	}

	public static double getXDisplacement(CurvedArrow arrow) {
		return getXDisplacement(arrow.getP1(), arrow.getP2());
	}

	public static double calculateAngle(Point2D p1, Point2D p2) {
		return Math.atan2(getYDisplacement(p1, p2), getXDisplacement(p1, p2));
	}

	public static double getYDisplacement(Point2D p1, Point2D p2) {
		return p2.getY() - p1.getY();
	}

	public static double getXDisplacement(Point2D p1, Point2D p2) {
		return p2.getX() - p1.getX();
	}

	public static Point2D getCenterPoint(CurvedArrow arrow){
		return getCenterPoint(arrow.getP1(), arrow.getP2());
	}


	public static Point2D getCenterPoint(Point2D p1, Point2D p2) {
		return cloneAndTranslatePoint(p1, getXDisplacement(p1, p2)/2, getYDisplacement(p1, p2)/2);
	}

	public static Point2D cloneAndTranslatePoint(Point2D point, double dx, double dy){
		Point2D newPoint = new Point2D.Double(point.getX()+dx, point.getY()+dy);
		return newPoint;
	}


	public static Point2D getCurveCenter(QuadCurve2D curve) {
		return getCurveCenter(curve.getP1(), curve.getCtrlPt(), curve.getP2());
	}

	public static Point2D getCurveCenter(Point2D p1, Point2D ctrlPt, Point2D p2) {
		Point2D center = CurvedArrowHelper.getCenterPoint(p1, p2);
		center = CurvedArrowHelper.getCenterPoint(center, ctrlPt);
		return center;
	}


}
