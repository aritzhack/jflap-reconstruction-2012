package view.grammar.parsing.cyk;

import java.awt.BorderLayout;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import view.grammar.parsing.RunningView;

public class NewCYKParseTablePanel extends RunningView {

	private CYKParseModel myModel;

	public NewCYKParseTablePanel(Parser parser) {
		super("CYK Parse Table", parser);
		setLayout(new BorderLayout());
		
		myModel = new CYKParseModel((CYKParser) parser);
	}

	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		// TODO Auto-generated method stub

	}

}
