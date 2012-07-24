package model.sets;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import model.sets.elements.Element;

public class CustomFiniteSet extends FiniteSet implements Customizable {

	private String myName;
	private String myDescription;
	private Set<Element> myElements;
	
	/**
	 * Creates a new finite set with the specified name, description, and elements
	 * @param name
	 * @param description
	 * @param elements
	 */
	public CustomFiniteSet(String name, String description, Set<Element> elements) {
		myName = name;
		myDescription = description;
		myElements = elements;
	}
	
	/**
	 * Creates a new finite set with the specified name and elements
	 * 
	 * If no description is provided, the description field will be initialized
	 * to an empty string by default
	 * @param name
	 * @param elements
	 */
	public CustomFiniteSet (String name, Set<Element> elements) {
		this(name, "", elements);
	}
	
	@Override
	public void setName(String name) {
		myName = name;		
	}

	@Override
	public void setDescription(String description) {
		myDescription = description;

	}

	@Override
	public void add(Element e) {
		myElements.add(e);
	}


	@Override
	public void remove(Element e) {
		myElements.remove(e);
		
	}

	@Override
	public int getCardinality() {
		return myElements.size();
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public boolean contains(Element e) {
		return myElements.contains(e);
	}

	@Override
	public String getName() {
		return myName;
	}

	@Override
	public String getDescription() {
		return myDescription;
	}

	
	
	public String getSetAsString() {
		StringBuilder s = new StringBuilder();
		ArrayList<Element> list = new ArrayList<Element>(myElements);
		for (int i = 0; i < myElements.size() - 1; i++) {
			s.append(list.get(i).toString() + ", ");
		}
		s.append(list.get(list.size()-1));
		
		return s.toString();
	}
}
