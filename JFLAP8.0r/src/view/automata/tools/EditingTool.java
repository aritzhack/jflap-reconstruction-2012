package view.automata.tools;

import model.automata.Automaton;
import model.automata.Transition;
import model.undo.UndoKeeper;
import view.automata.AutomatonEditorPanel;

/**Superclass for all Tools that require access to the AutomatonEditorPanel that is using them.
 * @author Ian McMahon
 */
public abstract class EditingTool<T extends Automaton<S>, S extends Transition<S>> extends Tool {

	private AutomatonEditorPanel<T, S> myPanel;
	private UndoKeeper myKeeper;

	public EditingTool(AutomatonEditorPanel<T, S> panel){
		this.myPanel = panel;
		myKeeper = panel.getKeeper();
	}
	
	public AutomatonEditorPanel<T, S> getPanel(){
		return myPanel;
	}

	public UndoKeeper getKeeper() {
		return myKeeper;
	}
}
