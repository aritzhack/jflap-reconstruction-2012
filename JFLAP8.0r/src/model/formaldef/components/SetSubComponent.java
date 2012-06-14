package model.formaldef.components;

import model.change.ChangeApplyingObject;
import model.change.events.SetToEvent;
import model.formaldef.Describable;
import util.Copyable;

public abstract class SetSubComponent<T> extends ChangeApplyingObject 
										implements Describable, 
														Copyable, 
														Comparable<T>, 
														Settable<T>{
	
}
