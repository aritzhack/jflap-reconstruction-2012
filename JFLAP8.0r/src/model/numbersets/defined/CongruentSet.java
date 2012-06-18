package model.numbersets.defined;


/**
 * Congruence using modular arithmetic
 * a ~= b (mod n) if a mod n = b mod n or (a-b) mod n = 0
 * 
 * @author Peggy Li
 * 
 */


import java.util.Set;
import java.util.TreeSet;


public class CongruentSet extends PredefinedSet {

	private Set<Integer> myValues;

	private int myModulus;
	private int myInitial;

	/**
	 * Constructor for all numbers i such that
	 * i mod modulus = startValue mod modulus
	 * 
	 * @param modulus the modulus of the congruence relation
	 * @param startValue first value in the set
	 */
	public CongruentSet(int modulus, int startValue) {
		
		myModulus = modulus;
		
		if (startValue >= modulus)
			startValue = wrapStart(startValue);
		myInitial = startValue;
		
		myValues = new TreeSet<Integer>();
	}
	
	private int wrapStart(int start) {
		while (start >= myModulus) {
			start -= myModulus;
		}
		return start;
	}
	

	public int getModulus() {
		return myModulus;
	}

	/**
	 * If <code>mod</code> differs from current modulus value,
	 * all values are removed from the set and NOT recomputed 
	 * at the time.
	 * 
	 * @param mod
	 *            the new modulus value
	 */
	public void setModulus(int mod) {
		if (myModulus != mod) {
			reset();
			myModulus = mod;
		}
	}

	@Override
	public Set<Integer> getSet() {
		return myValues;
	}

	@Override
	public Set<Integer> generateNextNumbers(int n) {
		int buffer = myValues.size();
		for (int i = buffer; i < n + buffer; i++) {
			myValues.add(myInitial + myModulus * i);
		}
		
		return myValues;
	}

	@Override
	public int getNthElement(int n) {
		return myInitial + myModulus * n; 
	}

	@Override
	public boolean contains(int n) {
		return (n - myInitial) % myModulus == 0;
	}

	@Override
	public String getName() {
		return "Equivalence Set";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		myValues.clear();
	}

	@Override
	public Set<Integer> getValuesInRange(int min, int max) {
		// TODO Auto-generated method stub
		Set<Integer> mods = new TreeSet<Integer>();
		
		int start = myInitial + (int) Math.floor(Math.abs(min - myInitial) % myModulus);
		System.out.println(start);
		
		
		return mods;
	}

	@Override
	public int getSize() {
		return myValues.size();
	}

}
