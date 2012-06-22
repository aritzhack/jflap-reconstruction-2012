package oldnewstuff.model.change.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

public abstract class SetComponentEvent<T extends SetSubComponent<T>> extends UndoableChangeEvent {

	private List<T> myItems;

	public SetComponentEvent(int type, SetComponent<T> source, T ... s) {
		this(type, source, Arrays.asList(s));

	}
	
	public SetComponentEvent(int type, SetComponent<T> source, Collection<T> s) {
	super(type, source);
	myItems = new ArrayList<T>(s);
	}

	public List<T> getItems(){
		return myItems;
	}


}
