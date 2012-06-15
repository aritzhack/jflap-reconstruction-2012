package model.languages.samplelanguages;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.languages.Language;

public class EvenLetterLanguage extends Language{
	private Terminal myTerminal;
	
	public EvenLetterLanguage(TerminalAlphabet alpha, Terminal letter) {
		super(alpha);
		myTerminal = letter;
	}


	@Override
	public void createVariablesAndProductions(VariableAlphabet v,
			ProductionSet p) {
		Variable S = new Variable("S"), A = new Variable("A");
		v.addAll(A,S);
		
		p.add(new Production(S,A));
		p.add(new Production(A, new SymbolString()));
		for(Symbol terminal : getAlphabet()){
			if(!terminal.equals(myTerminal)){
				p.add(new Production(A, A, terminal));
			}
		}
		p.add(new Production(A, A,myTerminal,A, myTerminal,A));
	}

}
