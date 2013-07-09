package view.automata.views;

import javax.swing.JComponent;

import view.automata.MealyEditorPanel;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.mealy.MealyMachine;
import model.undo.UndoKeeper;

public class MealyView extends AutomataView<MealyMachine, FSATransition>{

	public MealyView(MealyMachine model) {
		super(model);
	}
	
	@Override
	public JComponent createCentralPanel(MealyMachine model, UndoKeeper keeper,
			boolean editable) {
		// TODO Auto-generated method stub
		return new MealyEditorPanel(model, keeper, editable);
	}

}
