package view.algorithms.toolbar;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.grammar.PauseUnpauseAction;

public class BruteToolbar extends SteppableToolbar {

	private MagnifiableButton myPauseButton;
	private MagnifiableButton myResumeButton;

	public BruteToolbar(UnrestrictedBruteParser parser, boolean floatable) {
		super(parser, floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		myPauseButton = new MagnifiableButton(new PauseUnpauseAction(parser, true), size);
		myResumeButton = new MagnifiableButton(new PauseUnpauseAction(parser, false), size);
		add(myPauseButton);
		add(myResumeButton);
		updateButtons(parser);
	} 
	
	@Override
	public void updateButtons(SteppableAlgorithm alg) {
		UnrestrictedBruteParser parser = (UnrestrictedBruteParser) alg;
		myPauseButton.setEnabled(alg.canStep());
		myResumeButton.setEnabled(parser.isPaused());
		super.updateButtons(parser);
	}

}
