package model.formaldef.components;

import model.formaldef.Describable;
import errors.BooleanWrapper;

/**
 * A generic interface used to enforce all essential components
 * of a formal definition.
 * @author Julian Genkins
 *
 */
public interface FormalDefinitionComponent extends Describable, Cloneable{

	
	/**
	 * Every {@link FormalDefinitionComponent} is traditionally
	 * associated with a single character abbreviation.
	 * @return the single {@link Character} abbr
	 */
	public Character getCharacterAbbr();
	
	
	/**
	 * Checks to see if this {@link FormalDefinitionComponent} has
	 * been constructed to "completion" i.e. is functional
	 * 
	 * @return true or false and a descriptive reason why not
	 */
	public BooleanWrapper isComplete();

	/**
	 * Creates an exact clone of this {@link FormalDefinitionComponent}
	 * 
	 * @return the clone
	 */
	public FormalDefinitionComponent clone();
}
