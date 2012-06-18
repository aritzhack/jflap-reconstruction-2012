package model.change.events;

import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;

public class SpecialSymbolSetEvent extends IndividualComponentChange<SpecialSymbol, Symbol> {

	public SpecialSymbolSetEvent(SpecialSymbol source, Symbol from, Symbol to) {
		super(source, from, to);
	}

	@Override
	public boolean undo() {
		return this.getSource().setSymbol(this.getFrom());
	}

	@Override
	public boolean redo() {
		return this.getSource().setSymbol(this.getTo());
	}


}
