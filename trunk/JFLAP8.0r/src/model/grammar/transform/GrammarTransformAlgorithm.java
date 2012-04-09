package model.grammar.transform;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.SteppableAlgorithm;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;

public abstract class GrammarTransformAlgorithm extends SteppableAlgorithm {


	private Grammar myOldGrammar;
	private Grammar myNewGrammar;

	public GrammarTransformAlgorithm(Grammar g){
		myOldGrammar = g;
		if (!g.isType(GrammarType.CONTEXT_FREE))
			throw new AlgorithmException("The grammar must be restriced on the " +
					"left hand side.");
		reset();
	}
	
	public Grammar getOriginalGrammar(){
		return myOldGrammar;
	}
	
	public Grammar getTransformedGrammar(){
		return myNewGrammar;
	}
	
	@Override
	public boolean reset() throws AlgorithmException {
		myNewGrammar = (Grammar) myOldGrammar.alphabetAloneCopy();
		return true;
	}

}
