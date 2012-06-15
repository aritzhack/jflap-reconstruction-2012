package model.regex;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import oldnewstuff.universe.preferences.JFLAPPreferences;
import util.UtilFunctions;


import debug.JFLAPDebug;
import errors.BooleanWrapper;

import model.automata.InputAlphabet;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.UsesSymbols;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.parsing.brute.RestrictedBruteParser;
import model.grammar.transform.CNFConverter;
import model.regex.operators.OpenGroup;
import model.regex.operators.Operator;

public class RegularExpression extends FormalDefinition {

	private SymbolString myRegEx;
	private RegularExpressionGrammar myGrammar;
	private OperatorAlphabet myOperatorAlphabet;
	
	public RegularExpression(InputAlphabet alph){
		super(alph);
		myOperatorAlphabet = new OperatorAlphabet();
		myRegEx = new SymbolString();
		myGrammar = new RegularExpressionGrammar(alph, myOperatorAlphabet);
//		myGrammar  = createRegexGrammar(alph);
		
	}
	
	
//	private RegularExpressionGrammar createRegexGrammar(InputAlphabet alph) {
//		Grammar g = new RegularExpressionGrammar(alph, myOperatorAlphabet);
//		CNFConverter conv = new CNFConverter(g);
//		conv.stepToCompletion();
//		return (RegularExpressionGrammar) conv.getTransformedGrammar();
//		
//	}


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
		in = in.replaceAll(JFLAPPreferences.getEmptyStringSymbol(),
				myOperatorAlphabet.getEmptySub().getString());

		if (!SymbolString.canBeParsed(in, myGrammar)){
			throw new RegularExpressionException("Invalid input. This string contains symbols" +
					" that are neither input symbols or operators");
			
		}
		
		SymbolString s = SymbolString.createFromString(in, myGrammar);
		
		setTo(s);
		

		
	}
	

	public synchronized void setTo(SymbolString s) {
		//check all symbols - maybe be redundant if called with setTo(String)
		if (!myGrammar.getTerminals().containsAll(s)){
			throw new RegularExpressionException("The input string must contain symbols " +
					"from the " + this.getInputAlphabet().getDescriptionName() + " or the " +
					this.getOperators().getDescriptionName() +".");
		}
			
		//check syntax

		BooleanWrapper format = correctFormat(s);
		if (format.isError()){
			throw new RegularExpressionException(format.getMessage());
		}
	
		myRegEx = s;

	}

//	/**
//	 * Ideal version of the Correct format method. Uses the JFLAP 
//	 * Infrastructure to parse the input.
//	 * 
//	 * @param exp
//	 * @return
//	 */
//	private BooleanWrapper correctFormat(SymbolString exp){
//		RestrictedBruteParser parser = new RestrictedBruteParser(myGrammar);
//		parser.parse(exp);
//		parser.start();
//		while(parser.isActive()){
//		}
//		boolean isInLanguage = parser.getAnswer() != null;
//		return new BooleanWrapper(isInLanguage, "The input regex, " + exp + " is invalid. " +
//				"This means it is not in the language of the JFLAP regular expression grammar.");
//	}
	
	/**
	 * A temporary solution to the inefficiency of the Brute parser
	 * and non-optimized expression grammar.
	 * 
	 * @param exp
	 * @return true if the exp is properly formatted
	 */
	private BooleanWrapper correctFormat(SymbolString exp){
		if (exp.size() == 0)
			return new BooleanWrapper(false,
					"The expression must be nonempty.");
		if (!isGroupingBalanced(exp))
			return new BooleanWrapper(false,
					"The parentheses are unbalanced!");
		
		Symbol star = myOperatorAlphabet.getKleeneStar();
		Symbol open = myOperatorAlphabet.getOpenGroup();
		Symbol close = myOperatorAlphabet.getCloseGroup();
		Symbol union = myOperatorAlphabet.getUnionOperator();
		Symbol empty = myOperatorAlphabet.getEmptySub();
		BooleanWrapper poorFormat = new BooleanWrapper(false,
				"Operators are poorly formatted.");
		
		Symbol c = exp.getFirst();
		if (c.equals(star))
			return poorFormat;
		
		Symbol p = c;
		for (int i = 1; i < exp.size(); i++) {
			c = exp.get(i);
			
			if (c.equals(union) && i == exp.size()-1){
				return poorFormat;
			}
			else if( c.equals(star) &&
						(p.equals(open)|| p.equals(union))){
				return poorFormat;
			}
			else if(c.equals(empty) ){
				BooleanWrapper badLambda = new BooleanWrapper(false,
						"Lambda character must not cat with anything else.");
				if (!(p.equals(open) || p.equals(union)))
					return badLambda;
				if (i == exp.size() - 1)
					continue;
				p = exp.get(i+1);
				if (!(p.equals(close) || p.equals(union) || p.equals(star)))
					return badLambda;
			}
			p=c;
		}
		return new BooleanWrapper(true);
	}
	
	/**
	 * Checks if the parentheses are balanced in a string.
	 * 
	 * @param string
	 *            the string to check
	 * @return if the parentheses are balanced
	 */
	private boolean isGroupingBalanced(SymbolString exp) {
		int count = 0;
		for (int i = 0; i < exp.size(); i++) {
			if (exp.get(i).equals(myOperatorAlphabet.getOpenGroup()))
				count++;
			else if (exp.get(i).equals(myOperatorAlphabet.getCloseGroup()))
				count--;
			if (count < 0)
				return false;
		}
		return count == 0;
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
		temp = temp.replaceAll(ops.getUnionOperator(),new Symbol("|"));
		return temp.toNondelimitedString();

	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		SymbolString temp = getExpression();
		temp.removeAll(myOperatorAlphabet);
		return new TreeSet<Symbol>(temp);
	}
	
	@Override
	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
		if (a instanceof InputAlphabet)
			return myRegEx.removeEach(s);
		return super.purgeOfSymbol(a, s);
	}
	
	@Override
	public String toString() {
		return super.toString() + 
				"\n\t Regular Expression: " + myRegEx +
				"\n\t" + myGrammar.toString();
	}
	

	public SymbolString getExpression() {
		return new SymbolString(myRegEx);
	}
	
	@Override
	public BooleanWrapper[] isComplete() {
		
		BooleanWrapper[] comp = super.isComplete();
		
		if (comp.length == 0){
			BooleanWrapper bw = new BooleanWrapper(derivesSomething(), 
					"This regular Expression does not derive any strings.");
			if (bw.isError())
				comp = new BooleanWrapper[]{bw};
		}

		
		return comp;
	}


	private boolean derivesSomething() {
		SymbolString exp = new SymbolString(getExpression());
		exp.removeAll(getOperators());
		return !exp.isEmpty();
	}

	/**
	 * Retrieves the first operand from the input SymbolString.
	 * 
	 * @param input
	 * @return
	 */
	public static SymbolString getFirstOperand(SymbolString input,
			OperatorAlphabet ops) {
		if (input.isEmpty())
			return input;
		int n = 0;
		int i = 0;
		for (; i < input.size(); i++){
			Symbol s = input.get(i);
			if (ops.getOpenGroup().equals(s))
				n++;
			else if(ops.getCloseGroup().equals(s)) 
				n--;
			
			if (n == 0)
				break;
		}
		
		if (i < input.size()-1 && 
				input.get(i+1).equals(ops.getKleeneStar())){
			i++;
		}
		return input.subList(0,i+1);
	}


	@Override
	public RegularExpression copy() {
		RegularExpression exp = this.alphabetAloneCopy();
		exp.setTo(this.myRegEx);
		return exp;
	}
	
}
