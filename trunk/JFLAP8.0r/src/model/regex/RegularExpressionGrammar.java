package model.regex;

import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.TerminalAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;

/**
 * Creates a basic Regular Expression grammar with
 * Productions as follows:
 * 
 * 	EXPRESSION 	--> ( EXPRESSION )
 * 	EXPRESSION 	--> EXPRESSION + EXPRESSION
 * 	EXPRESSION  --> EXPRESSION *
 * 	EXPRESSION 	--> EXPRESSION EXPRESSION
 * 	EXPRESSION	--> a 
 * 
 * 		where a is any terminal.
 * 
 * 
 * @author Julian
 *
 */
public class RegularExpressionGrammar extends Grammar {

	private final Variable EXPRESSION = new Variable("<Expression>");
	
	public RegularExpressionGrammar(InputAlphabet alph,
			OperatorAlphabet ops) {
		TerminalAlphabet terms = this.getTerminals();
		terms.addAll(alph);
		terms.addAll(ops);
		this.setVariableGrouping(new GroupingPair('<', '>'));
		this.getVariables().addAll(EXPRESSION);
		addProduction(ops.getOpenGroup(), EXPRESSION, ops.getCloseGroup());
		addProduction(EXPRESSION, ops.getUnionOperator(), EXPRESSION);
		addProduction(EXPRESSION, ops.getKleeneStar());
		addProduction(EXPRESSION, EXPRESSION);
		for (Symbol s: terms){
			addProduction(s);
		}
	}

	private void addProduction(Symbol ... rhs) {
		
		Production p = new Production(new SymbolString(EXPRESSION),
										new SymbolString(rhs));
		this.getProductionSet().add(p);
	}

}
