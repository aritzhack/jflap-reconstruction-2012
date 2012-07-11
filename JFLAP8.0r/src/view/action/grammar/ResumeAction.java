package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;

public class ResumeAction extends AbstractAction {

	private UnrestrictedBruteParser myParser;

	public ResumeAction(UnrestrictedBruteParser parser){
		super("Resume");
		myParser = parser;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		myParser.setPaused(false);
	}

}
