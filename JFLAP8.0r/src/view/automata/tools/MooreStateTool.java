package view.automata.tools;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.change.events.AddEvent;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.CompoundUndoRedo;
import universe.preferences.JFLAPPreferences;
import view.MooreEditorPanel;
import view.automata.undoing.StateAddEvent;

public class MooreStateTool extends StateTool<MooreMachine, FSATransition> {

	private MooreOutputFunction myFunction;

	public MooreStateTool(MooreEditorPanel panel, MooreMachine def) {
		super(panel, def);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			MooreMachine m = getDef();
			String output = (String) JOptionPane.showInputDialog(getPanel(),
					"Enter output:", "Set Output",
					JOptionPane.QUESTION_MESSAGE, null, null,
					JFLAPPreferences.getEmptyString());

			if (output == null
					|| output.equals(JFLAPPreferences.getEmptyString()))
				output = "";
			super.mousePressed(e);
			myFunction = ((MooreEditorPanel) getPanel()).addOutputFunction(getState(),
					Symbolizers.symbolize(output, m));
			mouseReleased(e);
		} else
			super.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
		State s = getState();
		if (SwingUtilities.isLeftMouseButton(e) && s != null) {
			MooreEditorPanel panel = (MooreEditorPanel) getPanel();
			MooreMachine m = getDef();

			panel.clearSelection();
			
			CompoundUndoRedo comp = new CompoundUndoRedo(new StateAddEvent(panel, m, s, e.getPoint()));
			comp.add(new OutputFunctionAddEvent());
			getKeeper().registerChange(comp);
			
			clearValues();
		} else
			super.mouseReleased(e);
	}
	
	private class OutputFunctionAddEvent extends AddEvent<MooreOutputFunction> {

		private State myState;
		private SymbolString mySymbols;

		public OutputFunctionAddEvent(){
			super(getDef().getOutputFunctionSet(), myFunction);
			myState = getState();
			mySymbols = new SymbolString(myFunction.getOutput());
		}
		
		@Override
		public boolean redo() {
			if(super.redo()){
				((MooreEditorPanel) getPanel()).addOutputFunction(myState, mySymbols);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean undo() {
			if(super.undo()){
				((MooreEditorPanel) getPanel()).removeOutputFunction(myState);
				return true;
			}
			return false;
		}
		
	}
}
