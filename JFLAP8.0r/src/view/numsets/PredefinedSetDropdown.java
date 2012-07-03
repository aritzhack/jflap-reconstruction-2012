package view.numsets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import model.numbersets.controller.PredefinedSetController;

@SuppressWarnings("serial")
public class PredefinedSetDropdown extends JComboBox {
	
	public PredefinedSetDropdown (Object[] options) {
		super(options);
		this.setEditable(false);
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PredefinedSetDropdown source = (PredefinedSetDropdown) arg0.getSource();
				
				PredefinedSetController controller = new PredefinedSetController(null);
				System.out.println(source.getSelected());
			}
		});
	}
	
	
	public String getSelected () {
		return (String) this.getSelectedItem();
	}
	
	
}
