package model.automata.turing;

import model.automata.AutomatonException;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.Transition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

public class TuringMachineTransition extends Transition<TuringMachineTransition> {

	private SymbolString[] myWrites;
	private TuringMachineMove[] myMoves;
	private SymbolString[] myReads;

	public TuringMachineTransition(State from, 
										State to,
										Symbol read,
										Symbol write,
										TuringMachineMove move){
		this(from, to, 
				new Symbol[]{read},
				new Symbol[]{write},
				new TuringMachineMove[]{move} );
	}
	
	public TuringMachineTransition(State from, 
							State to, 
							Symbol[] read, 
							Symbol[] write, 
							TuringMachineMove[] move) {
		super(from, to);
		
		if (!(read.length == write.length && 
				write.length == move.length))
			throw new AutomatonException("The turing machine transition cannot" +
					" be created with unequal numbers of reads/writes/moves.");
		
		myReads = new SymbolString[read.length];
		myWrites = new SymbolString[write.length];
		myMoves = new TuringMachineMove[move.length];
		for (int i = 0; i< this.getNumTapes(); i++){
			setRead(read[i], i);
			setWrite(write[i], i);
			setMove(move[i], i);

		}
	}
	
	public SymbolString getWrite(int tape){
		return myWrites[tape];
	}
	
	public SymbolString getRead(int tape){
		return myReads[tape];
	}
	
	public TuringMachineMove getMove(int tape){
		return myMoves[tape];
	}
	
	public void setMove(TuringMachineMove move, int tape){
		myMoves[tape] = move;
	}
	
	public void setRead(Symbol read, int tape){
		myReads[tape].setTo(read);
	}
	
	public void setWrite(Symbol write, int tape){
		myWrites[tape].setTo(write);
	}

	@Override
	public String getDescriptionName() {
		return "Turing Machine Transition";
	}

	@Override
	public String getDescription() {
		return "The transition for a single tape turing machine";
	}

	@Override
	public TuringMachineTransition copy() {
		return new TuringMachineTransition(this.getFromState().copy(), 
									this.getToState().copy(), 
									toSymbols(myReads),
									toSymbols(myWrites),
									myMoves);
	}

	private Symbol[] toSymbols(SymbolString[] strings) {
		Symbol[] symbols = new Symbol[strings.length];
		for (int i =0; i< strings.length; i++){
			symbols[i] = strings[i].getFirst();
		}
		return symbols;
	}

	@Override
	public String getLabelText() {
		String label = "";
		for (int i = 0; i < this.getNumTapes(); i++){
			label += this.getRead(i) + ";" + 
					this.getWrite(i) + "," + 
					this.getMove(i) + "\n";
		}
		return label;
	}

	public int getNumTapes() {
		return myReads.length;
	}

	@Override
	public int compareTo(TuringMachineTransition o) {
		int compare = new Integer(this.getNumTapes()).compareTo(o.getNumTapes());
		if (compare == 0){
			for (int i = 0; i < this.getNumTapes(); i++){
				compare = this.getRead(i).compareTo(o.getRead(i));
				if (compare != 0) return compare;
				compare = this.getWrite(i).compareTo(o.getWrite(i));
				if (compare != 0) return compare;
				compare = this.getMove(i).compareTo(o.getMove(i));
				if (compare != 0) return compare;
			}
		}
		return compare;
	}

	@Override
	public boolean setTo(TuringMachineTransition o) {
		if ( this.getNumTapes() != o.getNumTapes()) return false;
		
		for (int i = 0; i < this.getNumTapes(); i++){
			this.setRead(o.getRead(i).getFirst(), i);
			this.setWrite(o.getWrite(i).getFirst(), i);
			this.setMove(o.getMove(i), i);
		}
		return true;
	}

	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		return null;
	}

	@Override
	public boolean isLambdaTransition() {
		// TODO Auto-generated method stub
		return false;
	}

}
