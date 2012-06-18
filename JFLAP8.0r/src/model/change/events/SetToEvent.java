package model.change.events;

import model.formaldef.components.Settable;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public class SetToEvent<T extends Settable<T>> extends AdvancedUndoableEvent {

	private T myFrom;
	private T myTo;
	private T myBase;

	public SetToEvent(T source, T from, T to) {
		super(source, ITEM_MODIFIED);
		myBase = source;
		myFrom = from;
		myTo = to;
	}

	@Override
	public boolean undo() {
		return myBase.setTo(myFrom);
	}
	
	@Override
	public boolean redo() {
		return myBase.setTo(myTo);
	}
	
	@Override
	public T getSource() {
		return (T) super.getSource();
	}
	
	public T getFrom(){
		return myFrom;
	}
	
	public T getTo(){
		return myTo;
	}

	@Override
	public String getName() {
		return "Set " + myFrom + " to " + myTo;
	}

}
