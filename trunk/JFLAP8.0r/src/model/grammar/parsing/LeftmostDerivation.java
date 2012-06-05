package model.grammar.parsing;

import java.lang.reflect.Array;
import java.util.Arrays;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.util.UtilFunctions;

public class LeftmostDerivation extends Derivation {

	public LeftmostDerivation(Production start) {
		super(start);
	}
	
	public LeftmostDerivation(Production ...productions ){
		super(productions.length == 0 ? new Production() : productions[0]);
		if(productions.length>0){
			addAll(UtilFunctions.subArray(productions, 1, productions.length));
		}
	}
	
	@Override
	public boolean addStep(Production p, int subIndex) {
		//Hey guys, this method just calls addStep(p), so use that instead!
		return addStep(p);
	}

	public void addAll(Production ... p){
		Integer[] subs = new Integer[p.length];
		for(int i=0;i<subs.length;i++){
			subs[i] = 0;
		}
		addAll(p, subs);
	}
	
	
	public boolean addStep(Production p) {
		SymbolString current = createResult();
		for (int i = 0; i< current.size(); i++){
			if (Grammar.isVariable(current.get(i))){
				return super.addStep(p, i);
			}
				
		}
		return false;
	}
	
}
