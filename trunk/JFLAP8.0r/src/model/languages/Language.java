package model.languages;

/**
 * Utility class for generating a set of strings
 * in a language given a restricted grammar 
 * for the language
 */

import java.util.Set;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SymbolString;

public abstract class Language {
	
	private Alphabet myAlphabet;
	private Set<SymbolString> myStrings;
	

	public Language (Alphabet alpha) {
		myAlphabet = alpha;
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
