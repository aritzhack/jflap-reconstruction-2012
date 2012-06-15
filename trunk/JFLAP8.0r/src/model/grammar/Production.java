package model.grammar;

import java.util.Set;
import java.util.TreeSet;

import util.JFLAPConstants;



import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;

public class Production implements LanguageFunction<Production>, JFLAPConstants{

	/** the left hand side of the production. */
	private SymbolString myLHS;

	/** the right hand side of the production. */
	private SymbolString myRHS;

	/**
	 * Creates an instance of <CODE>Production</CODE>.
	 * 
	 * @param lhs
	 *            the left hand side of the production rule.
	 * @param rhs
	 *            the right hand side of the production rule.
	 */
	public Production(SymbolString lhs, SymbolString rhs) {
		myLHS = lhs;
		myRHS = rhs;
	}

	public Production(){
		this(new SymbolString(), new SymbolString());
	}
	
	public Production(Symbol lhs, Symbol ... rhs) {
		this(new SymbolString(lhs), new SymbolString(rhs));
	}

	public Production(Symbol lhs, SymbolString rhs) {
		this(new SymbolString(lhs), rhs);

	}

	/**
	 * Sets the right hand side of production to <CODE>rhs</CODE>.
	 * 
	 * @param rhs
	 *            the right hand side
	 */
	public boolean setRHS(SymbolString rhs) {
		checkBadSymbols(rhs);
		return myRHS.setTo(rhs);
	}

	/**
	 * Sets the left hand side of production to <CODE>lhs</CODE>.
	 * 
	 * @param lhs
	 *            the left hand side
	 */
	public boolean setLHS(SymbolString lhs) {
		checkBadSymbols(lhs);
		return myLHS.setTo(lhs);
	}

	private void checkBadSymbols(SymbolString lhs) {
		if (containsBadSymbol(lhs))
			throw new ProductionException("The SymbolString set as the LHS or RHS " +
					"in a production cannot contain non-terminal/non-variable " +
					"symbols.");
	}

	private boolean containsBadSymbol(SymbolString side) {
		for (Symbol s: side){
			if (!(Grammar.isTerminal(s) ||
					Grammar.isVariable(s)))
				return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of the left hand side of the production
	 * rule.
	 * 
	 * @return a string representation of the lhs.
	 */
	public SymbolString getLHS() {
		return myLHS;
	}

	/**
	 * Returns a string representation of the right hand side of the production
	 * rule.
	 * 
	 * @return a string representation of the rhs.
	 */
	public SymbolString getRHS() {
		return myRHS;
	}

	/**
	 * Returns all variables in the production.
	 * 
	 * @return all variables in the production.
	 */
	public Set<Variable> getVariablesUsed() {
		TreeSet<Variable> results = new TreeSet<Variable>(this.getVariablesOnLHS());
		results.addAll(this.getVariablesOnRHS());
		return results;
	}

	/**
	 * Returns all variables on the left hand side of the production.
	 * 
	 * @return all variables on the left hand side of the production.
	 */
	public Set<Variable> getVariablesOnLHS() {
		return getLHS().getSymbolsOfClass(Variable.class);
	}

	/**
	 * Returns all variables on the right hand side of the production.
	 * 
	 * @return all variables on the right hand side of the production.
	 */
	public Set<Variable> getVariablesOnRHS() {
		return getRHS().getSymbolsOfClass(Variable.class);
	}

	/**
	 * Returns all terminals in the production.
	 * 
	 * @return all terminals in the production.
	 */
	public Set<Terminal> getTerminalsUsed() {
		TreeSet<Terminal> results = new TreeSet<Terminal>(this.getTerminalsOnLHS());
		results.addAll(this.getTerminalsOnRHS());
		return results;
	}

	/**
	 * Returns all terminals on the right hand side of the production.
	 * 
	 * @return all terminals on the right hand side of the production.
	 */
	public Set<Terminal> getTerminalsOnRHS() {
		return getRHS().getSymbolsOfClass(Terminal.class);
	}

	/**
	 * Returns true if <CODE>production</CODE> is equivalent to this
	 * production (i.e. they have identical left and right hand sides).
	 * 
	 * @param production
	 *            the production being compared to this production
	 * @return true if <CODE>production</CODE> is equivalent to this
	 *         production (i.e. they have identical left and right hand sides).
	 */
	public boolean equals(Object production) {
		if (production instanceof Production) {
			Production p = (Production) production;
			return getRHS().equals(p.getRHS()) && getLHS().equals(p.getLHS());
		}
		return false;
	}

	/**
	 * Returns a hashcode for this production.
	 * 
	 * @return the hashcode for this production
	 */
	public int hashCode() {
		return getRHS().hashCode() * getLHS().hashCode();
	}

	/**
	 * Returns all terminals on the left hand side of the production.
	 * 
	 * @return all terminals on the left hand side of the production.
	 */
	public Set<Terminal> getTerminalsOnLHS() {
		return getLHS().getSymbolsOfClass(Terminal.class);
	}

	/**
	 * Returns a string representation of the production object.
	 * 
	 * @return a string representation of the production object.
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getLHS());
		 buffer.append("->");
//		buffer.append('\u2192');
		buffer.append(getRHS());
		// buffer.append('\n');
		return buffer.toString();
	}
	

	@Override
	protected Production clone() {
		try {
			return this.getClass().
							getConstructor(SymbolString.class, SymbolString.class).
								newInstance(getLHS().clone(), getRHS().clone());
		} catch (Exception e) {
			throw new AlphabetException("Error cloning Production");
		}
	}

	public boolean isStartProduction(Variable variable) {
		if (this.getLHS().isEmpty()) return false;
		return this.getLHS().getFirst().equals(variable);
//				&& this.getLHS().size() == 1;
	}
	
	public boolean containsSymbol(Symbol symbol) {
		return getLHS().contains(symbol) || getRHS().contains(symbol);
	}

	@Override
	public int compareTo(Production o) {
		
		int i = 0;
		if((i = this.getLHS().compareTo(o.getLHS())) == 0)
			i = this.getRHS().compareTo(o.getRHS());
		
		return i;
	}

	public boolean isEmpty() {
		return this.getLHS().isEmpty() && this.getRHS().isEmpty();
	}

	public boolean purgeOfSymbol(Alphabet a, Symbol s) {
		boolean lhs = this.getLHS().removeEach(s); 
		return this.getRHS().removeEach(s) || lhs;
	}

	
	public Object[] toArray() {
		return new Object[]{
				this.getLHS().toString(),
				ARROW,
				this.getRHS().toString()};
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> set = new TreeSet<Symbol>();
		if (a instanceof VariableAlphabet){
			set.addAll(getVariablesUsed());
		}
		else if (a instanceof TerminalAlphabet){
			set.addAll(getTerminalsUsed());
		}
		return set;
	}

	@Override
	public String getDescriptionName() {
		return "Production Rule";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Production copy() {
		return new Production(this.getLHS().copy(), this.getRHS().copy());
	}
	
	public boolean isLambdaProduction() {
		return this.getRHS().isEmpty();
	}

	@Override
	public boolean setTo(Production other) {
		return this.setTo(other.getLHS(), other.getRHS());
	}

	private boolean setTo(SymbolString lhs, SymbolString rhs) {
		return this.setLHS(lhs) && this.setRHS(rhs);
	}


}
