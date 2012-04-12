package model.formaldef.components.symbols;


public class Terminal extends Symbol {

	public Terminal(String s) {
		super(s);
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Terminal && super.equals(o);
	}

}
