package model.grammar;

import java.util.Set;

import errors.BooleanWrapper;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.alphabets.specific.TerminalAlphabet;
import model.formaldef.components.alphabets.specific.VariableAlphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.Variable;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.DisallowedCharacterRule;
import model.formaldef.rules.GroupingRule;
import model.formaldef.rules.TerminalGroupingRule;
import model.formaldef.rules.TermsVersusVarsIdenticalRule;
import model.formaldef.rules.VariableGroupingRule;
import model.formaldef.rules.VarsVersusTermsIdenticalRule;

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
public class Grammar extends FormalDefinition<TerminalAlphabet, ProductionSet> {

	private VariableAlphabet myVariableAlphabet;
	
	private StartVariable myStartVariable;
	
	/**
	 * Creates a {@link Grammar}with all of the necessary components.
	 * @param terminals = the initial {@link TerminalAlphabet}
	 * @param variables = the initial {@link VariableAlphabet}
	 * @param functions = set of {@link Production} rules
	 * @param startVar = the {@link StartVariable} of this grammar
	 */
	public Grammar(TerminalAlphabet terminals,
					VariableAlphabet variables,
					ProductionSet functions,
					StartVariable startVar) {
		super(terminals, functions);
		myVariableAlphabet = variables;
		myStartVariable = startVar;
		setUpRules();
	}
	
	public Grammar(){
		this(new TerminalAlphabet(),
				new VariableAlphabet(),
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
		this.getVariables().removeRule(varsRule);
		this.getTerminals().removeRule(termsRule);
	}

	public void setGrouping(GroupingPair gp) {
		clearGroupingPairRules();
		addGroupingPairRules(gp);
	}

	/**
	 * Retrieves the {@link VariableAlphabet} of this grammar
	 * @return
	 */
	public VariableAlphabet getVariables() {
		return myVariableAlphabet;
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
		return super.getLanguageAlphabet();
	}
	
	/**
	 * Returns the start variable for this grammar
	 * @return
	 */
	public StartVariable getStartVariable(){
		return myStartVariable;
	}
	
	/**
	 * Sets the StartVariable to the {@link Variable} v;
	 * @param s
	 */
	public void setStartVariable(Variable v){
		myStartVariable.setString(v.getString());
	}

	@Override
	public FormalDefinition<TerminalAlphabet, ProductionSet> alphabetAloneCopy() {
		return new Grammar(this.getTerminals(), 
							this.getVariables(),
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
		return super.getFunctionSet();
	}

	@Override
	public FormalDefinitionComponent[] getComponents() {
		return new FormalDefinitionComponent[]{
									this.getVariables(),
									this.getTerminals(), 
									this.getProductionSet(),
									this.getStartVariable()};
	}
	
	

}
