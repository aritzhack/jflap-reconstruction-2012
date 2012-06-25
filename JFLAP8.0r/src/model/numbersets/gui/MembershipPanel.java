package model.numbersets.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * User inputs a number to see whether or not it belongs in the set
 * 
 * @author peggyli
 *
 */

@SuppressWarnings("serial")
public class MembershipPanel extends JPanel {
	
	private JTextField myInputBox;
	
	private JTextField myAnswer;
	
	public MembershipPanel() {
	
		myInputBox = new JTextField(10);
		
		myAnswer = new JTextField(20);
		myAnswer.setEditable(false);
		
		this.setLayout(new GridLayout(0, 2));
		this.add(myInputBox);
		this.add(myAnswer);
		
	}
	
	
	
	
	

}
