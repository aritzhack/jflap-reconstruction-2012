package model.languages;

/**
 * Utility class for generating a set of strings
 * in a language given a restricted grammar 
 * for the language
 */

import java.util.Set;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;

public abstract class Language {
	
	private Alphabet myAlphabet;
	private Set<SymbolString> myStrings;
	

	public Language (Alphabet alpha) {
		myAlphabet = alpha;
	}
	
	public Language (Grammar g) {
		myAlphabet = g.getTerminals();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Language))
			return false;
		Language other = (Language) obj;
		if (!myAlphabet.equals(other.myAlphabet))
			return false;
		return myStrings.equals(other.myStrings);
	}
	
	
	public Set<SymbolString> getStrings () {
		return myStrings;
	}
	
	
	


}
