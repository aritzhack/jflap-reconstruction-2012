package model.numbersets.gui;

/**
 * @author peggyli
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import model.numbersets.defined.FibonacciSet;
import model.numbersets.defined.PredefinedSet;
import model.numbersets.defined.PrimeSet;

@SuppressWarnings("serial")
public class SetPreviewPane extends JPanel {
	
	
	static Map<String, PredefinedSet> setTypes;
	
	static void initMap () {
		setTypes = new HashMap<String, PredefinedSet>();
		setTypes.put("Select a predefined set", null);
		setTypes.put("Fibonacci", new FibonacciSet());
		setTypes.put("Prime Numbers", new PrimeSet());
	}
	
	/**
	 * Drop-down menu of predefined sets user can choose from
	 */
	private JComboBox mySelections;
	
	
	/**
	 * Text box where elements of the set are displayed
	 */
	private JTextArea myDescriptionBox;
	
	
	private JTextArea mySetDisplayBox;
	
	
	public SetPreviewPane () {
		super(new GridLayout(5, 1));
		
		initMap();
		
		
		mySelections = new JComboBox(setTypes.keySet().toArray());
		mySelections.setSize(100, 20);
		mySelections.addActionListener(new ActionListener() {	
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected = (String) mySelections.getSelectedItem();
				if (selected == null)	return;
				
				PredefinedSet set = setTypes.get(selected);
				myDescriptionBox.setText(set.getDescription());
				
				mySetDisplayBox.setText(set.getSet().toString());
			}
		});
		
		myDescriptionBox = new JTextArea();
//		myDescriptionBox.setPreferredSize(new Dimension(400, 200));
		myDescriptionBox.setLineWrap(true);
		myDescriptionBox.setWrapStyleWord(true);
		
		mySetDisplayBox = new JTextArea();
		mySetDisplayBox.setLineWrap(true);
		mySetDisplayBox.setBackground(Color.LIGHT_GRAY);
		
		JScrollPane scroll = new JScrollPane(mySetDisplayBox);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(mySelections);
		this.add(myDescriptionBox);
		this.add(scroll);
		
	
	}
	
	
}
