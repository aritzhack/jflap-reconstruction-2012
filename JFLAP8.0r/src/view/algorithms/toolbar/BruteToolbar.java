package view.algorithms.toolbar;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.grammar.PauseAction;
import view.action.grammar.ResumeAction;

public class BruteToolbar extends SteppableToolbar {

	private MagnifiableButton myPauseButton;
	private MagnifiableButton myResumeButton;

	public BruteToolbar(UnrestrictedBruteParser parser, boolean floatable) {
		super(parser, floatable);
	} 
	
	@Override
	public void updateButtons(SteppableAlgorithm alg) {
		UnrestrictedBruteParser parser = (UnrestrictedBruteParser) alg;
		if( myPauseButton == null || myResumeButton == null){
			initializeButtons(parser);
		}
		myPauseButton.setEnabled(alg.canStep());
		myResumeButton.setEnabled(parser.isPaused());
		super.updateButtons(parser);
	}

	private void initializeButtons(UnrestrictedBruteParser parser) {
		int size = JFLAPPreferences.getDefaultTextSize();
		myPauseButton = new MagnifiableButton(new PauseAction(parser), size);
		myResumeButton = new MagnifiableButton(new ResumeAction(parser), size);
		add(myPauseButton);
		add(myResumeButton);
	}

}
