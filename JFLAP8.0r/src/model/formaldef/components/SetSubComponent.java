package model.formaldef.components;

import javax.swing.event.ChangeEvent;

import oldnewstuff.model.change.ChangeApplyingObject;
import model.change.ChangingObject;
import model.change.events.SetToEvent;
import model.formaldef.Describable;
import model.formaldef.components.symbols.Symbol;
import util.Copyable;

public abstract class SetSubComponent<T extends SetSubComponent<T>> extends ChangingObject 
																	implements Describable, 
																				Copyable, 
																				Comparable<T>, 
																				Settable<T>{
							
	@Override
	public boolean setTo(T other) {
		ChangeEvent change = new SetToEvent<T>((T) this, this.copy(), other);
		applySetTo(other);
		distributeChange(change);
		return true;
	}
	
	protected abstract void applySetTo(T other); //not sure if I like this.

	@Override
	public abstract T copy();
	
	
}
