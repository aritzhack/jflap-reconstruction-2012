package view.sets;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

import universe.preferences.JFLAPPreferences;
import util.view.magnify.*;

public class TextFields {
	
	public static JComponent makePanel(String title, JTextComponent field) {
		MagnifiableToolbar panel = new MagnifiableToolbar();
		panel.setFloatable(false);
		panel.add(new MagnifiableLabel(title, JFLAPPreferences.getDefaultTextSize()));
		panel.add(field);
		return panel;
	}
	
	public static void main (String[] args) {
		JFrame frame = new JFrame("Test");
		
//		frame.setLayout(new GridLayout(0, 1));
		frame.setLayout(new BorderLayout());
		frame.add(makePanel("Name", new MagnifiableTextField(50)));
		
		
//		frame.add(new MagnifiableTextField(50));
//		frame.add(new MagnifiableTextField(50));
		frame.pack();
		frame.setVisible(true);
	}

}

