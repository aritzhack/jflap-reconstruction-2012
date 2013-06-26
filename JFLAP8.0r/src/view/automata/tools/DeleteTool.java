package view.automata.tools;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import view.automata.AutomatonEditorPanel;

/**
 * Tool used in the deleting of States, Transitions, edges, and Notes in an
 * Automaton graph
 * 
 * @author Ian McMahon
 */
public class DeleteTool<T extends Automaton<S>, S extends Transition<S>>
		extends EditingTool<T, S> {

	public DeleteTool(AutomatonEditorPanel<T, S> panel) {
		super(panel);
	}

	@Override
	public String getToolTip() {
		return "Deleter";
	}

	@Override
	public char getActivatingKey() {
		return 'd';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/delete.gif";
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			AutomatonEditorPanel<T, S> panel = getPanel();
			Object o = panel.objectAtPoint(e.getPoint());

			if (o instanceof State)
				panel.removeState((State) o);
			else if (o instanceof Transition)
				panel.removeTransition((S) o);
			else if (o instanceof State[])
				panel.removeEdge(((State[]) o)[0], ((State[]) o)[1]);
			panel.repaint();
		}
	}
}
