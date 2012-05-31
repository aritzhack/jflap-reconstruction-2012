package model.grammar.transform;

import java.util.ArrayList;
import java.util.List;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.SteppableAlgorithm;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;

public abstract class GrammarTransformAlgorithm extends FormalDefinitionAlgorithm<Grammar> {


	private Grammar myNewGrammar;

	public GrammarTransformAlgorithm(Grammar g){
		super(g);
	}
	
	public Grammar getOriginalGrammar(){
		return getOriginalDefinition();
	}
	
	public Grammar getTransformedGrammar(){
		return myNewGrammar;
	}
	
	@Override
	public boolean reset() throws AlgorithmException {
		myNewGrammar = (Grammar) getOriginalGrammar().alphabetAloneCopy();
		return true;
	}
	
	@Override
	public BooleanWrapper[] checkOfProperForm(Grammar g) {
		List<BooleanWrapper> bw = new ArrayList<BooleanWrapper>();
		if (!g.isType(GrammarType.CONTEXT_FREE))
			bw.add(new BooleanWrapper(false,"The grammar must be restriced on the " +
					"left hand side."));
		return bw.toArray(new BooleanWrapper[0]);
	}

}
