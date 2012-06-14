package oldnewstuff.action.grammar;

import jflap.errors.BooleanWrapper;
import jflap.model.grammar.Production;


public class RemoveProductionAction extends AddProductionAction
{

	public RemoveProductionAction(Production p) {
		super(p);
		this.putValue(NAME, "Remove Production");
	}

	@Override
	protected BooleanWrapper doUndo() {
		return super.doRedo();
	}

	@Override
	protected BooleanWrapper doRedo() {
		return super.doUndo();
	}
	
	

}
