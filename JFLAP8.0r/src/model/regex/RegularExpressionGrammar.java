package model.regex;

import oldnewstuff.main.JFLAP;
import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.TerminalAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;

/**
 * Creates a basic Regular Expression grammar with
 * Productions as follows:
 * 	START --> EXPRESSION
 * 	EXPRESSION 	--> ( EXPRESSION )
 * 	EXPRESSION 	--> EXPRESSION + EXPRESSION
 * 	EXPRESSION  --> EXPRESSION *
 * 	EXPRESSION 	--> EXPRESSION EXPRESSION
 * 	EXPRESSION	--> a 
 * 
 * 		where a is any terminal.
 * 
 * I imagine this could be optimized.
 * 
 * @author Julian
 *
 */
public class RegularExpressionGrammar extends Grammar {

	private final Variable EXPRESSION = new Variable("<Expression>");
	private final Variable START = new Variable("<Start>");
	private InputAlphabet myInputAlph;
	private OperatorAlphabet myOperatorAlph;
	
	public RegularExpressionGrammar(InputAlphabet alph,
			OperatorAlphabet ops) {
		myInputAlph = alph;
		myInputAlph.addListener(this);
		myOperatorAlph = ops;
		TerminalAlphabet terms = this.getTerminals();
		terms.addAll(ops);
		this.setVariableGrouping(new GroupingPair('<', '>'));
		this.getVariables().addAll(START, EXPRESSION);
		this.setStartVariable(START);
		this.getProductionSet().add(new Production(START, EXPRESSION));
		addProduction(ops.getOpenGroup(), EXPRESSION, ops.getCloseGroup());
		addProduction(EXPRESSION, ops.getUnionOperator(), EXPRESSION);
		addProduction(EXPRESSION, ops.getKleeneStar());
		addProduction(EXPRESSION, EXPRESSION);
		addProduction(ops.getEmptySub());
		for (Symbol s: alph){
			inputSymbolAdded(s);
		}
	}

	private boolean addProduction(Symbol ... rhs) {
		
		Production p = new Production(new SymbolString(EXPRESSION),
										new SymbolString(rhs));
		return this.getProductionSet().add(p);
	}
	
	@Override
	public String getDescriptionName() {
		return "Regular Expression " + super.getDescriptionName();
	}

	/**
	 * Removes the input symbol s from the terminal alphabet
	 * and from the production set. This is to ensure that the 
	 * {@link RegularExpressionGrammar} has alphabets and productions
	 * which mirror that of the {@link RegularExpression} object itself
	 * 
	 * @param s
	 * @return
	 */
	public boolean inputSymbolRemoved(Symbol s){
		return removeProductionForSymbol(s) &&
				this.getTerminals().remove(s) ;
		
	}
	
	/**
	 * Removes the production associated with the given input
	 * symbol s that has been removed from the input alphabet.
	 * This means any production of the form:
	 * 
	 * 		EXPRESSION ---> s
	 * 
	 * @param s
	 * @return
	 */
	private boolean removeProductionForSymbol(Symbol s) {
		ProductionSet productions = this.getProductionSet();
		for (Production p: productions){
			if (p.getRHS().contains(s)){
				return productions.remove(p);
			}
		}
		return false;
	}

	/**
	 * Adds the input symbol s to the terminal alphabet
	 * and add the correct production to the production set. 
 	 * is to ensure that the {@link RegularExpressionGrammar} 
 	 * has alphabets and productions which mirror that of the 
 	 * {@link RegularExpression} object itself.
	 * 
	 * @param s
	 * @return
	 */
	public boolean inputSymbolAdded(Symbol s){
		TerminalAlphabet terms = this.getTerminals();
		return terms.add(s) &&
				addProductionForSymbol(terms.getByString(s.getString()));
		
	}

	private boolean addProductionForSymbol(Symbol s) {
		return addProduction(s);
	}
	
	@Override
	public Grammar alphabetAloneCopy() {
		
		return new RegularExpressionGrammar(myInputAlph, myOperatorAlph);
	}
	
	@Override
	public RegularExpressionGrammar copy(){
		RegularExpressionGrammar g = (RegularExpressionGrammar) super.copy();
		ProductionSet prods = g.getProductionSet();
		prods.clear();
		prods.addAll(this.getProductionSet());
		return g;
	}

	@Override
	public void componentChanged(ComponentChangeEvent event) {
		if (event.comesFrom(myInputAlph)){
			switch (event.getType()){
			case ALPH_SYMBOL_MODIFY: break;
				//propagate modify to regex grammar if need be.
			case ITEM_ADDED:
				this.inputSymbolAdded((Symbol)event.getArg(0));
				break;
			case ITEM_REMOVED:
				this.inputSymbolRemoved((Symbol)event.getArg(0));
				break;
			}
		}
		super.componentChanged(event);
	}
}
