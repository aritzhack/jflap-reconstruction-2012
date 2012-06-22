package oldnewstuff.model.change.events;

import oldnewstuff.model.change.ChangeEvent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;
import model.symbols.Symbol;

public class SetComponentModifyEvent<T extends SetSubComponent<T>> extends SetComponentEvent<T> {

	private SetToEvent<T, T> myEvent;

	public SetComponentModifyEvent( SetComponent<T> source, SetToEvent<T, T> e) {
		super(ITEM_MODIFY, source, e.getFrom());
		myEvent = e;
	}

	@Override
	public boolean undo() {
		return myEvent.undo();
	}

	@Override
	public boolean redo() {
		return myEvent.redo();
	}

	@Override
	public String getName() {
		return "Modify " + this.getItems().get(0).getDescriptionName();
	}

	public T getModifiedItem() {
		return myEvent.getTo();
	}

	@Override
	public boolean applyChange() {
		return myEvent.applyChange();
	}

}
