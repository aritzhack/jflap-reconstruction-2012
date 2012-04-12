package model.formaldef.components;

import javax.swing.event.ChangeEvent;

import model.util.UtilFunctions;

public class ComponentChangeEvent extends ChangeEvent{

	private Class<? extends FormalDefinitionComponent> myComponentClass;
	
	private int myChangeType;
	
	private Object[] myArgs;
	
	
	public ComponentChangeEvent(Class<? extends FormalDefinitionComponent> clazz,
									int type,
									Object ... args){
		super(UtilFunctions.concatAll(new Object[]{clazz, type}, args));
		myComponentClass = clazz;
		myChangeType = type;
		myArgs = args;
	}
	
	
	/**
	 * Returns true if the {@link FormalDefinitionComponent} 
	 * class associated with this {@link ComponentChangeEvent}
	 * is the same as or is a subclass of the passed in 
	 * <code>FormalDefinitionComponent</code> 
	 * 
	 * @see {@link Class}.isAssignableFrom()
	 * 
	 * @param c
	 * @return
	 */
	public boolean comesFrom(FormalDefinitionComponent c){
		return myComponentClass.isAssignableFrom(c.getClass());
	}
	
	public int getNumArgs(){
		return myArgs.length;
	}
	
	public Object getArg(int i){
		return myArgs[i];
	}
	
	public int getType(){
		return myChangeType;
	}
	
}
