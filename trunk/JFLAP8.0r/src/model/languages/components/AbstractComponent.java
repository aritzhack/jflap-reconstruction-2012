package model.languages.components;

import java.util.Collection;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public abstract class AbstractComponent {
	
	
	public abstract Collection<Symbol> getSymbolsUsed ();
	
	
	public abstract SymbolString deriveString ();
	
	

}
