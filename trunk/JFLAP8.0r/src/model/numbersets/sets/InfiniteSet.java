package model.numbersets.sets;


public abstract class InfiniteSet extends AbstractSet {

	
	@Override
	public boolean isFinite () {
		return false;
	}
	
	public abstract void generateMore ();
	
	
	public boolean overflow (int n) {
		return Integer.toBinaryString(n).length() > 31;
	}
	
	
	public String getOverflowMessage () {
		return "Infinite set: unable to show more elements due to integer overflow";
	}
	
}
