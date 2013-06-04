package view.action.newactions;

import model.lsystem.LSystem;

public class NewLSystemAction extends NewAction<LSystem>{

	public NewLSystemAction() {
		super("L-System");
	}

	@Override
	public LSystem createNewModel() {
		return new LSystem();
	}

}
