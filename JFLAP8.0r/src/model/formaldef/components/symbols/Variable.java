package model.formaldef.components.symbols;


public class Variable extends Symbol {

	public Variable(String s) {
		super(s);
	}

	@Override
	public boolean equals(Object o) {
		return !(o instanceof Terminal) && super.equals(o);
	}
	
	
	

}
