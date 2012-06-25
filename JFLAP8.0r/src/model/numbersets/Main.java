package model.numbersets;

import java.awt.GridLayout;

import javax.swing.JFrame;

import model.numbersets.gui.ActiveSetDisplay;
import model.numbersets.gui.SetCreationPanel;
import model.numbersets.gui.SetOperationsPanel;
import model.numbersets.gui.SetPropertiesBox;

public class Main {

	public static void print(Object o) {
		System.out.println(o);
	}

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		
		frame.setLayout(new GridLayout(1, 1));
		
		frame.add(new ActiveSetDisplay());
		frame.add(new SetOperationsPanel());
		
		frame.add(new SetCreationPanel());
		frame.add(new SetPropertiesBox());
		
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);


	}

}
