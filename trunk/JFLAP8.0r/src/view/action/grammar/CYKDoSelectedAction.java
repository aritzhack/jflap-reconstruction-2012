package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.grammar.parsing.cyk.CYKParseTablePanel;

public class CYKDoSelectedAction extends AbstractAction {

	private CYKParseTablePanel myPanel;

	public CYKDoSelectedAction(CYKParseTablePanel panel){
		super("Do Selected");
		myPanel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		myPanel.doSelected();
	}

}
