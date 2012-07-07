package model.numbersets.sets;


public abstract class FiniteSet extends AbstractSet {

	
	@Override
	public boolean isFinite () {
		return true;
	}
	
	public abstract int getCardinality ();
	
	
}
