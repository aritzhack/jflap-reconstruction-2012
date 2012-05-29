package model.regex;

import universe.preferences.JFLAPPreferences;
import model.formaldef.components.symbols.Symbol;

public class EmptySub extends Symbol {

	public EmptySub(String s) {
		super(s);
	}
	
	@Override
	public String toString() {
		return JFLAPPreferences.getEmptyStringSymbol();
	}

}
