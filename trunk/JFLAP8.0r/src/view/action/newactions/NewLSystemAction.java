package view.action.newactions;

import model.lsystem.NLSystem;

public class NewLSystemAction extends NewAction<NLSystem>{

	public NewLSystemAction() {
		super("L-System");
	}

	@Override
	public NLSystem createNewModel() {
		return new NLSystem();
	}

}
