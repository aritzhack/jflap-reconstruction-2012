package model.regex;

import java.util.LinkedList;
import java.util.regex.Pattern;

import model.automata.InputAlphabet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.regex.operators.OpenGroup;
import model.regex.operators.Operator;
import model.util.UtilFunctions;

public class RegularExpression extends FormalDefinition {

	private SymbolString myRegEx;
	private RegularExpressionGrammar myGrammar;
	
	public RegularExpression(InputAlphabet alph, OperatorAlphabet operators){
		super(alph, operators);
		myRegEx = new SymbolString();
		myGrammar = new RegularExpressionGrammar(alph, operators);
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
		return this.getComponentOfClass(OperatorAlphabet.class);
	}


	@Override
	public RegularExpression alphabetAloneCopy() {
		return new RegularExpression(getInputAlphabet(),
										new OperatorAlphabet());
	}
	
	public void setTo(String in){
		if (!SymbolString.canBeParsed(in, this)){
			throw new RegularExpressionException("Invalid input. This string contains symbols" +
					" that are neither input symbols or operators");
			
		}
		
		SymbolString s = SymbolString.createFromString(in, this);
		
		//TODO: create parseAlg and then run it
		
//		if (!parseAlg.quickParse(s)){
//			throw new RegularExpressionException("Invalid input. This string contains symbols" +
//					" that are neither input symbols or operators");
//		}
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


	public static String convertToStringPattern(RegularExpression regEx) {
		OperatorAlphabet ops = regEx.getOperators();
		InputAlphabet inputAlph = regEx.getInputAlphabet();

		LinkedList<String> operands = new LinkedList<String>();
		LinkedList<Symbol> operators = new LinkedList<Symbol>();
		
		SymbolString temp = new SymbolString(regEx.myRegEx);
		SymbolString stack = new SymbolString();
		while (!temp.isEmpty()){
			Symbol next = temp.pop();
			
			if (inputAlph.contains(next) ||
					next.equals(ops.getKleeneStar())){
				String s = operands.pop();
				s += next.toString();
				operands.push(s);
			}
			else if (next.equals(ops.getOpenGroup()) ||
						next.equals(ops.getUnionOperator())){
				operators.push(next);
				operands.push("");
			}
			else if(next.equals(ops.getCloseGroup())){
				while (operators.pop().equals(ops.getUnionOperator())){
					String s = "(" + operands.pop() + ")";
					s += "(" + operands.pop() + ")";
					operands.push(s);
				}
				String s = "([" + operands.pop() + "])";
				operands.push(s);
			}
		}
		return operands.pop();
	}

	
}
