package view.algorithms.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.algorithms.ThreadedAlgorithm;

public class ResumeAction extends AbstractAction {

	private ThreadedAlgorithm myAlgorithm;

	public ResumeAction(ThreadedAlgorithm alg){
		super("Resume");
		myAlgorithm = alg;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myAlgorithm.resume();
	}

}
