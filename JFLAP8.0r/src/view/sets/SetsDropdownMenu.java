package view.sets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComboBox;

import model.sets.AbstractSet;
import model.sets.Loader;
import model.sets.PredefinedNumberSet;

@SuppressWarnings({"serial", "rawtypes"})
public class SetsDropdownMenu extends JComboBox {

	private static Map<String, Class> myClassNamesMap;

	public SetsDropdownMenu() {
		super(initResources());
	}

	private static String[] initResources () {		
		myClassNamesMap = new HashMap<String, Class>();

		Class[] classes = Loader.getLoadedClasses(System.getProperty("user.dir") 
				+ "/bin/model/sets/num");
		HashSet<String> temp = new HashSet<String>();

		for (Class c : classes) {
			if (!(Modifier.isAbstract(c.getModifiers())) && PredefinedNumberSet.class.isAssignableFrom(c)) {
				try {
					String name = ((AbstractSet) c.getConstructor().newInstance()).getName();
					if (!(name == null || name.length() == 0))
						temp.add(name);
					myClassNamesMap.put(name, c);
				} 

				// none of these exceptions should be thrown
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}

		String[] names = new String[temp.size()];
		int index = 0;
		Iterator<String> it = temp.iterator();
		while (it.hasNext()) {
			names[index] = it.next();
			index++;
		}

		return names;
	}



	public AbstractSet getSelectedSet () {
		Class c = myClassNamesMap.get((String) getSelectedItem());
		try {
			return (AbstractSet) c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}




}
