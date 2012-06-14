package model.grammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import preferences.JFLAPPreferences;

import errors.BooleanWrapper;
import model.change.interactions.CustomModeSymbolModifiedInteraction;
import model.change.interactions.CustomModeSymbolRemovedInteraction;
import model.change.interactions.DefaultAddProductionInteraction;
import model.change.interactions.DefaultRemoveProductionInteraction;
import model.change.rules.FormalDefinitionRule;
import model.change.rules.GroupingRule;
import model.change.rules.IdenticalItemRule;
import model.change.rules.applied.CustomModeProductionSetRule;
import model.change.rules.applied.CustomModeStartVariableRule;
import model.change.rules.applied.DefaultModeInUseRule;
import model.change.rules.applied.DefaultModeStartSymbolRule;
import model.change.rules.applied.DisallowedCharacterRule;
import model.change.rules.applied.TerminalGroupingRule;
import model.change.rules.applied.TermsVersusVarsIdenticalRule;
import model.change.rules.applied.VariableGroupingRule;
import model.change.rules.applied.VarsVersusTermsIdenticalRule;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
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

	private GroupingPair myGrouping;

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
		if (!JFLAPPreferences.isUserDefinedMode()){
			this.getStartVariable().setTo(JFLAPPreferences.getDefaultStartVariable());
			this.getVariables().add(JFLAPPreferences.getDefaultStartVariable());
		}
	}
	
	public Grammar(){
		this(new VariableAlphabet(),
				new TerminalAlphabet(),
				new ProductionSet(),
				new StartVariable());
	}


	private void addGroupingPairRules(GroupingPair gp) {
		if (gp == null) return;
		VariableAlphabet vars = getVariables();
		vars.addRules(new VariableGroupingRule(ITEM_ADD, gp),
						new VariableGroupingRule(ITEM_MODIFY, gp));
		TerminalAlphabet terms =  getTerminals();
		terms.addRules(new TerminalGroupingRule(ITEM_ADD, gp),
										new TerminalGroupingRule(ITEM_MODIFY, gp));
		
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
		myGrouping = gp;
	}
	
	/**
	 * Retrieves the open group of the grouping pair in use
	 * or null if grouping is not in use.
	 * 
	 * @return
	 */
	public Character getOpenGroup(){
		if (myGrouping != null)
			return myGrouping.getOpenGroup();
		return null;
	}
	
	/**
	 * Retrieves the close group of the grouping pair in use
	 * or null if grouping is not in use.
	 * @return
	 */
	public Character getCloseGroup(){
		if (myGrouping != null)
			return myGrouping.getCloseGroup();
		return null;
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
	public StartVariable getStartVariable(){
		return getComponentOfClass(StartVariable.class);
	}
	
	
	
	/**
	 * Sets the StartVariable to the {@link Variable} v;
	 * @param s
	 */
	public boolean setStartVariable(Variable s){
		return this.getStartVariable().setTo(s);
	}

	@Override
	public Grammar alphabetAloneCopy() {
		return new Grammar(this.getVariables().copy(),
							this.getTerminals().copy(), 
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
			if (p.isStartProduction(this.getStartVariable().toSymbolObject()))
					startProds.add(p);
		}
		return startProds.toArray(new Production[0]);
	}

	public static boolean isVariable(Symbol first) {
		return first instanceof Variable;
	}

	public static boolean isTerminal(Symbol symbol) {
		return symbol instanceof Terminal;
	}

	@Override
	public Grammar copy() {
		return new Grammar(this.getVariables().copy(),
							this.getTerminals().copy(),
							this.getProductionSet().copy(),
							this.getStartVariable().copy());
	}

	public static boolean isStartVariable(Variable a, Grammar gram) {
		return gram.getStartVariable().equals(a);
	}
	
	public static boolean isStartProduction(Production p, Grammar g){
		SymbolString lhs = p.getLHS();
		return lhs.size() == 1 && lhs.contains(g.getStartVariable());
	}



	@Override
	public void setUpDefaultRulesAndInteractions() {
		super.setUpDefaultRulesAndInteractions();
		ProductionSet prods = this.getProductionSet();
		//add rules
		StartVariable start = getStartVariable();
		start.addRules(new DefaultModeStartSymbolRule());
		
		//add interactions
		prods.addInteractions(new DefaultAddProductionInteraction(this));
		prods.addInteractions(new DefaultRemoveProductionInteraction(this));
		
	}

	@Override
	public void setUpUserDefinedRulesAndInteractions() {
		VariableAlphabet vars = this.getVariables();
		TerminalAlphabet terms = this.getTerminals();
		ProductionSet prods = this.getProductionSet();
		StartVariable start = getStartVariable();
		
		start.addRules(new CustomModeStartVariableRule(vars));
		prods.addRules(new CustomModeProductionSetRule(ITEM_ADD, vars, terms),
						new CustomModeProductionSetRule(ITEM_MODIFY, vars, terms));
		vars.addInteractions(new CustomModeSymbolRemovedInteraction(this),
							new CustomModeSymbolModifiedInteraction(this));
		terms.addInteractions(new CustomModeSymbolRemovedInteraction(this),
					new CustomModeSymbolModifiedInteraction(this));
//		this.getTerminals().addRules(new TermsVersusVarsIdenticalRule(getVariables()));
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed(Alphabet alph) {
		ProductionSet prods = this.getProductionSet();
		if (alph instanceof VariableAlphabet){
			Set<Variable> vars = prods.getVariablesUsed();
			vars.add(this.getStartVariable().toSymbolObject());
			return new TreeSet<Symbol>(vars);
		}
		else if(alph instanceof TerminalAlphabet)
			return  new TreeSet<Symbol>(prods.getTerminalsUsed());
				
		return new HashSet<Symbol>();
	}


	

}
