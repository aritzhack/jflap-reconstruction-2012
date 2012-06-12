package model.languages.sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Peggy Li
 * @param <T>
 *
 */

public class FiniteSet<T> implements AbstractJflapSet {
	
	private String myDescription;
	private Set<T> myElements;
	
	public FiniteSet () {
		myElements = new HashSet<T>();
	}
	
	
	public void add (T element) {
		myElements.add(element);
	}
	
	public void allAll (T... elements) {
		for (T e : elements) {
			this.add(e);
		}
	}
	
	public Collection<T> getElements () {
		return myElements;
	}
	

	/**
	 * Returns the cardinality of the set
	 * 
	 * @return number of elements in the set
	 */
	public int getCardinality() {
		return myElements.size();
	}

	
	@Override
	public void setDescription(String description) {
		myDescription = description;
	}

	@Override
	public String getDescription() {
		if (myDescription == null) 
			return "No description available.";
		return myDescription;
	} 
	
}
