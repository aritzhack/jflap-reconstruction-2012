package model.numbersets.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.numbersets.AbstractNumberSet;
import model.numbersets.controller.OperationsController;
import model.numbersets.operations.SetOperation;
import model.numbersets.operations.Union;

@SuppressWarnings("serial")
public class SetOperationsPanel extends JPanel {

	public SetOperationsPanel () {

		initButtons();
	}
	
	
	private void initButtons () {
		JButton button;
		
		button = new JButton("Union");
		this.add(button);
		
		button = new JButton("Intersection");
		this.add(button);
		
		
		button = new JButton("Powerset");
		this.add(button);
		
		button = new JButton("Difference");
		this.add(button);
		
	}
	
	

	private class SetOpsActionListener implements ActionListener {

		private SetOperation myOperation;
		
		public SetOpsActionListener (SetOperation op) {
			myOperation = op;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Set<AbstractNumberSet> operands = null;
			OperationsController controller = new OperationsController(myOperation, operands);
		}
		
	}
	
	
	public static void main (String[] args) {
		
		JFrame frame = new JFrame("Operations");
		frame.add(new SetOperationsPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
