package model.grammar.parsing;

import util.Copyable;
import errors.BooleanWrapper;
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
		BooleanWrapper[] bw = new BooleanWrapper[0];
		if (!type.matches(g))
			 bw = new BooleanWrapper[]{new BooleanWrapper(false, "To use the " + this.getDescriptionName() +
					" the grammar must be in " + type.name)};
		return bw;
	}

	@Override
	public boolean reset() throws AlgorithmException {
		return setInput(null);
	}
	
	/**
	 * Does a quick parse of the input string. This method
	 * automatically sets the input, and then calls the 
	 * {@link SteppableAlgorithm} method <code>stepToCompletion</code>.
	 * 
	 * If the input was accepted, returns true. Otherwise returns false,
	 * although this may not necessarily mean that the string was 
	 * outright rejected as some parsers may be "done" as a result of
	 * reaching a complexity limit. 
	 *  
	 * @param input
	 * 		Symbol String to parse
	 * @return
	 * 		true if the string is in the language of the associated grammar
	 * 			false otherwise.
	 */
	public boolean quickParse(SymbolString input){
		setInput(input);
		stepToCompletion();
		return this.isAccept();
	}
	
	/**
	 * Returns this parser to its base state without changing
	 * the currently set input. Can be used if one would
	 * like to restart the parser, but not change what it is
	 * parsing. This method is automatically called when the
	 * input in the parser changes.
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
		if (!isValidInput(string))
			throw new ParserException("The string must not contain non-terminal symbols.");
			myInput = string;
		return resetParserStateOnly();
	}

	public boolean isValidInput(SymbolString string) {
		
		return string == null || getGrammar().getTerminals().containsAll(string);
	}
	
	public SymbolString getInput(){
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
	
	/**
	 * Retrieves a derivation from this parser. Depending
	 * on the current state of this parser, what the 
	 * derivation represents may vary.
	 * 
	 * @return
	 * 		A {@link Derivation} relating to this parser.
	 */
	public abstract Derivation getDerivation();
	
	
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
			if (getInput() == null)
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
