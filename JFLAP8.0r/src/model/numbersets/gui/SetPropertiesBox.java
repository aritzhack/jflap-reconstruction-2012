package model.numbersets.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class SetPropertiesBox extends JPanel {
	
	private JTextArea myProperties;
	
	public SetPropertiesBox () {
		this.setName("Properties");
		
		myProperties = new JTextArea();
		
		this.add(init());
		this.setPreferredSize(new Dimension(500, 500));
		
		setVisible(true);
		
	}
	
	private JComponent init () {
		JScrollPane scroll = new JScrollPane();
		scroll.add(myProperties);
		
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return scroll;
	}
	
	
	public void updateText () {
		
	}

}
