package model.algorithms.conversion.autotogram;

import model.automata.State;
import model.formaldef.components.symbols.Symbol;

public class TMVariableMapping extends VariableMapping{
	private State myState;
	private Symbol myA;
	private Symbol myB;

	public TMVariableMapping(Symbol a, Symbol b){
		this(a,null,b);
	}
	
	public TMVariableMapping(Symbol a, State i, Symbol b){
		myA = a;
		myState = i;
		myB = b;
	}
	
	@Override
	public String toString() {
		return "V"+ myA + (myState==null? "" : myState.getID()) + myB;
	}

	@Override
	public boolean equals(Object arg0) {
		TMVariableMapping other = (TMVariableMapping) arg0;
		return (myA.equals(other.myA) && myB.equals(other.myB) && myState.equals(other.myState));
	}

	@Override
	public int hashCode() {
		return myA.hashCode()+myState.hashCode()*myState.hashCode()+myB.hashCode()*myB.hashCode()*myB.hashCode();
	}
	

}
