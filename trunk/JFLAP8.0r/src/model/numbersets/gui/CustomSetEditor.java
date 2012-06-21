package model.numbersets.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.numbersets.controller.CustomSetController;

@SuppressWarnings("serial")
public class CustomSetEditor extends JPanel {
	
	/**
	 * Holds instance of a controller
	 */
	private CustomSetController myController;
	
	/**
	 * Text field where user sets or modifies name of the set
	 */
	private JTextField myNameField;

	/**
	 * Text field where the user inputs numbers to be added to a custom set
	 */
	private JTextField myInputArea;
	
	/**
	 * Display some of the elements presently in the set
	 */
	private JTextArea myDisplayArea;
	

	public CustomSetEditor() {
		this.setLayout(new GridLayout(4, 1));
		
		this.add(initName());
		this.add(addElements());
		
		myDisplayArea = new JTextArea();
		myDisplayArea.setEditable(false);
		this.add(myDisplayArea, BorderLayout.PAGE_END);
		
	}

	private JComponent addElements () {
		myInputArea = new JTextField("Enter elements separated by commas or spaces", 50);
		myInputArea.setToolTipText("Add integers separated by commas or spaces. " +
				"Entries that are not valid integers will be ignored.");
		
		myInputArea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = myInputArea.getText();
				myDisplayArea.setText(input);
			}
			
		});
		
		return myInputArea;
	}
	
	
	private JComponent initName () {
		myNameField = new JTextField("Name", 25);
		myNameField.setEditable(true);
		
		return myNameField;
	}
	

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.getContentPane().add(new CustomSetEditor());

		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

}
