package model.formaldef;

/**
 * A generic interface used to enforce all essential components
 * of a formal definition.
 * @author Julian Genkins
 *
 */
public abstract class FormalDefinitionComponent implements Describable {

	
	/**
	 * Every {@link FormalDefinitionComponent} is traditionally
	 * associated with a single character abbreviation.
	 * @return the single {@link Character} abbr
	 */
	public abstract Character getCharacterAbbr();
	
	
	@Override
	public boolean equals(Object o) {
		return o.getClass().isAssignableFrom(this.getClass()) ||
				this.getClass().isAssignableFrom(o.getClass());
	}
	
	@Override
	public abstract String toString();
	
}
