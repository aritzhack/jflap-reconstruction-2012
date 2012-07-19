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
		super.updateStatus(e);
		
		Parser alg = getAlgorithm();
		if(!alg.isDone()){
			if(alg.getInput() == null){
				setStatus(ParserView.SET_INPUT);
				return;
			}
			setStatus("Fill in the next row of the parse table! Fill in a space for empty sets.");
		}
	}

}
