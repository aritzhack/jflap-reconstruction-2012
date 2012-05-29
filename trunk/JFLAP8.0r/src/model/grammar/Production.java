package model.grammar;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;



import errors.BooleanWrapper;

import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.util.JFLAPResources;

public class Production implements LanguageFunction, Comparable<Production>, JFLAPResources{

	/** the left hand side of the production. */
	protected SymbolString myLHS;

	/** the right hand side of the production. */
	protected SymbolString myRHS;

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

	public Production(Symbol lhs, Symbol ... rhs) {
		this(new SymbolString(lhs), new SymbolString(rhs));
	}

	/**
	 * Sets the right hand side of production to <CODE>rhs</CODE>.
	 * 
	 * @param rhs
	 *            the right hand side
	 */
	public void setRHS(SymbolString rhs) {
		myRHS = rhs;
	}

	/**
	 * Sets the left hand side of production to <CODE>lhs</CODE>.
	 * 
	 * @param lhs
	 *            the left hand side
	 */
	public void setLHS(SymbolString lhs) {
		myLHS = lhs;
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
		return myLHS.getSymbolsOfClass(Variable.class);
	}

	/**
	 * Returns all variables on the right hand side of the production.
	 * 
	 * @return all variables on the right hand side of the production.
	 */
	public Set<Variable> getVariablesOnRHS() {
		return myRHS.getSymbolsOfClass(Variable.class);
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
		return myRHS.getSymbolsOfClass(Terminal.class);
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
		return myRHS.hashCode() * myLHS.hashCode();
	}

	/**
	 * Returns all terminals on the left hand side of the production.
	 * 
	 * @return all terminals on the left hand side of the production.
	 */
	public Set<Terminal> getTerminalsOnLHS() {
		return myLHS.getSymbolsOfClass(Terminal.class);
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
								newInstance(myLHS.clone(), myRHS.clone());
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
		return myLHS.contains(symbol) || myRHS.contains(symbol);
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

	public boolean purgeOfSymbol(Symbol s) {
		boolean lhs = this.getLHS().purgeOfSymbol(s); 
		return this.getRHS().purgeOfSymbol(s) || lhs;
	}

	
	public Object[] toArray() {
		return new Object[]{
				this.getLHS().toString(),
				ARROW,
				this.getRHS().toString()};
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		Set<Symbol> used = myLHS.getUniqueSymbolsUsed();
		used.addAll(myRHS.getUniqueSymbolsUsed());
		return used;
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


}
