package view.automata;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.automata.Automaton;
import util.arrows.GeometryHelper;
import util.view.magnify.MagnifiablePanel;

public class AutomatonEditorPanel<T extends Automaton> extends MagnifiablePanel {

	public LabelBounds getLabelBounds(Point2D center, Point2D pFrom, Point2D pTo, Graphics g) {

		double angle = GeometryHelper.calculateAngle(pFrom,pTo);
		//calculate bounds
		FontMetrics metrics = g.getFontMetrics();
		String label = this.toString();
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX()-w/2);
		int y = (int) (center.getY()-h/2);

		return new LabelBounds(angle, new Rectangle(x, y, w, h));
	}

}
