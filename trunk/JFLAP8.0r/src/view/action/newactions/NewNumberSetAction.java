package view.action.newactions;

import model.numbersets.control.*;

public class NewNumberSetAction extends NewAction<SetsManager> {

	public NewNumberSetAction () {
		super("Number Sets");
	}

	@Override
	public SetsManager createNewModel() {
		// TODO Auto-generated method stub
		return new SetsManager();
	}

}
