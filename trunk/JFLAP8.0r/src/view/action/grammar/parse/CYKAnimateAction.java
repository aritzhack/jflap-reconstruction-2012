package view.action.grammar.parse;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.grammar.parsing.cyk.CYKParseTablePanel;

public class CYKAnimateAction extends AbstractAction {

	private CYKParseTablePanel myPanel;
	
	public CYKAnimateAction(CYKParseTablePanel panel){
		super("Animate Selected");
		myPanel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myPanel.animate();
	}

}
