package view.automata.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.SwingUtilities;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import util.Point2DAdv;
import view.automata.editing.AutomatonEditorPanel;

public class DeexpressionTransitionTool extends
		EditingTool<FiniteStateAcceptor, FSATransition> {

	private RegularExpressionToNFAConversion myAlg;

	public DeexpressionTransitionTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel,
			RegularExpressionToNFAConversion convert) {
		super(panel);
		myAlg = convert;
	}

	@Override
	public String getToolTip() {
		return "De-expressionify Transition";
	}

	@Override
	public char getActivatingKey() {
		return 'd';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/de-expressionify.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (SwingUtilities.isLeftMouseButton(e)) {
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
			
			Object o = panel.objectAtPoint(e.getPoint());
			if(o instanceof FSATransition){
				Set<FSATransition> existingTransitions = panel.getAutomaton().getTransitions().toCopiedSet();
				
				myAlg.beginDeExpressionify((FSATransition) o);
				
				Set<FSATransition> addedT = panel.getAutomaton().getTransitions().toCopiedSet();
				addedT.removeAll(existingTransitions);
				
				replaceTransition((FSATransition) o, addedT);
			}
		}

	}
	

	private void replaceTransition(FSATransition transition,
			Set<FSATransition> added) {
		// Compose the transform.
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
		
		AffineTransform at = new AffineTransform();
		Point2D pStart = panel.getPointForVertex(transition.getFromState());
		Point2D pEnd = panel.getPointForVertex(transition.getToState());
		at.translate(pStart.getX(), pStart.getY());
		at.scale(pStart.distance(pEnd), pStart.distance(pEnd));
		at.rotate(Math.atan2(pEnd.getY() - pStart.getY(), pEnd.getX() - pStart.getX()));

		Point2D.Double ps = new Point2D.Double(0.2, 0.0);
		Point2D.Double pe = new Point2D.Double(0.8, 0.0);

		int i = 0;
		for (FSATransition trans : added) {
			pStart = new Point();
			pEnd = new Point();
			double y = added.size() > 1 ? ((double) i
					/ ((double) added.size() - 1.0) - 0.5) * 0.5 : 0.0;
			pe.y = ps.y = y;
			at.transform(ps, pStart);
			at.transform(pe, pEnd);
			// Clamp bounds.
			pStart = new Point2DAdv(Math.max(pStart.getX(), 20), Math.max(pStart.getY(), 20));
			pEnd = new Point2DAdv(Math.max(pEnd.getX(), 20), Math.max(pEnd.getY(), 20));

			panel.moveState(trans.getFromState(), pStart);
			panel.moveState(trans.getToState(), pEnd);
			
			panel.moveCtrlPoint(trans.getFromState(), trans.getToState(), panel.getGraph().getDefaultControlPoint(trans.getFromState(), trans.getToState()));

			i++;
		}

	}

}
