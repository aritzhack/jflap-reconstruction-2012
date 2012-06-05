package model.grammar.parsing;

import errors.BooleanWrapper;
import util.Copyable;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.formaldef.Describable;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;


public abstract class Parser extends FormalDefinitionAlgorithm<Grammar>{


	private SymbolString myInput;
	
	public Parser(Grammar g){
		super(g);
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(Grammar g) {
		GrammarType type = this.getRequiredGrammarType();
		BooleanWrapper bw = new BooleanWrapper(true);
		if (!type.matches(g))
			 bw = new BooleanWrapper(false, "To use the " + this.getDescriptionName() +
					" the grammar must be in " + type.name);
		return new BooleanWrapper[]{bw};
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new DoParsingStep()};
	}

	public Grammar getGrammar(){
		return super.getOriginalDefinition();
	}
	
	public void setInput(SymbolString string){
		this.reset();
		myInput = string;
	}
	
	public SymbolString getCurrentInput(){
		return myInput;
	}
	
	public boolean isReject(){
		return !this.isAccept() && this.isDone();
	}

	public abstract boolean isAccept();

	public abstract boolean isDone();

	public abstract GrammarType getRequiredGrammarType() throws ParserException;
	
	protected abstract boolean stepParser();
	
	private class DoParsingStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Step Parser";
		}

		@Override
		public String getDescription() {
			return "Performs a single parsing step.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			if (getCurrentInput() == null)
				throw new AlgorithmException("You must set an input first before " +
						"running the parser. ");
			return stepParser();
		}

		@Override
		public boolean isComplete() {
			return isDone() ;
		}
		
	}
	
}
