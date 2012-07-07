package model.numbersets.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.numbersets.controller.SteppableController;

@SuppressWarnings("serial")
public class StepButton extends JButton {

	private SteppableController myController;
	
	public StepButton () {
		super("Step");
		
		this.setToolTipText("Show next element in the set");
		this.addActionListener(new StepActionListener());
		
		myController = new SteppableController(null);
	}
	
	
	private class StepActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int next = myController.step();
		}
		
	}
}
