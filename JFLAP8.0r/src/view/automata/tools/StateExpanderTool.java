package view.automata.tools;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import errors.BooleanWrapper;
import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;

public class StateExpanderTool extends EditingTool<FiniteStateAcceptor, FSATransition>{

	private NFAtoDFAConverter myAlg;

	public StateExpanderTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, NFAtoDFAConverter convert) {
		super(panel);
		myAlg = convert;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
		Object o = panel.objectAtPoint(e.getPoint());
		if(o instanceof State){
			BooleanWrapper wrap = myAlg.expandState((State) o);
			if(wrap.isError())
				JOptionPane.showMessageDialog(panel, wrap.getMessage());
		}
	}
	@Override
	public String getToolTip() {
		return "State Expander";
	}

	@Override
	public char getActivatingKey() {
		return 's';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/state_expander.gif";
	}

}
