package model.numbersets;

import java.util.Set;
import java.util.TreeSet;


/**
 * Finite set with values input by the user
 * 
 * @author Peggy Li
 * 
 */

public class CustomSet extends AbstractNumberSet {

	
	private String name;
	private String description;

	private Set<Integer> elements;

	public CustomSet() {
		elements = new TreeSet<Integer>();
		
	}
	
	public CustomSet (Set<Integer> set) {
		elements = set;
	}
	

	public CustomSet(int... i) {
		this();
		addAll(i);
	}

	public void add(int value) {
		elements.add(value);
	}

	public void addAll(int... values) {
		for (int v : values) {
			add(v);
		}
	}

	/**
	 * Removes the specific value from the set if the set contains an element
	 * with that value
	 * 
	 * @param value
	 *          number to remove
	 */
	public void remove(int value) {
		elements.remove(value);
	}
	
	public Set<Integer> getSet () {
		return elements;
	}
	
	public boolean contains(int value) {
		return elements.contains(value);
	}

	public void setName(String name) {
		System.out.println("Named " + name);
		
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void addDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append(getName() + "\n");
//		sb.append(getDescription() + "\n");
		sb.append(elements.toString());
		return sb.toString();
	}

	@Override
	public boolean isFinite() {
		return true;
	}


}
