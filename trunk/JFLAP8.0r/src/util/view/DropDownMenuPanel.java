package util.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import debug.JFLAPDebug;

import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiablePanel;

public class DropDownMenuPanel extends MagnifiablePanel implements ItemListener {

	private JComboBox myComboBox;
	private MagnifiablePanel myPanel;

	public DropDownMenuPanel(Component ... options){
		//set up primary panel
		myPanel = new MagnifiablePanel(new CardLayout());
		
		//set up combo box
		myComboBox = new JComboBox();
		myComboBox.addItemListener(this);
		
		//init choices
		for (Component c: options){
			addOption(c);
		}
		
		this.setLayout(new BorderLayout());
		this.add(myComboBox, BorderLayout.NORTH);
		this.add(myPanel, BorderLayout.CENTER);		
	}

	public void addOption(Component c){
		myComboBox.addItem(c.getName());
		myPanel.add(c, c.getName());
		
	}

	private void setSelectedComponent(String item) {
		CardLayout cl = (CardLayout)(myPanel.getLayout());
        cl.show(myPanel, item);
	}

	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		myComboBox.setFont(myComboBox.getFont().deriveFont(size));
		myPanel.setMagnification(mag);
	}


	@Override
	public void itemStateChanged(ItemEvent evt) {
		setSelectedComponent((String)evt.getItem());
	}
	
}
