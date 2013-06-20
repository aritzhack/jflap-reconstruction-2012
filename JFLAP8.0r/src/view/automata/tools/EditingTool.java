package view.automata.tools;

import model.automata.Automaton;
import model.automata.Transition;
import view.automata.AutomatonEditorPanel;

public abstract class EditingTool<T extends Automaton<S>, S extends Transition<S>> extends Tool {

	private AutomatonEditorPanel<T, S> myPanel;

	public EditingTool(AutomatonEditorPanel<T, S> panel){
		this.myPanel = panel;
	}
	
	public AutomatonEditorPanel<T, S> getPanel(){
		return myPanel;
	}

}
