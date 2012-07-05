package view.numsets;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.numbersets.AbstractNumberSet;

@SuppressWarnings("serial")
public class InformationPanel extends JPanel {

	private AbstractNumberSet mySet;
	
	public InformationPanel (AbstractNumberSet set) {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	}
	
	
	private Component createName () {
		JTextField name = new JTextField(mySet.getName());

		return name;
		
	}
	
	private Component createDescription () {
		String des = mySet.getDescription() != null ? mySet.getDescription() : "No description provided";
		JTextArea description = new JTextArea(des);
		
		return description;
		
	}
	
	
}
