package model.numbersets;

import java.util.Set;
import java.util.TreeSet;

/**
 * Finite set with values input by the user
 * 
 * @author Peggy Li
 * 
 */

public class CustomSet {

	private String name;
	private String description;

	private Set<Integer> elements;

	public CustomSet() {
		elements = new TreeSet<Integer>();
	}

	public boolean add(int value) {
		elements.add(value);
		return true;
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
	 *            to remove
	 * @return true if value was found and removed from set, else false
	 */
	public boolean remove(int value) {
		if (elements.contains(value)) {
			elements.remove(value);
			return true;
		}
		return false;
	}
	
	
	
	
	public boolean contains(int value) {
		return elements.contains(value);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName() + "\n");
		sb.append(getDescription() + "\n");
		sb.append(elements.toString());
		return sb.toString();
	}

}
