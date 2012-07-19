package model.sets;

import java.util.Set;

import model.sets.elements.Element;

public class CustomInfiniteSet extends InfiniteSet implements Customizable {
	
	private String myName;
	private String myDescription;
	private Set<Element> myElements;
	
	public CustomInfiniteSet () {
		
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
	public void generateMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return myName;
	}

	@Override
	public String getDescription() {
		return myDescription;
	}

	@Override
	public boolean contains(Element e) {
		return myElements.contains(e);
	}

	

}
