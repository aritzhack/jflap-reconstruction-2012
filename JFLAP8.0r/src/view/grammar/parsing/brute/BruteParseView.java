package view.grammar.parsing.brute;

import javax.swing.JOptionPane;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.change.events.AdvancedChangeEvent;
import view.algorithms.toolbar.BruteToolbar;
import view.algorithms.toolbar.SteppableToolbar;
import view.grammar.parsing.FindFirstParserView;

/**
 * This is a brute force parse pane.
 * 
 * @author Thomas Finley
 */
public class BruteParseView extends FindFirstParserView<BruteRunningView> {
	private static final int MAX_REACHED = 2;

	public BruteParseView(UnrestrictedBruteParser alg) {
		super(alg);
		
	}
	
	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		if(e.comesFrom(getAlgorithm()) && e.getType() == MAX_REACHED){
			createNodeWarning(e);
		}
		super.updateStatus(e);
	}

	private void createNodeWarning(AdvancedChangeEvent e) {
		UnrestrictedBruteParser parser = (UnrestrictedBruteParser) getAlgorithm();
		
		int max = (Integer) e.getArg(0);
		String[] options = {"Increase limit to "+max*5, "Stop"};
		int n = JOptionPane.showOptionDialog(this, "The number of nodes generated is at "+
				parser.getNumberOfNodes()+" ." + " Would you like to continue?", "Node Warning", 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
				null, options, options[0]);
		if(n==JOptionPane.YES_OPTION){
			parser.raiseCapacity();
			parser.setPaused(false);
		} else{
			parser.reset();
		}
	}
	

	@Override
	public BruteRunningView createRunningView(Parser alg) {
		BruteRunningView running = new BruteRunningView((UnrestrictedBruteParser) alg);
		return running;
	}


	@Override
	public SteppableToolbar createToolbar(Parser alg) {
		BruteToolbar toolbar = new BruteToolbar((UnrestrictedBruteParser) alg, false);
		return toolbar;
	}
}
