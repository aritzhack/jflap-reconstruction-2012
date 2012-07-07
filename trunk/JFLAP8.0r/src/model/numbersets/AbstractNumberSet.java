package model.numbersets;

import java.util.Set;

public abstract class AbstractNumberSet {

	public abstract Set<Integer> getSet();
	
	public abstract boolean contains (int value);
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract boolean isFinite ();
	
	
	@Override
	public int hashCode() {
		return getSet().hashCode() + getName().hashCode();		
	}
	
	
	@Override
	public boolean equals (Object obj) {
		AbstractNumberSet other = (AbstractNumberSet) obj;
		return this.getSet().equals(other.getSet());
	}
}
