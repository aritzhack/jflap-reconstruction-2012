package view.action.newactions;

import view.pumping.PumpingLemmaChooser;
import view.pumping.RegPumpingLemmaChooser;

public class NewRegPumpingLemmaAction extends NewAction<PumpingLemmaChooser>{

	public NewRegPumpingLemmaAction() {
		super("Regular Pumping Lemma");
	}

	@Override
	public PumpingLemmaChooser createNewModel() {
		return new RegPumpingLemmaChooser();
	}

}
