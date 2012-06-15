package model.languages.samplelanguages;

import model.formaldef.components.symbols.*;
import model.grammar.*;
import model.languages.*;

public class SigmaStarLanguage extends Language {

	public SigmaStarLanguage(TerminalAlphabet alpha) {
		super(alpha);
		
	}

	@Override
	public void createVariablesAndProductions(VariableAlphabet v, ProductionSet p) {
		Variable S = new Variable("S"), A = new Variable("A");
		v.addAll(A,S);
		
		p.add(new Production(S,A));
		p.add(new Production(A, new SymbolString()));
		for(Symbol terminal : getAlphabet()){
			p.add(new Production(A, A, terminal));
		}
	}

}
