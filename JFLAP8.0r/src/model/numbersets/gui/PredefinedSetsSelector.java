package model.numbersets.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.numbersets.controller.SetEnvironment;
import model.numbersets.defined.EvenSet;
import model.numbersets.defined.FibonacciSet;
import model.numbersets.defined.OddSet;
import model.numbersets.defined.PredefinedSet;
import model.numbersets.defined.PrimeSet;

@SuppressWarnings("serial")
public class PredefinedSetsSelector extends JPanel {
	
	// TODO REFACTOR

	static String[] sets = { 
			"Prime Numbers", 
			"Fibonacci",
			"Even Numbers", 
			"Odd Numbers", };

	static HashMap<String, PredefinedSet> map;
	
	private void initMap () {
		map = new HashMap<String, PredefinedSet>();
		map.put("Prime Numbers", new PrimeSet());
		map.put("Fibonacci", new FibonacciSet());
		map.put("Even Numbers", new EvenSet());
		map.put("Odd Numbers", new OddSet());
	}
	
	
	/**
	 * List of all predefined set options in a drop-down menu
	 */
	private JComboBox myDropdown;

	/**
	 * Click button to add set to active sets registry
	 */
	private JButton myAddButton;

	public PredefinedSetsSelector() {
		
		initMap();

		myDropdown = new JComboBox(sets);
		this.add(myDropdown);

		initAdd();
		this.add(myAddButton);
	}

	private void initAdd() {
		myAddButton = new JButton("ADD");
		myAddButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected = (String) myDropdown.getSelectedItem();
				if (selected != null) {

					PredefinedSet s = map.get(selected);
					SetEnvironment.getInstance().getActiveRegistry().add(s);
					
					System.out.println(SetEnvironment.getInstance().getActiveRegistry().getArray().length);
				}
			}

		});
	}

	private int promptForNumber() {
		String message = "Multiple of __ (enter positive integer):";
		String input = (String) JOptionPane.showInputDialog(message);
		
		int value = Integer.parseInt(input);

		return value;

	}

}
