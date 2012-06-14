package oldnewstuff.model.change.events;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.formaldef.FormalDefinition;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;

public class TrimAlphabetsEvent extends CompoundUndoableChangeEvent {

	public TrimAlphabetsEvent(FormalDefinition source, 
			Map<Alphabet, Set<Symbol>> trimMap) {
		super(source, "Trim Alphabets");
		for(Entry<Alphabet, Set<Symbol>> e: trimMap.entrySet()){
			Symbol[] toRemove = e.getValue().toArray(new Symbol[0]);
			this.addSubEvents(e.getKey().createRemoveEvent(toRemove));
		}
	}



}
