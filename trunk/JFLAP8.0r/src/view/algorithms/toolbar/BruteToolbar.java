package view.algorithms.toolbar;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.grammar.PauseAction;
import view.algorithms.ThreadedAlgorithm;

public class BruteToolbar extends SteppableToolbar {

	private MagnifiableButton myPauseButton;
	private MagnifiableButton myResumeButton;

	
	public BruteToolbar(UnrestrictedBruteParser parser, boolean floatable){
		this(new ThreadedAlgorithm<UnrestrictedBruteParser>(parser), floatable);
	}
	
	private BruteToolbar(ThreadedAlgorithm alg,
			boolean floatable) {
		super(alg, floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		myPauseButton = new MagnifiableButton(new PauseAction(alg), size);
		myResumeButton = new MagnifiableButton(new ResumeAction(alg), size);
		add(myPauseButton);
		add(myResumeButton);
	}

	@Override
	public void updateButtons(SteppableAlgorithm alg) {
		if (alg instanceof ThreadedAlgorithm){
			ThreadedAlgorithm parser = (ThreadedAlgorithm) alg;
			myPauseButton.setEnabled(!parser.isPaused());
			myResumeButton.setEnabled(parser.isPaused());
		}
		else super.updateButtons(alg);
	}

}
