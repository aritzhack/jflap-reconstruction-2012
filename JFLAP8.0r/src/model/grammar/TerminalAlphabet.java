package model.grammar;

import java.util.ArrayList;
import java.util.List;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;




public class TerminalAlphabet extends Alphabet {


	@Override
	public String getDescriptionName() {
		return "Terminals";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'T';
	}

	@Override
	public String getDescription() {
		return "The terminal alphabet.";
	}

	@Override
	public String getSymbolName() {
		return "Terminal";
	}
	
	@Override
	public boolean add(Symbol e) {
		return super.add(new Terminal(e.getString()));
	}


	@Override
	public TerminalAlphabet copy() {
		return (TerminalAlphabet) super.copy();
	}
	
}
