package model.numbersets.gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SetOperationsPanel extends JPanel {

	public SetOperationsPanel () {
		this.setPreferredSize(new Dimension(100, 50));
		
		init();
	}
	
	
	private void init () {
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
	
	
	public static void main (String[] args) {
		
		JFrame frame = new JFrame("Operations");
		frame.add(new SetOperationsPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
