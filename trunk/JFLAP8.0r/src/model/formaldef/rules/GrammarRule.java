package model.formaldef.rules;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.specific.TerminalAlphabet;
import model.formaldef.components.alphabets.specific.VariableAlphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.grammar.Grammar;
import errors.BooleanWrapper;


public abstract class GrammarRule<S extends Grammar, T extends Alphabet> extends SymbolRule<S, T> {

	@Override
	public BooleanWrapper canRemove(Grammar parent, Alphabet a,
			Symbol oldSymbol) {
		return new BooleanWrapper(true);
	}

	
	protected BooleanWrapper checkNoOverlappingChars(String string, Alphabet alph) {
		for (char c: string.toCharArray()){
			Symbol s = alph.getFirstSymbolContaining(c);
			if (s != null)
				return new BooleanWrapper(false, "Without grouping, the " + this.getName() + 
						" set cannot share characters with any symbol in the " + 
						alph.getName() +", and " + string + " has \'" + c + "\' in common with " + s.toString());
		}
		return new BooleanWrapper(true, "Symbol " + string + " can be added sucessfully");
	}
	
	protected BooleanWrapper checkIdentical(String string, Alphabet a) {
		if (a instanceof VariableAlphabet){
			string = a.getGrouping().creatGroupedString(string);
		}
		else if (a instanceof TerminalAlphabet){
			string = string.substring(1, string.length());
		}
		return new BooleanWrapper(!a.containsSymbolWithString(string), "You may not add a symbol to " + this.getName() + 
				 " which has String identical to a symbol in the " + a.getName());
	}
	
	public BooleanWrapper checkValid(Alphabet alph, Symbol newSymbol) {
		BooleanWrapper bw;
		if (alph.usingGrouping()){
			bw = this.checkIdentical(newSymbol.toString(), alph);
		}
		else
			bw = this.checkNoOverlappingChars(newSymbol.toString(), alph);
		return bw;
	}
	

}
