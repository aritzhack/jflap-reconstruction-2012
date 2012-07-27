package model.sets;

import java.util.Set;

import util.Copyable;

import model.sets.elements.Element;

public abstract class AbstractSet implements Copyable {

	public abstract Set<Element> getSet();
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract void setName(String name);
	
	public abstract void setDescription(String description);
	
	public abstract boolean isFinite();
	
	public abstract boolean contains(Element e);
	
	@Override
	public int hashCode () {
		return getName().hashCode();
	}
	
	public boolean equals (Object obj) {
		AbstractSet other = (AbstractSet) obj;
		return 	getName().equals(other.getName()) && 
				getDescription().equals(other.getDescription()) &&
				getSet().equals(other.getSet());
	}
	
	public String toString () {
		return getName();
	}
	
	public abstract String getSetAsString();
	
	
}
