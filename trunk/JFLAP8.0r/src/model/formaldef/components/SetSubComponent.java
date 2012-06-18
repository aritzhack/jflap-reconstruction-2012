package model.formaldef.components;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;

import oldnewstuff.model.change.ChangeApplyingObject;
import model.change.ChangingObject;
import model.change.events.SetToEvent;
import model.formaldef.Describable;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.symbols.Symbol;
import util.Copyable;

public abstract class SetSubComponent<T extends SetSubComponent<T>> extends ChangingObject 
																	implements Describable, 
																				Copyable, 
																				Comparable<T>, 
																				Settable<T>{
	
	private SetComponent<T> myParent;

	public void setParent(SetComponent<T> parent){
		if (myParent != null && !myParent.equals(parent)){
			clearParent();
		}
		myParent = parent;
		this.addListener(myParent);
	}
	
	public void clearParent() {
		this.removeListener(myParent);
		myParent.remove(this);
		myParent = null;
	}

	public SetComponent<T> getParent(){
		return myParent;
	}

	@Override
	public boolean setTo(T other) {
		ChangeEvent change = new SetToEvent<T>((T) this, this.copy(), other);
		if (myParent.contains(other)){
			throw new FormalDefinitionException("The " + myParent.getDescriptionName() + 
					" already contains the " + other.getDescriptionName() + " " + other.toString() +".");
		}
		applySetTo(other);
		distributeChange(change);
		return true;
	}
	
	protected abstract void applySetTo(T other); //not sure if I like this.

	@Override
	public abstract T copy();
	
	
}
