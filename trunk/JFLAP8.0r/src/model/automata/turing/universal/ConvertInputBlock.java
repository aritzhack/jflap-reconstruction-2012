package model.automata.turing.universal;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ConvertInputBlock extends MappingBlock{

	private SymbolString myTransitionEncoding;
	private Block myLeftBlank, myRightPivot;
	private Set<Block> loops;
	
	public ConvertInputBlock(SymbolString transEncoding, TapeAlphabet tape, int id) {
		super(tape, "Convert", id, transEncoding);
		
		updateTuringMachine(tape);
		
	}
	
	private void addTransition(Block from, Block to, Symbol input) {
		TransitionSet<BlockTransition> trans = this.getTuringMachine().getTransitions();
		trans.add(new BlockTransition(from, to, input));
	}

	@Override
	public void updateTuringMachine(Map<Symbol, SymbolString> encodings) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		BlockSet blocks = tm.getStates();
		BlankSymbol blank = new BlankSymbol();
		
		Set<Symbol> tape = new TreeSet<Symbol>();
		for(Symbol s : encodings.keySet()){
			tape.add(s);
		}
		
		for(BlockTransition transition : transitions.toArray(new BlockTransition[0])){
			Block to = transition.getToState(), from = transition.getFromState();
			
			if(loops.contains(to) || loops.contains(from)){
				blocks.remove((myRightPivot.equals(from) ? to : from));  
			}
		}
		
		loops.clear();
		
		for(Symbol term : tape){
				SymbolString ones = encodings.get(term);
				Block mapBlock = new ToMapBlock(ones, alph, blocks.getNextUnusedID());
				
				addTransition(myRightPivot, mapBlock, term);
				addTransition(mapBlock, myLeftBlank, new Symbol(TILDE));
				
				loops.add(mapBlock);
			
		}

	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		myTransitionEncoding = (SymbolString) args[0];
		loops = new TreeSet<Block>();
	
		BlockTuringMachine tm = getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		BlockSet blocks = tm.getStates();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		BlankSymbol blank = new BlankSymbol();
		
		int id=0;
		Symbol tilde = new Symbol(TILDE);
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		
		Block b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank.getSymbol(), alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(new Symbol(TM_MARKER), alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		myLeftBlank = b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank.getSymbol(), alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		myRightPivot = b2 = new MoveBlock(TuringMachineMove.RIGHT, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(new Symbol("0"), alph,id++);
		addTransition(b1, b2, new Symbol(TM_MARKER));
		
		for(int i=myTransitionEncoding.size()-1; i>=0; i--){
			Symbol a = myTransitionEncoding.get(i);
			
			b1=b2;
			b2 = new MoveBlock(TuringMachineMove.LEFT, alph,id++);
			addTransition(b1, b2, tilde);
			
			b1=b2;
			b2 = new WriteBlock(a, alph,id++);
			addTransition(b1, b2, tilde);
		}
		
		b1=b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);
		
		tm.getFinalStateSet().add(b2);
		
		
	}
}
