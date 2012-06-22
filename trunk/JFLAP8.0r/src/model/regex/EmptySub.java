package model.regex;

import preferences.JFLAPPreferences;
import model.grammar.Terminal;
import model.symbols.Symbol;

public class EmptySub extends Terminal {

	public EmptySub(String s) {
		super(s);
	}
	
	@Override
	public String toString() {
		return JFLAPPreferences.getEmptyStringSymbol();
	}

}
