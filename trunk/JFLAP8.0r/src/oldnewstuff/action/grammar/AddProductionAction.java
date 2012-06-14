package oldnewstuff.action.grammar;


import java.awt.event.ActionEvent;

import jflap.actions.undo.UndoableAction;
import jflap.errors.BooleanWrapper;
import jflap.main.universe.UniverseHelper;
import jflap.model.grammar.Production;




public class AddProductionAction extends UndoableAction {

	private Production myProduction;
	private int myIndex;

	public AddProductionAction(Production p) {
		this(p, UniverseHelper.getActiveGrammar().getNumProductions());
	}

	public AddProductionAction(Production p, int index) {
		super("Add Production");
		myProduction = p;
		myIndex = index;
	}

	@Override
	protected boolean setFields(ActionEvent e) {
		//donothing
		return true;
	}

	@Override
	protected BooleanWrapper doUndo() {
		return UniverseHelper.getActiveGrammar().removeProduction(myProduction);
	}

	@Override
	protected BooleanWrapper doRedo() {
		return UniverseHelper.getActiveGrammar().addProduction(myIndex, myProduction);
	}

}


