package model.languages.sets;

import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class Main {
	
	public static void main (String[] args) {
		Alphabet alpha = new InputAlphabet();
		alpha.addAll(new Symbol("a"), new Symbol("ab"));
		SymbolString a = SymbolString.createFromDefinition("a", alpha);
		SymbolString ab = SymbolString.createFromDefinition("ab", alpha);
		
		System.out.println(a.getFirst().getString());
		System.out.println(ab.getFirst().getString());
		System.out.println(a.compareTo(ab));
		
	}

}
