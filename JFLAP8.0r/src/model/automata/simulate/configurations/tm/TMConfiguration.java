package model.automata.simulate.configurations.tm;

import preferences.JFLAPPreferences;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.simulate.Configuration;
import model.automata.turing.TuringMachine;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class TMConfiguration<S extends TuringMachine<T>, T extends Transition<T>> 
														extends Configuration<S,T> {

	public TMConfiguration(S tm, State s, int[] pos,
			SymbolString ... tapes) {
		super(tm, s, 0, null, pos, tapes);
	}

	@Override
	public String getName() {
		return "TM Configuration";
	}

	@Override
	protected String getPrimaryPresentationName() {
		//primary is not used
		return null;
	}

	@Override
	protected int getNextPrimaryPosition(T label) {
		//primary is not used
		return 0;
	}

	@Override
	protected boolean shouldFindValidTransitions() {
		//Turing machine is never "done". if a valid transitions
		//from a state exists, it should be moved on.
		return true;
	}

	@Override
	protected boolean isDone() {
		//a Turing machine is done iff it does not have a next state
		return !this.hasNextState();
	}

	@Override
	protected String getStringPresentationName(int i) {
		return "Tape " + i;
	}
	
	public Symbol getReadForTape(int i){
		return getStringForIndex(i).get(getPositionForIndex(i));
	}


	public static SymbolString updateTape(TuringMachineMove move, int pos, SymbolString tape) {
		SymbolString temp = new SymbolString(tape);
		if (pos == temp.size()-1)
			temp.add(JFLAPPreferences.getTMBlankSymbol());
		else if (pos == 0 && move == TuringMachineMove.LEFT)
			temp.addFirst(JFLAPPreferences.getTMBlankSymbol());
		return temp;
	}
	
	public static int reBufferString(SymbolString input, int pos, int bufferSize){
		Symbol blank = JFLAPPreferences.getTMBlankSymbol();
		while(input.getFirst().equals(blank) && pos > 0){
			input.removeFirst();
			pos--;
		}
		while(input.getLast().equals(blank)){
			input.removeLast();
		}
		
		input = TMConfiguration.createBlankBufferedString(input, bufferSize);
		
		pos+=bufferSize;

		return pos;
	}
	
	public static SymbolString createBlankBufferedString(SymbolString input, int n) {
		input = new SymbolString(input);
		input.addAll(0,createBlankTape(n));
		input.addAll(createBlankTape(n));
		return input;
	}

	public static SymbolString createBlankTape(int size) {
		Symbol blank = JFLAPPreferences.getTMBlankSymbol();
		SymbolString blanks = new SymbolString();
		for (int i = 0; i< size; i++){
			blanks.add(blank);
		}
		return blanks;
	}
}
