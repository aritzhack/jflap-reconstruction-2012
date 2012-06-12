package model.regex;

import preferences.JFLAPPreferences;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;

public class EmptySub extends Terminal {

	public EmptySub(String s) {
		super(s);
	}
	
	@Override
	public String toString() {
		return JFLAPPreferences.getEmptyStringSymbol();
	}

}
