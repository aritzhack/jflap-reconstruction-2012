package model.formaldef.components;

import oldnewstuff.model.change.ChangeApplyingObject;
import oldnewstuff.model.change.events.SetToEvent;
import model.formaldef.Describable;
import util.Copyable;

public interface SetSubComponent<T> extends Describable, 
											Copyable, 
											Comparable<T>, 
											Settable<T>{
	
}
