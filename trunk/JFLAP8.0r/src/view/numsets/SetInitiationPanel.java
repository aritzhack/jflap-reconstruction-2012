package view.numsets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;

import model.numbersets.control.SetsManager;
import model.numbersets.defined.PredefinedSet;

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
		Class[] classes = SetsManager.PREDEFINED_SETS_CLASSES;
		Set<String> uniqueNames = new HashSet<String>();
		for (Class c : classes) {
			try {
				uniqueNames.add((PredefinedSet.class).cast(c.newInstance()).getName());
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
		}
		
		OPTIONS = new String[uniqueNames.size()];
		Iterator<String> iter = uniqueNames.iterator();
		int index = 0;
		while (iter.hasNext()) {
			OPTIONS[index] = iter.next();
			index++;
		}
		
	};

	public SetInitiationPanel () {
	
		setLayout(new BorderLayout());
		add(new PredefinedSetDropdown(OPTIONS), BorderLayout.NORTH);
		
		JButton newSetButton = new JButton("Build new");
		newSetButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				SetEditor editor = new SetEditor();
				JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
				environ.addSelectedComponent(editor);
			}
			
			
		});
		add(newSetButton, BorderLayout.SOUTH);
		
		
		
	}
	
	/**
	 * Confirms addition of the selected set and adds it to the active sets list
	 */
	private JButton myAddButton;
}
