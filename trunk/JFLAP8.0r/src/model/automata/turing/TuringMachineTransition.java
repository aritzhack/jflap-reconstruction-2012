package model.automata.turing;

import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;

public class TuringMachineTransition extends Transition {

	private Symbol myWrite;
	private TuringMachineMove myMove;

	public TuringMachineTransition(State from, 
							State to, 
							Symbol read, 
							Symbol write, 
							TuringMachineMove move) {
		super(from, to, new SymbolString(read));
		
		myWrite = write;
		myMove = move;
		
		
	}
	
	public Symbol getWrite(){
		return myWrite;
	}
	
	public Symbol getRead(){
		return this.getInput().getFirst();
	}
	
	public TuringMachineMove getMove(){
		return myMove;
	}
	
	public void setMove(TuringMachineMove move){
		myMove = move;
	}
	
	public void setRead(Symbol read){
		super.setInput(new SymbolString(read));
	}
	
	public void setWrite(Symbol write){
		myWrite = write;
	}

	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
