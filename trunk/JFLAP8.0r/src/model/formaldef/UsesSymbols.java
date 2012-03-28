package model.formaldef;

import java.util.Set;

import model.formaldef.components.alphabets.symbols.Symbol;

/**
 * A simple interface to give an object the quality of 
 * using symbols and therefore having some set of unique
 * symbols applied within that object. Involved with the
 * trimming of alphabets.
 * 
 * @author Julian Genkins
 *
 */
public interface UsesSymbols {

	public Set<Symbol> getUniqueSymbolsUsed();
	
	
}
