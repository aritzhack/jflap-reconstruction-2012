package model.grammar;

import java.util.Arrays;
import java.util.Set;

import errors.BooleanWrapper;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Variable;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.GroupingRule;
import model.formaldef.rules.applied.DisallowedCharacterRule;
import model.formaldef.rules.applied.TerminalGroupingRule;
import model.formaldef.rules.applied.TermsVersusVarsIdenticalRule;
import model.formaldef.rules.applied.VariableGroupingRule;
import model.formaldef.rules.applied.VarsVersusTermsIdenticalRule;
import model.grammar.typetest.GrammarType;

/**
 * An object representing the formal 4-tuple that represents
 * a grammar. This includes the following {@link FormalDefinitionComponent}s:
 * 		{@link TerminalAlphabet}
 * 		{@link VariableAlphabet}
 * 		{@link ProductionSet}
 * 		{@link StartVariable}
 * 
 * Each of these is accessible from the 
 * 
 * @author Julian Genkins
 *
 */
public class Grammar extends FormalDefinition{

	private StartVariable myStartVariable;

	/**
	 * Creates a {@link Grammar}with all of the necessary components.
	 * @param terminals = the initial {@link TerminalAlphabet}
	 * @param variables = the initial {@link VariableAlphabet}
	 * @param functions = set of {@link Production} rules
	 * @param startVar = the {@link StartVariable} of this grammar
	 */
	public Grammar(VariableAlphabet variables,
					TerminalAlphabet terminals,
					ProductionSet functions,
					StartVariable startVar) {
		super(variables, terminals, functions, startVar);
		myStartVariable = startVar;
		setUpRules();
	}
	
	public Grammar(){
		this(new VariableAlphabet(),
				new TerminalAlphabet(),
				new ProductionSet(),
				new StartVariable());
	}

	private void setUpRules() {
		DisallowedCharacterRule disallowed = new DisallowedCharacterRule(this);
		this.getVariables().addRules(disallowed, new VarsVersusTermsIdenticalRule(getTerminals()));
		this.getTerminals().addRules(disallowed, new TermsVersusVarsIdenticalRule(getVariables()));
	}

	private void addGroupingPairRules(GroupingPair gp) {
		if (gp == null) return;
		
		this.getVariables().addRules(new VariableGroupingRule(gp));
		this.getTerminals().addRules(new TerminalGroupingRule(gp));
		
	}

	private void clearGroupingPairRules() {
		GroupingRule varsRule = this.getVariables().getRuleOfClass(GroupingRule.class);
		GroupingRule termsRule = this.getTerminals().getRuleOfClass(GroupingRule.class);
		if (varsRule != null)
			this.getVariables().removeRule(varsRule);
		if (termsRule != null)
			this.getTerminals().removeRule(termsRule);
	}

	public void setVariableGrouping(GroupingPair gp) {
		clearGroupingPairRules();
		addGroupingPairRules(gp);
	}
	
	/**
	 * Retrieves the {@link VariableAlphabet} of this grammar
	 * @return
	 */
	public VariableAlphabet getVariables() {
		return getComponentOfClass(VariableAlphabet.class);
	}

	@Override
	public String getDescriptionName() {
		return "Grammar";
	}

	@Override
	public String getDescription() {
		
		return "Hey look, its a Grammar!";
	}

	/**
	 * Returns the {@link TerminalAlphabet} of this grammar. The
	 * {@link Alphabet} returned is identical to that of the 
	 * {@link Alphabet.getLanguageAlphabet()} method.
	 * @return
	 */
	public TerminalAlphabet getTerminals() {
		return getComponentOfClass(TerminalAlphabet.class);
	}
	
	/**
	 * Returns the start variable for this grammar
	 * @return
	 */
	public Variable getStartVariable(){
		return getComponentOfClass(StartVariable.class).toSymbolObject();
	}
	
	/**
	 * Sets the StartVariable to the {@link Variable} v;
	 * @param s
	 */
	public void setStartVariable(Symbol s){
		myStartVariable.setTo(s);
	}

	@Override
	public FormalDefinition alphabetAloneCopy() {
		return new Grammar(this.getVariables(),
							this.getTerminals(), 
							new ProductionSet(),
							new StartVariable());
	}
	
	/**
	 * Returns the set of products in the from of a {@link ProductionSet} object.
	 * Calling this methods is equivalent to calling <code>getFunctionSet()</code>.
	 * 
	 * @return
	 */
	public ProductionSet getProductionSet(){
		return getComponentOfClass(ProductionSet.class);
	}

	/**
	 * Returns true if and only if the variable alphabet has a grouping rule,
	 * signifying that it is using a grouping pair!
	 * 
	 * @return
	 */
	public boolean usingGrouping() {
		return this.getVariables().getRuleOfClass(GroupingRule.class) != null;
	}

	public boolean isType(GrammarType type) {
		GrammarType[] myTypes = GrammarType.getType(this);
		for (GrammarType gt : myTypes){
			if (gt == type)
				return true;
		}
		return false;
	}

	public Production[] getStartProductions() {
		ProductionSet startProds = new ProductionSet();
		for (Production p : this.getProductionSet()){
			if (p.isStartProduction(this.getStartVariable()))
					startProds.add(p);
		}
		return startProds.toArray(new Production[0]);
	}

	

}