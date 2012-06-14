package model.formaldef.components;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import util.Copyable;


import model.change.AdvancedChangingObject;
import model.change.ChangeEvent;
import model.change.ChangeListener;
import model.change.ChangeTypes;
import model.change.ChangeDistributingObject;
import model.change.rules.Rule;
import model.change.rules.FormalDefinitionRule;
import model.formaldef.Describable;
import model.formaldef.FormalDefinition;
import errors.BooleanWrapper;

/**
 * A generic interface used to enforce all essential components
 * of a formal definition.
 * @author Julian Genkins
 *
 */
public abstract class FormalDefinitionComponent extends AdvancedChangingObject implements Describable, Copyable, ChangeTypes{

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

	/**
	 * Creates an exact clone of this {@link FormalDefinitionComponent}
	 * Does not set the parent definition if one exists.
	 * 
	 * @return the clone
	 */
	@Override
	public abstract FormalDefinitionComponent copy();


	
	
}
