package model.grammar.parsing.lr;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import model.automata.State;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;
import model.util.UtilFunctions;

public class SLR1DFAState extends State{

	private Set<SLR1Production> myProductions;

	public SLR1DFAState(String name, int id, Set<SLR1Production> closure) {
		super(name, id);
		myProductions = new TreeSet<SLR1Production>(closure);
	}

	public SLR1DFAState(State s, Set<SLR1Production> closure) {
		this(s.getName(), s.getID(), closure);
	}

	public boolean matchesSet(Set<SLR1Production> closure) {
		return myProductions.containsAll(closure);
	}
	
	public SLR1Production[] getProductions(){
		return myProductions.toArray(new SLR1Production[0]);
	}
	
	@Override
	public String getLabel() {
		return UtilFunctions.createDelimitedString(myProductions, "\n");
	}
	
	@Override
	public String toString() {
		return super.toString() + " | " + myProductions.toString();
	}

	public Set<Symbol> getSymbolsForTransition() {
		Set<Symbol> curr = new TreeSet<Symbol>();
		for (SLR1Production p: myProductions){
			Symbol s = p.getSymbolAfterMarker();
			if (s == null) continue;
			curr.add(s);
		}
		return curr;
	}

	public Set<SLR1Production> getProductionsWithMarkBefore(Symbol s) {
		Set<SLR1Production> marked = new TreeSet<SLR1Production>();
		for (SLR1Production p: myProductions){
			Symbol o = p.getSymbolAfterMarker();
			if (o != null && o.equals(s))
				marked.add(p);
		}
		return marked;
	}

	public Set<SLR1Production> getReduceProductions() {
		Set<SLR1Production> reduce = new TreeSet<SLR1Production>();
		for (SLR1Production p: myProductions){
			if (p.isReduceProduction())
				reduce.add(p);
		}
		return reduce;
	}
	
	

}
