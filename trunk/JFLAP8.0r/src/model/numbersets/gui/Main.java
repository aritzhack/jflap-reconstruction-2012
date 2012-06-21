package model.numbersets.gui;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main {


	public static void main (String[] args) {
		
		JComponent comp = new SetCreationPanel();
		
		JFrame frame = new JFrame("Sets");
		frame.add(comp);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

}
