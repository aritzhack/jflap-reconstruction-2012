package view.sets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComboBox;

import model.numbersets.defined.PrimeSet;
import model.sets.AbstractSet;
import model.sets.num.PredefinedNumberSet;
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
		myClassNamesMap.put("Prime Numbers", PrimeSet.class);
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
	
	
	public AbstractSet getSelectedSet() {
		Class cl = myClassNamesMap.get((String) getSelectedItem());
		getParameters(cl);
	
		
		return null;
	}
	
	
	private void getParameters(Class c) {
		if (c.equals(FactorSet.class)) {
			promptForParameters();
		}
		if (c.equals(CongruenceSet.class)) {
			promptForParameters();
		}
		
	}
	
	private void promptForParameters() {
		
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
