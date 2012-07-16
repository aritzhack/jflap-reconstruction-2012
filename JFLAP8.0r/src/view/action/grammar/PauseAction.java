package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.algorithms.ThreadedAlgorithm;

import model.algorithms.testinput.parse.brute.RestrictedBruteParser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;

public class PauseAction extends AbstractAction {

	private ThreadedAlgorithm myAlgorithm;

	public PauseAction(ThreadedAlgorithm alg){
		super("Pause");
		myAlgorithm = alg;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myAlgorithm.pause();
	}

}
