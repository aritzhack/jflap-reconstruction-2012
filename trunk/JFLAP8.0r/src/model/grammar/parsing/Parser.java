package model.grammar.parsing;

import errors.BooleanWrapper;
import util.Copyable;
import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.SteppableAlgorithm;
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
	public boolean reset() throws AlgorithmException {
		return setInput(null);
	}
	
	/**
	 * Returns this parser to its base state without changing
	 * the currently set input. Can be used if one would
	 * like to restart the parser, but not change what it is
	 * parsing. 
	 * 
	 * NOTE: call the {@link Parser}.reset method to reset the
	 * input string (to <code>null</code>) and the parser state.
	 * 
	 * @return
	 * 		true if everything went ok in the reset.
	 */
	public abstract boolean resetParserStateOnly();

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new DoParsingStep()};
	}

	public Grammar getGrammar(){
		return super.getOriginalDefinition();
	}
	
	public boolean setInput(SymbolString string){
		myInput = string;
		return resetParserStateOnly();
	}
	
	public SymbolString getCurrentInput(){
		return myInput;
	}
	
	public boolean isReject(){
		return !this.isAccept() && this.isDone();
	}

	/**
	 * Returns true if this parser is accept. This conditions
	 * varies based on the parser.
	 * @return
	 */
	public abstract boolean isAccept();

	/**
	 * 
	 * @return
	 */
	public abstract boolean isDone();

	public abstract GrammarType getRequiredGrammarType() throws ParserException;
	
	/**
	 * This will execute another step in the parser. It is 
	 * more or less the same as calling {@link SteppableAlgorithm}.step
	 * but does not require the same "isComplete" case in the
	 * {@link AlgorithmStep} class.
	 * 
	 * @return
	 */
	public abstract boolean stepParser();
	
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
