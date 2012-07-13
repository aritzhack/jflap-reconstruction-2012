package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.grammar.parsing.cyk.CYKParseTablePanel;

import model.algorithms.testinput.parse.cyk.CYKParser;

public class CYKDoSelectedAction extends AbstractAction {

	private CYKParser myParser;
	private CYKParseTablePanel myPanel;

	public CYKDoSelectedAction(CYKParser parser, CYKParseTablePanel panel){
		super("Do Selected");
		myParser = parser;
		myPanel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int row = myPanel.getSelectedRow();
		int col = myPanel.getSelectedColumn();
		myParser.doSelected(row, col);
	}

}
