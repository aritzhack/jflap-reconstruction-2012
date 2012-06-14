package oldnewstuff.model.change;

import oldnewstuff.model.change.interactions.Interaction;

/**
 * A simple abstract class designed to work with anything that is related
 * to a ChangeEvent. It simply provides a common method to check if
 * a given ChangeEvent actually applies to the object or not.
 * @author Julian
 *
 */
public abstract class ChangeRelated implements ChangeTypes, Comparable<ChangeRelated>{

	private int myType;

	public ChangeRelated(int type){
		myType = type;
	}
	
	/**
	 * Checks to see if this object applies to the given change event.
	 * 
	 * @param event
	 * @return
	 */
	public boolean appliesTo(ChangeEvent event) {
		return event.isOfType(myType);
	}
	
	@Override
	public int compareTo(ChangeRelated o) {
		return new Integer(myType).compareTo(o.myType);
	}
}
