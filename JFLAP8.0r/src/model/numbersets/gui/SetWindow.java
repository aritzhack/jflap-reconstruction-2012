package model.numbersets.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class SetWindow extends JFrame {
	
	private static SetWindow myWindow;
	
	public static SetWindow getInstance () {
		if (myWindow == null)
			myWindow = new SetWindow();
		return myWindow;
	}
	
	private ActiveSetDisplay myActiveSetDisplay;
	private SetOperationsPanel mySetOperations;
	private MembershipPanel myMembership;
	private SetCreationPanel mySetCreation;
	private SetPropertiesBox mySetProperties;
	private StepButton myStep;
	
	
	private SetWindow () {
		
		myActiveSetDisplay = new ActiveSetDisplay();
		mySetOperations = new SetOperationsPanel();
		myMembership = new MembershipPanel();
		mySetCreation = new SetCreationPanel();
		mySetProperties = new SetPropertiesBox();
		myStep = new StepButton();
		
		
		LayoutManager layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		this.setLayout(layout);
		
		c.gridx = 0;
		c.gridy = 0;
		this.add(myActiveSetDisplay, c);
		
		c.gridx = 0;
		c.gridy = 1;
		this.add(mySetOperations, c);
		
		
		c.gridx = 1;
		c.gridy = 0;
		this.add(mySetCreation, c);
	
		
		c.gridx = 1;
		c.gridy = 1;
		this.add(mySetProperties, c);
		
		c.gridx = 1;
		c.gridy = 2;
		this.add(myMembership, c);
	
		c.gridx = 0;
		c.gridy = 2;
		this.add(myStep, c);
		
		
		this.setPreferredSize(new Dimension(800, 600));
		this.setSize(getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	
	
	public ActiveSetDisplay getActiveSetDisplay () {
		return myActiveSetDisplay;
	}
}
