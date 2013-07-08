package view.automata.views;

import javax.swing.JComponent;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.moore.MooreMachine;
import model.undo.UndoKeeper;
import view.MooreEditorPanel;
import view.automata.AutomatonEditorPanel;
import view.automata.tools.MooreStateTool;
import view.automata.tools.StateTool;

public class MooreView extends AutomataView<MooreMachine, FSATransition>{

	public MooreView(MooreMachine model) {
		super(model);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public StateTool<MooreMachine, FSATransition> createStateTool(
			AutomatonEditorPanel<MooreMachine, FSATransition> panel,
			MooreMachine def) {
		return new MooreStateTool((MooreEditorPanel) panel, def);
	}
	
	@Override
	public JComponent createCentralPanel(MooreMachine model, UndoKeeper keeper,
			boolean editable) {
		// TODO Auto-generated method stub
		return new MooreEditorPanel(model, keeper, editable);
	}

}
