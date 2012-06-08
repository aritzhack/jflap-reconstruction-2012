package file.xml.formaldef.components.specific.states;

import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;

public class FinalStateSetTransducer extends StateSetTransducer {

	private static final String FINAL_STATESET_TAG = "final_states";

	@Override
	public StateSet createEmptyComponent() {
		return new FinalStateSet();
	}

	@Override
	public String getTag() {
		return FINAL_STATESET_TAG;
	}

	
}
