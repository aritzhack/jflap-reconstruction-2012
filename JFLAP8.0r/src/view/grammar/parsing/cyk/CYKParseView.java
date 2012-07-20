package view.grammar.parsing.cyk;

import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import util.view.magnify.MagnifiableToolbar;
import view.algorithms.toolbar.CYKToolbar;
import view.grammar.parsing.FindFirstParserView;
import view.grammar.parsing.ParserView;

/**
 * CYK Parser GUI
 * @author Ian McMahon
 *
 */
public class CYKParseView extends FindFirstParserView <CYKParseTablePanel>{

	public CYKParseView(Parser alg) {
		super(alg);
	}

	@Override
	public CYKParseTablePanel createRunningView(Parser alg) {
		CYKParseTablePanel table = new CYKParseTablePanel(alg, true);
		return table;
	}
	
	@Override
	public MagnifiableToolbar createToolbar(Parser alg) {
		return new CYKToolbar(getRunningView(), alg, false);
	}
	
	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		Parser alg = getAlgorithm();
		if(!alg.isDone()){
			setStatus("Fill in the next row of the parse table! Enter a space for empty sets.");
		}
		super.updateStatus(e);
		
	}

}
