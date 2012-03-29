package model.formaldef.components.alphabets.specific;

import java.util.ArrayList;
import java.util.List;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.Terminal;




public class TerminalAlphabet extends Alphabet {


	@Override
	public String getDescriptionName() {
		return "Terminals";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSymbolName() {
		return "Terminal";
	}
	
	@Override
	public boolean add(Symbol e) {
		return super.add(new Terminal(e.getString()));
	}


	
}
