package model.numbersets.sets;

public abstract class Element {
	
	private String myValue;
	
	public Element (String val) {
		myValue = val;
	}
	
	
	@Override
	public int hashCode () {
		return myValue.hashCode();
	}
	
	
	@Override
	public boolean equals (Object obj) {
		if (!(obj instanceof Element))
			return false;
		return this.myValue == ((Element) obj).myValue;
	}
	
	
	

}
