package view.numsets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.numbersets.AbstractNumberSet;
import model.numbersets.control.SetsManager;

public abstract class AddSet extends JButton {

	public AddSet () {
		
		super("Add");
		setEnabled(false);
		
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SetsManager.ACTIVE_REGISTRY.add(getSetToAdd());
			}			
		});
	}

	
	public abstract AbstractNumberSet getSetToAdd();
	
	
	public void setEnabled (boolean bool) {
		this.setEnabled(bool);
	}
}
