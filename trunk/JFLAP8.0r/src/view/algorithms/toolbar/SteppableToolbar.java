package view.algorithms.toolbar;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.steppable.SteppableAlgorithm;
import model.change.events.AdvancedChangeEvent;

import util.view.magnify.MagnifiableToolbar;
import view.action.StepAction;
import view.action.algorithm.AlgorithmCompleteAction;
import view.action.algorithm.AlgorithmResetAction;

public class SteppableToolbar extends MagnifiableToolbar implements ChangeListener{

	private JButton myStepButton;
	private JButton myCompleteButton;
	private JButton myResetButton;

	public SteppableToolbar(SteppableAlgorithm alg, boolean floatable){
		setFloatable(floatable);
		myStepButton = new JButton(new StepAction(alg));
		myCompleteButton = new JButton(new AlgorithmCompleteAction(alg));
		myResetButton = new JButton(new AlgorithmResetAction(alg));
		alg.addListener(this);
		this.add(myStepButton);
		this.add(myCompleteButton);
		this.add(myResetButton);
		updateButtons(alg);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof AdvancedChangeEvent)
			updateButtons((SteppableAlgorithm) e.getSource());
	}

	private void updateButtons(SteppableAlgorithm alg) {
		myStepButton.setEnabled(alg.canStep());
		myCompleteButton.setEnabled(alg.canStep());
		myResetButton.setEnabled(alg.isRunning());
	}
	
}
