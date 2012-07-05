package view.numsets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;

import model.numbersets.control.Loader;
import model.numbersets.control.SetsManager;
import model.numbersets.controller.PredefinedSetController;
import model.numbersets.defined.PredefinedSet;

@SuppressWarnings({ "serial", "rawtypes" })
public class PredefinedSetDropdown extends JComboBox {
	
	public PredefinedSetDropdown (Object[] options) {
		super(options);
		this.setEditable(false);
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PredefinedSetDropdown source = (PredefinedSetDropdown) arg0.getSource();
				
				String selection = source.getSelected();
				try {
					PredefinedSet set = (PredefinedSet) predefinedNameMap.get(selection).newInstance();
					PredefinedSetController controller = new PredefinedSetController(set);
					SetsManager.ACTIVE_REGISTRY.add(set);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}					
			}
		});
	}
	
	
	public String getSelected () {
		return (String) this.getSelectedItem();
	}
	
	
	private static HashMap<String, Class> predefinedNameMap;
	
	static {
		predefinedNameMap = new HashMap<String, Class>();
		Class[] classes = Loader.getLoadedClasses();
		for (Class c : classes) {
			try {
				String str = (PredefinedSet.class).cast(c.newInstance()).getName();
				predefinedNameMap.put(str, c);
			} catch (InstantiationException e) {
//				e.printStackTrace();
			} catch (IllegalAccessException e) {
//				e.printStackTrace();
			}
		}
	}
	
	
}
