package model.lsystem.formaldef;

import model.formaldef.components.FormalDefinitionComponent;
import model.grammar.Grammar;
import errors.BooleanWrapper;

public class FormalGrammarComponent extends FormalDefinitionComponent {

	private Grammar grammar;

	public FormalGrammarComponent(Grammar g){
		grammar = g;
	}
	
	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return "Grammar";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Formal Definition Component Wrapper for Grammar";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return 'G';
	}

	@Override
	public BooleanWrapper isComplete() {
		for(BooleanWrapper b : grammar.isComplete())
			if(b.isError())
				return b;
		return new BooleanWrapper(true);
	}

	@Override
	public FormalDefinitionComponent copy() {
		// TODO Auto-generated method stub
		return new FormalGrammarComponent(grammar.copy());
	}

	@Override
	public void clear() {
		grammar = new Grammar();
	}

}
