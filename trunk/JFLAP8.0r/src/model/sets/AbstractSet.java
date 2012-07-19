package model.sets;

import java.util.Set;

import model.sets.elements.Element;

public abstract class AbstractSet {

	public abstract Set<Element> getSet();
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract boolean isFinite();
	
	public abstract boolean contains(Element e);
	
	@Override
	public int hashCode () {
		return getName().hashCode();
	}
	
	public boolean equals (Object obj) {
		return getSet().equals(((AbstractSet) obj).getSet());
	}
	
	public String toString () {
		return getName();
	}
	
	
	public abstract String getSetAsString();
	
}
