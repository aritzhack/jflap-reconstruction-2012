package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;

public class PauseAction extends AbstractAction {

	private UnrestrictedBruteParser myParser;

	public PauseAction(UnrestrictedBruteParser parser){
		super("Pause");
		myParser = parser;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myParser.setPaused(true);
	}

}
