package model.regex.operators;

import model.formaldef.Describable;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;

public abstract class Operator extends Terminal implements Describable{

	public Operator(String s) {
		super(s);
	}
	
}
