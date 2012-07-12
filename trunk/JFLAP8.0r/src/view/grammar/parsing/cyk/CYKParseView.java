package view.grammar.parsing.cyk;

import model.algorithms.testinput.parse.Parser;
import util.view.magnify.MagnifiableToolbar;
import view.algorithms.toolbar.CYKToolbar;
import view.grammar.parsing.FindFirstParserView;

public class CYKParseView extends FindFirstParserView <CYKParseTablePanel>{

	public CYKParseView(Parser alg) {
		super(alg);
	}

	@Override
	public CYKParseTablePanel createRunningView(Parser alg) {
		CYKParseTablePanel table = new CYKParseTablePanel(alg);
		return table;
	}
	
	@Override
	public MagnifiableToolbar createToolbar(Parser alg) {
		
		return new CYKToolbar(getRunningView(), alg, false);
	}

}
