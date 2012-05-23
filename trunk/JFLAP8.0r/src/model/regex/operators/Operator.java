package model.regex.operators;

import model.formaldef.Describable;
import model.formaldef.components.symbols.Symbol;

public abstract class Operator extends Symbol implements Describable{

	public Operator(String s) {
		super(s);
	}
	
}
