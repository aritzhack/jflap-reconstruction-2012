package model.numbersets;

import java.util.Set;

public abstract class AbstractNumberSet {

	public abstract Set<Integer> getSet();
	
	public abstract boolean contains (int value);
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract boolean isFinite ();
	
}
