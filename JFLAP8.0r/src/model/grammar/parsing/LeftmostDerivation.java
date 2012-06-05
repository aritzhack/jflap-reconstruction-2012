package model.grammar.parsing;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;

public class LeftmostDerivation extends Derivation {

	public LeftmostDerivation(Production start) {
		super(start);
	}
	
	@Override
	public boolean addStep(Production p, int subIndex) {
		//Hey guys, this method just calls addStep(p), so use that instead!
		return addStep(p);
	}

	public void addAll(Production ... p){
		addAll(p, new Integer[p.length]);
	}
	
	
	private boolean addStep(Production p) {
		SymbolString current = createResult();
		for (int i = 0; i< current.size(); i++){
			if (Grammar.isVariable(current.get(i))){
				return super.addStep(p, i);
			}
				
		}
		return false;
	}
	
}
