package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;

public class PauseUnpauseAction extends AbstractAction {

	private UnrestrictedBruteParser myParser;
	private boolean pause;

	public PauseUnpauseAction(UnrestrictedBruteParser parser, boolean pause){
		super(pause ? "Pause" : "Unpause");
		myParser = parser;
		this.pause = pause;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myParser.setPaused(pause);
	}

}
