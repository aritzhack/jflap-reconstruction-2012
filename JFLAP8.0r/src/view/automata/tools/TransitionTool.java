package view.automata.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import util.JFLAPConstants;
import view.automata.AutomatonEditorPanel;

public class TransitionTool<T extends Automaton<S>, S extends Transition<S>>
		extends EditingTool<T, S> {

	private State from;
	private Point2D pCurrent;
	private Point2D pFrom;

	public TransitionTool(AutomatonEditorPanel<T, S> panel) {
		super(panel);
		from = null;
		pCurrent = pFrom = null;

	}

	@Override
	public String getToolTip() {
		return "Transition Creator";
	}

	@Override
	public char getActivatingKey() {
		return 't';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/transition.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		Object obj = panel.objectAtPoint(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (obj instanceof State) {
				from = (State) obj;
				pFrom = pCurrent = panel.getGraph().pointForVertex(from);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (from != null) {
			pCurrent = e.getPoint();
			getPanel().repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (from != null) {
			AutomatonEditorPanel<T, S> panel = getPanel();
			Object obj = panel.objectAtPoint(e.getPoint());

			if (obj instanceof State) {
				panel.createTransition(from, (State) obj);
			}
			from = null;
		}
	}

	@Override
	public void draw(Graphics g) {
		if (from != null) {
			Graphics2D g2 = (Graphics2D) g;
			Stroke s = g2.getStroke();
			g2.setStroke(JFLAPConstants.DEFAULT_TRANSITION_STROKE);
			g2.setColor(JFLAPConstants.DEFAULT_TRANSITION_COLOR);
			g2.drawLine((int) pFrom.getX(), (int) pFrom.getY(),
					(int) pCurrent.getX(), (int) pCurrent.getY());
			g2.setStroke(s);
		}
	}

}
