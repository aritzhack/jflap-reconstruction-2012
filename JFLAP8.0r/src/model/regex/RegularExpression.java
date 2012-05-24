package model.regex;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import debug.JFLAPDebug;

import model.automata.InputAlphabet;
import model.formaldef.FormalDefinition;
import model.formaldef.UsesSymbols;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.parsing.brute.RestrictedBruteParser;
import model.regex.operators.OpenGroup;
import model.regex.operators.Operator;
import model.util.UtilFunctions;

public class RegularExpression extends FormalDefinition {

	private SymbolString myRegEx;
	private RegularExpressionGrammar myGrammar;
	private OperatorAlphabet myOperatorAlphabet;
	
	public RegularExpression(InputAlphabet alph){
		super(alph);
		myOperatorAlphabet = new OperatorAlphabet();
		myRegEx = new SymbolString();
		myGrammar = new RegularExpressionGrammar(alph, myOperatorAlphabet);
	}
	
	
	public InputAlphabet getInputAlphabet(){
		return this.getComponentOfClass(InputAlphabet.class);
	}
	
	@Override
	public String getDescriptionName() {
		return "Regular Expression";
	}

	@Override
	public String getDescription() {
		return "A regular expression.";
	}

	public OperatorAlphabet getOperators() {
		return myOperatorAlphabet;
	}


	@Override
	public RegularExpression alphabetAloneCopy() {
		return new RegularExpression(getInputAlphabet());
	}
	
	public void setTo(String in){
		if (!SymbolString.canBeParsed(in, myGrammar)){
			throw new RegularExpressionException("Invalid input. This string contains symbols" +
					" that are neither input symbols or operators");
			
		}
		
		SymbolString s = SymbolString.createFromString(in, myGrammar);
		
		setTo(s);
		

		
	}
	

	public void setTo(SymbolString s) {
		
		//check all symbols - maybe be redundant if called with setTo(String)
		if (!myGrammar.getTerminals().containsAll(s)){
			throw new RegularExpressionException("The input string must contain symbols " +
					"from the " + this.getInputAlphabet().getDescriptionName() + " or the " +
					this.getOperators().getDescriptionName() +".");
		}
			
		//check syntax
		RestrictedBruteParser parser = new RestrictedBruteParser(myGrammar);
		parser.init(s);
		parser.start();
		if (parser.getAnswer() == null){
			throw new RegularExpressionException("The input string is not formatted correctly.");
		}
	
		myRegEx = s;

	}


	public String getExpressionString(){
		return myRegEx.toString();
	}
	
	public boolean matches(String in){
		Pattern p = convertToPattern();
		return p.matcher(in).matches();
	}


	public Pattern convertToPattern() {
		
		return RegularExpression.convertToPattern(this);
	}

	public static Pattern convertToPattern(RegularExpression regEx) {
		
		return Pattern.compile(convertToStringPattern(regEx));
	}


	/**
	 * Will convert a regular expression object into a Java String object
	 * with the correct syntax to be converted into a Pattern object.
	 * @param regEx
	 * @return
	 */
	public static String convertToStringPattern(RegularExpression regEx) {
		OperatorAlphabet ops = regEx.getOperators();
		SymbolString temp = regEx.getExpression();
		temp.replaceAll(ops.getUnionOperator(),new Symbol("|"));
		return temp.toNondelimitedString();

	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		SymbolString temp = getExpression();
		temp.removeAll(myOperatorAlphabet);
		return new TreeSet<Symbol>(temp);
	}
	
	@Override
	public boolean purgeOfSymbol(Symbol s) {
		return myGrammar.inputSymbolRemoved(s) ||
				myRegEx.purgeOfSymbol(s) || super.purgeOfSymbol(s);
	}
	
	@Override
	public String toString() {
		return super.toString() + 
				"\n\t Regular Expression: " + myRegEx +
				"\n\t" + myGrammar.toString();
	}
	
	@Override
	public void componentChanged(ComponentChangeEvent event) {
		if (event.comesFrom(getInputAlphabet())){
			switch (event.getType()){
			case ALPH_SYMBOL_MODIFY: break;
				//propagate modify to regex grammar if need be.
			case ITEM_ADDED:
				myGrammar.inputSymbolAdded((Symbol)event.getArg(0));
				break;
			}
		}
		super.componentChanged(event);
	}


	public SymbolString getExpression() {
		return new SymbolString(myRegEx);
	}
	
}
