package model.numbersets.gui;

/**
 * @author peggyli
 */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SetCreationPanel extends JPanel {
	
	/**
	 * Launches a new {@link CustomSetEditor}
	 */
	private JButton myCustomSetButton;
	
	/**
	 * Allows user to select a predefined set from a drop-down
	 */
	private JPanel myPredefinedSetMenu; 
	
	
	public SetCreationPanel(){
		
		this.setLayout(new GridLayout(2, 1));
		
		initCustomSetButton();
		initPredefinedSetMenu();
		
		this.add(myCustomSetButton);
		this.add(myPredefinedSetMenu);
	}
	
	
	private void initCustomSetButton() {
		myCustomSetButton = new JButton("Custom Set Builder");
	
		myCustomSetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
				frame.add(new CustomSetEditor());
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				
			}
			
		});
	}
	
	private void initPredefinedSetMenu() {
		myPredefinedSetMenu = new PredefinedSetsSelector();
		
	}
	
	
	
}
