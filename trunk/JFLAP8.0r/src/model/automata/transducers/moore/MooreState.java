package model.automata.transducers.moore;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Set;

import model.automata.State;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;




public class MooreState extends State implements UsesSymbols {

	private SymbolString myOutput;

	
	
	public MooreState(String name, int id, Point location, SymbolString output) {
		super(name, id, location);
		this.setOutput(output);
	}

	public SymbolString getOutput() {
		return myOutput;
	}
	
	public void setOutput(SymbolString output) {
		this.myOutput = output;
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		return myOutput.getUniqueSymbolsUsed();
	}

	@Override
	public boolean purgeOfSymbol(Symbol s) {
		return myOutput.purgeOfSymbol(s);
	}

	@Override
	public State clone() {
		return new MooreState(this.getName(),
				this.getID(),
				this.getLocation(),
				this.getOutput());
	}


	



	
	
	
}
