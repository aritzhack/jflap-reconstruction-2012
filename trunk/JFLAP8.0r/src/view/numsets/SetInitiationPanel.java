package view.numsets;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.numbersets.control.*;

/**
 * Panel holds drop-down menu of predefined set option
 * and button to allow user to create a new finite set
 * 
 * @author peggyli
 *
 */

@SuppressWarnings("serial")
public class SetInitiationPanel extends JPanel {
	
	private static Object[] OPTIONS;
	
	static {
		Class[] classes = Loader.getLoadedClasses();
		OPTIONS = new String[classes.length];
		for (int i = 0; i < classes.length; i++){
			OPTIONS[i] = classes[i].getSimpleName();
		}
		
	};

	public SetInitiationPanel () {
	
		this.setLayout(new BorderLayout());
		this.add(new PredefinedSetDropdown(OPTIONS), BorderLayout.NORTH);
		
		this.add(new JButton("Build new set"), BorderLayout.SOUTH);
		
		
	}
}
