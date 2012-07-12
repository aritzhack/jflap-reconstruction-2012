package view.algorithms.toolbar;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.cyk.CYKParser;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.grammar.DoSelectedAction;
import view.grammar.parsing.cyk.CYKParseTablePanel;

public class CYKToolbar extends SteppableToolbar {

	private MagnifiableButton mySelectedButton;

	public CYKToolbar(CYKParseTablePanel panel, SteppableAlgorithm alg, boolean floatable) {
		super(alg, floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		mySelectedButton = new MagnifiableButton(new DoSelectedAction((CYKParser) alg, panel), size);
		add(mySelectedButton);
	}
	
	@Override
	public void updateButtons(SteppableAlgorithm alg) {
		mySelectedButton.setEnabled(alg.canStep());
		super.updateButtons(alg);
	}

}
