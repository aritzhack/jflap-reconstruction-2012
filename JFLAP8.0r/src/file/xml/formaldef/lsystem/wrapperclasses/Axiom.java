package file.xml.formaldef.lsystem.wrapperclasses;

import universe.preferences.JFLAPPreferences;
import debug.JFLAPDebug;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Special implementation of a SymbolString to allow for TransducerFactory to
 * work correctly (maps the proper transducer to the axiom, but not other
 * instances of SymbolString) and allow for simple parsing (ignores Symbolizer
 * as each Symbol is space separated).
 * 
 * @author Ian McMahon
 * 
 */
public class Axiom extends SymbolString {

	public Axiom() {
		super();
	}

	public Axiom(Symbol... symbols) {
		super(symbols);
	}

	public Axiom(SymbolString subList) {
		super(subList);
	}

	public Axiom(String text) {
		this(parseSimpleText(text));
	}

	/**
	 * Splits text on whitespace after trimming to separate Symbols from one
	 * another. 
	 * So "  abc      def g h   ij   " would become {abc, def, g, h, ij}
	 * 
	 * @param text
	 * @return Array of Symbols representing the input text.
	 */
	private static Symbol[] parseSimpleText(String text) {
		if(text.equals(JFLAPPreferences.getEmptyStringSymbol()) || text.equals(""))
			return new Symbol[0];
		text = text.trim();
		String[] splitString = text.split("\\s+");
		Symbol[] symbols = new Symbol[splitString.length];
		
		for (int i = 0; i < symbols.length; i++)
			symbols[i] = new Symbol(splitString[i]);
		return symbols;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
