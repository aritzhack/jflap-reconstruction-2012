package model.numbersets.sets;

import java.util.Set;

public abstract class AbstractSet {

	public abstract Set getSet();
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract boolean isFinite();
	
	public abstract boolean contains();
	
	@Override
	public int hashCode () {
		return getSet().hashCode() * getName().hashCode();
	}
	
	public boolean equals (Object obj) {
		return getSet().equals(((AbstractSet) obj).getSet());
	}
	
	
}
