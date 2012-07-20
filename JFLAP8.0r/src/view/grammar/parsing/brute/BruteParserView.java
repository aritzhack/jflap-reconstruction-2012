package view.grammar.parsing.brute;

import javax.swing.JOptionPane;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.change.events.AdvancedChangeEvent;
import view.algorithms.toolbar.SteppableToolbar;
import view.grammar.parsing.FindFirstParserView;

/**
 * This is a brute force parse pane.
 * 
 * @author Ian McMahon
 */
public class BruteParserView extends FindFirstParserView<BruteParseTablePanel> {

	public BruteParserView(UnrestrictedBruteParser alg) {
		super(alg);

	}

	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		UnrestrictedBruteParser parser = (UnrestrictedBruteParser) getAlgorithm();
		if (e.comesFrom(parser)) {
			setStatus("Press one of the buttons to continue, restart, or choose a new input.");
			if (e.getType() == UnrestrictedBruteParser.MAX_REACHED) {
				boolean shouldInc = promptForIncreaseCapacity(
						parser.getNumberOfNodes(), e);
				if (shouldInc)
					parser.raiseCapacity(5);
			}

		}
		super.updateStatus(e);
	}

	private boolean promptForIncreaseCapacity(int nodeNum, AdvancedChangeEvent e) {

		int n = JOptionPane
				.showConfirmDialog(
						this,
						"Warning: The parser may slow down drastically and cause JFLAP to freeze if you continue."
								+ "\nNumber of nodes generated: "
								+ nodeNum
								+ " Would you like to continue?",
						"Node Warning", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

		return n == JOptionPane.YES_OPTION;
	}

	@Override
	public BruteParseTablePanel createRunningView(Parser alg) {
		BruteParseTablePanel running = new BruteParseTablePanel(
				(UnrestrictedBruteParser) alg);
		return running;
	}

	@Override
	public SteppableToolbar createToolbar(Parser alg) {
		return new SteppableToolbar(alg, false);
	}
}
