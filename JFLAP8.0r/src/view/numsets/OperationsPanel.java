package view.numsets;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import view.numsets.actions.ActionButton;

@SuppressWarnings("serial")
public class OperationsPanel extends JPanel {

	public OperationsPanel () {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		ActionButton union = new ActionButton("Union");
		
		
		ActionButton intersection = new ActionButton("Intersection");
		
		
		ActionButton powerset = new ActionButton("Powerset");
		

		this.add(union);
		this.add(intersection);
		this.add(powerset);

	}

	
	
	
	public String[] getAllActions () {
		Component[] comps = this.getComponents();
		String[] actions = new String[comps.length];
		for (int i = 0; i < comps.length; i++) {
			actions[i] = comps[i].getName();
		}
		return actions;
	}
	
}
