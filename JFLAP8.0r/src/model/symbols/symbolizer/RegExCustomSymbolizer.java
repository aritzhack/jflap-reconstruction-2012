package model.symbols.symbolizer;

import model.regex.RegularExpression;

public class RegExCustomSymbolizer extends CustomSymbolizer {

	public RegExCustomSymbolizer(RegularExpression fd) {
		super(fd.getOperators(), fd.getInputAlphabet());
	}
}
