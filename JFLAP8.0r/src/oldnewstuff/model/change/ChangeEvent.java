package oldnewstuff.model.change;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.Symbol;


public abstract class ChangeEvent implements ChangeTypes{

	private int myType;
	private Object mySource;
	
	public ChangeEvent(int type, Object source){
		myType = type;
		mySource = source;
	}
	
	public ChangeEvent(Object source){
		this(NULL, source);
	}
	
	/**
	 * Returns true if the {@link FormalDefinitionComponent} 
	 * class associated with this {@link AdvancedChangeEvent}
	 * is the same as or is a subclass of the passed in 
	 * <code>FormalDefinitionComponent</code> 
	 * 
	 * @see {@link Class}.isAssignableFrom()
	 * 
	 * @param c
	 * @return
	 */
	public boolean comesFrom(Object o){
		return mySource.getClass().isAssignableFrom(o.getClass());
	}
	
	/**
	 * Checks to see if this change is of the type we desire, see
	 * the interface {@link ChangeTypes} for int values of various
	 * changes used.
	 * 
	 * @param type
	 * @return
	 */
	public boolean isOfType(int type){
		return myType == type;
	}
	
	/**
	 * Applies this change. 
	 * @return 
	 */
	public abstract boolean applyChange();
	
	public abstract String getName();
}
