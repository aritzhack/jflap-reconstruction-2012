package view.grammar.parsing.brute;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.change.events.AdvancedChangeEvent;
import view.grammar.parsing.RunningView;

public class BruteRunningView extends RunningView {

	public BruteRunningView(UnrestrictedBruteParser parser) {
		super("Brute Force Parser", parser);
		// TODO Auto-generated constructor stub
	}

}
