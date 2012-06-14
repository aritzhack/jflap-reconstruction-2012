package oldnewstuff.action.grammar;


import java.awt.event.ActionEvent;

import jflap.actions.undo.UndoableAction;
import jflap.errors.BooleanWrapper;
import jflap.model.formaldef.symbols.SymbolString;
import jflap.model.grammar.Production;




public class ModifyProductionAction extends UndoableAction {

	private Production myProduction;
	private SymbolString newLHS, newRHS, oldLHS, oldRHS;

	public ModifyProductionAction(Production production, SymbolString lhs,
			SymbolString rhs) {
		super("Modify Production");
		
		myProduction = production;
		newLHS = lhs;
		newRHS = rhs;
	}

	@Override
	protected boolean setFields(ActionEvent e) {
		oldLHS = myProduction.getLHS().clone();
		oldRHS = myProduction.getRHS().clone();
		return true;
	}

	@Override
	protected BooleanWrapper doUndo() {
		return setProduction(oldLHS, oldRHS);
	}

	private BooleanWrapper setProduction(SymbolString lhs, SymbolString rhs) {
		myProduction.setLHS(lhs);
		myProduction.setRHS(rhs);
		return new BooleanWrapper(true);
	}

	@Override
	protected BooleanWrapper doRedo() {
		return setProduction(newLHS, newRHS);
	}

}
