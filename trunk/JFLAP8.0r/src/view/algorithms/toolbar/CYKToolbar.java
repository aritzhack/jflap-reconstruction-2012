package view.algorithms.toolbar;

import model.algorithms.steppable.SteppableAlgorithm;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.grammar.CYKDoSelectedAction;
import view.action.grammar.parse.CYKAnimateAction;
import view.grammar.parsing.cyk.CYKParseTablePanel;

public class CYKToolbar extends SteppableToolbar {

	private MagnifiableButton mySelectedButton;
	private MagnifiableButton myAnimateButton;

	public CYKToolbar(CYKParseTablePanel panel, SteppableAlgorithm alg, boolean floatable) {
		super(alg, floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		mySelectedButton = new MagnifiableButton(new CYKDoSelectedAction(panel), size);
		myAnimateButton = new MagnifiableButton(new CYKAnimateAction(panel), size);
		add(mySelectedButton);
		add(myAnimateButton);
		updateButtons(alg);
	}
	
	@Override
	public void updateButtons(SteppableAlgorithm alg) {
		mySelectedButton.setEnabled(alg.canStep());
		myAnimateButton.setEnabled(alg.canStep());
		super.updateButtons(alg);
	}

}
