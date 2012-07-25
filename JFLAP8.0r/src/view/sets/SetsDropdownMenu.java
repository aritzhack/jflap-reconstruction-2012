package view.sets;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import model.sets.AbstractSet;
import model.sets.num.CongruenceSet;
import model.sets.num.EvensSet;
import model.sets.num.FactorSet;
import model.sets.num.FibonacciSet;
import model.sets.num.OddsSet;
import model.sets.num.PrimesSet;

@SuppressWarnings({"serial", "rawtypes"})
public class SetsDropdownMenu extends JComboBox {

	private static Map<String, Class> myClassNamesMap;
	
	static {
		myClassNamesMap = new HashMap<String, Class>();
		myClassNamesMap.put("Fibonacci", FibonacciSet.class);
		myClassNamesMap.put("Prime Numbers", PrimesSet.class);
		myClassNamesMap.put("Even Numbers", EvensSet.class);
		myClassNamesMap.put("Odd Numbers", OddsSet.class);
		myClassNamesMap.put("Factors ofSet", FactorSet.class);
		myClassNamesMap.put("CongruenceSet", CongruenceSet.class);
	}

	public SetsDropdownMenu() {
		super(getNamesToArray());
	}
	
	private static String[] getNamesToArray() {
		String[] array = new String[myClassNamesMap.size()];
		int index = 0;
		Iterator iter = myClassNamesMap.keySet().iterator();
		while(iter.hasNext()) {
			array[index] = (String) iter.next();
			index++;
		}
		return array;
	}
	
	
	public AbstractSet getSelectedSet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Class cl = myClassNamesMap.get((String) getSelectedItem());
		
		if (cl.equals(FactorSet.class)) {
			int[] params = getParameters(new String[] {"factor"});
			return (AbstractSet) cl.getDeclaredConstructor(int.class).newInstance(params[0]);
		}
		if (cl.equals(CongruenceSet.class)) {
			int[] params = getParameters(new String[] {"dividend", "modulus"});
			return (AbstractSet) cl.getDeclaredConstructor(int.class, int.class).newInstance(params[0], params[1]);
		}
		return (AbstractSet) cl.newInstance();
	}
	
	
	
	
	private int[] getParameters(String[] names) {
		int[] values = new int[names.length];
		for (int i = 0; i < names.length; i++) {
			values[i] = promptForParameter(names[i]);
		}
		return values;
		
	}
	
	private int promptForParameter(String name) {
		String ans = JOptionPane.showInputDialog("Enter value for " + name + ": ");
		int i;
		try {
			i = Integer.parseInt(ans);
		} catch (NumberFormatException e) {
			i = promptForParameter(name);
		}
		return i;
		
	}
	

//	private static String[] initResources () {		
//		myClassNamesMap = new HashMap<String, Class>();
//
//		HashSet<String> temp = new HashSet<String>();
//
//		for (Class c : PREDEFINED_SETS) {
//			if (!(Modifier.isAbstract(c.getModifiers())) && PredefinedNumberSet.class.isAssignableFrom(c)) {
//				try {
//					String name = ((AbstractSet) c.getConstructor().newInstance()).getName();
//					if (!(name == null || name.length() == 0))
//						temp.add(name);
//					myClassNamesMap.put(name, c);
//				} 
//
//				// none of these exceptions should be thrown
//				catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (SecurityException e) {
//					e.printStackTrace();
//				} catch (InstantiationException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		String[] names = new String[temp.size()];
//		int index = 0;
//		Iterator<String> it = temp.iterator();
//		while (it.hasNext()) {
//			names[index] = it.next();
//			index++;
//		}
//
//		return names;
//	}
//
//
//
//	public AbstractSet getSelectedSet () {
//		Class c = myClassNamesMap.get((String) getSelectedItem());
//		try {
//			return (AbstractSet) c.newInstance();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	
//	/**
//	 * Returns an array of the names of the parameters
//	 * required to instantiate the class
//	 * Assumes that all parameters are of type int 
//	 * 
//	 * @param c the class 
//	 */
//	public String[] getConstructorParameters(Class c) {
//		if (c.equals(FactorSet.class)) {
//			return new String[]{"factor"};
//		}
//		if (c.equals(CongruenceSet.class)) {
//			return new String[]{"first", "second"};
//		}
//		return new String[]{};
//	}
//	
//	
//	
//	public static final Class[] PREDEFINED_SETS = {
//		FibonacciSet.class,
//		PrimesSet.class,
//		EvensSet.class,
//		OddsSet.class,
//		FactorSet.class,
//		CongruenceSet.class
//	};




}
