package model.formaldef;

import errors.BooleanWrapper;

/**
 * A generic interface used to enforce all essential components
 * of a formal definition.
 * @author Julian Genkins
 *
 */
public interface FormalDefinitionComponent extends Describable {

	
	/**
	 * Every {@link FormalDefinitionComponent} is traditionally
	 * associated with a single character abbreviation.
	 * @return the single {@link Character} abbr
	 */
	public abstract Character getCharacterAbbr();
	
	
	/**
	 * Checks to see if this {@link FormalDefinitionComponent} has
	 * been constructed to "completion" i.e. is functional
	 * 
	 * @return true or false and a descriptive reason why not
	 */
	public abstract BooleanWrapper isComplete();
	
}
