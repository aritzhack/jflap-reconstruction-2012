package oldnewstuff.model.change.events;

import model.formaldef.components.Settable;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public abstract class SetToEvent<T extends Settable<S>, S> extends UndoableChangeEvent {

	private S myFrom;
	private S myTo;
	private T myBase;

	public SetToEvent(T source, S from, S to) {
		super(SET_TO, source);
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
	
	public S getFrom(){
		return myFrom;
	}
	
	public S getTo(){
		return myTo;
	}

}
