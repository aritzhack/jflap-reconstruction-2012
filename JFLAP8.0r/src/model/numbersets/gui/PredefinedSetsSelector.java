package model.numbersets.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PredefinedSetsSelector extends JPanel {
	
	static String[] sets = {
		"Prime Numbers",
		"Fibonacci",
		"Multiples of ",
		"Even Numbers",
		"Odd Numbers",
	};
	
	
	/**
	 * List of all predefined set options in a drop-down menu
	 */
	private JComboBox myDropdown;
	
	public PredefinedSetsSelector () {
	
		myDropdown = new JComboBox(sets);
		myDropdown.addActionListener(new SetActionListener());
		
		this.add(myDropdown);
	}
	
	private class SetActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	

}
