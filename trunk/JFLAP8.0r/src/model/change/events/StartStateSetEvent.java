package model.change.events;

import model.automata.StartState;
import model.automata.State;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public class StartStateSetEvent extends IndividualComponentChange<StartState, State> {

	public StartStateSetEvent(StartState source, State from, State to) {
		super(source,from, to);
	}

	@Override
	public boolean undo() {
		return this.getSource().setState(getFrom());
	}

	@Override
	public boolean redo() {
		return this.getSource().setState(getTo());
	}


}
