package model.automata.turing.buildingblock.library;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import oldnewstuff.main.JFLAP;
import test.BuildingBlockTesting;

import debug.JFLAPDebug;

import model.automata.AutomatonException;
import model.automata.InputAlphabet;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.typetest.matchers.RightLinearChecker;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * This block is used as part of the ShiftBlock block.
 * it will only shift if the symbol passed into the constructor
 * is currently being read.
 * 
 * @author Julian
 *
 */
public class SingleShiftBlock extends BlockTMUpdatingBlock {

	private TuringMachineMove myShift;
	private TuringMachineMove myOpposite;
	private ArrayList<Loop> myLoops;
	private Block myPivot;
	private Symbol myMarker;

	public SingleShiftBlock(Symbol s, TuringMachineMove direction, TapeAlphabet tape, int id) {
		super(tape, BlockLibrary.SHIFT + BlockLibrary.UNDSCR +direction.char_abbr+"_" + s, id, direction, s);
		if (direction == TuringMachineMove.STAY)
			throw new AutomatonException("You may not shift with a stay option.");

		
		
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> sym = super.getSymbolsUsedForAlphabet(a);
			sym.remove(myMarker);

		return sym;
	}

	private String getBestMarker(TapeAlphabet alph) {
		return "#";
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		alph.retainAll(tape);
		alph.addAll(tape);
		alph.add(myMarker);
		
		Set<Symbol> symbols = new TreeSet<Symbol>(tape);
		for (Loop loop: myLoops.toArray(new Loop[0])){
			if (symbols.contains(loop.symbol))
				symbols.remove(loop.symbol);
			else
				removeLoop(loop);
		}

		symbols.remove(myMarker);
		symbols.remove(tm.getBlankSymbol());

		for (Symbol s: symbols){
			Loop loop = createLoop(s,alph);
			myLoops.add(loop);
		}
	}

	private void removeLoop(Loop loop) {
		BlockSet blocks = this.getTuringMachine().getStates(); 
		for (Block b: loop.blocks){
			blocks.remove(b);
		}
		myLoops.remove(loop);
	}

	private Loop createLoop(Symbol s, TapeAlphabet local) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		BlockSet blocks = tm.getStates(); 
		BlankSymbol blank = new BlankSymbol();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();

		Block b1 = new WriteBlock(blank.getSymbol(), local, blocks.getNextUnusedID());
		blocks.add(b1);
		Block b2 = new MoveBlock(myShift, local, blocks.getNextUnusedID());
		blocks.add(b2);
		Block b3 = new WriteBlock(s, local, blocks.getNextUnusedID());
		blocks.add(b3);
		Block b4 = new MoveBlock(myOpposite, local, blocks.getNextUnusedID());
		blocks.add(b4);

		BlockTransition t1 = new BlockTransition(myPivot, b1, new SymbolString(s));
		BlockTransition t2 = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		BlockTransition t3 = new BlockTransition(b2, b3, new SymbolString(new Symbol(TILDE)));
		BlockTransition t4 = new BlockTransition(b3, b4, new SymbolString(new Symbol(TILDE)));
		BlockTransition t5 = new BlockTransition(b4, myPivot, new SymbolString(new Symbol(TILDE)));

		for (BlockTransition t: new BlockTransition[]{t1,t2,t3,t4,t5}){
			transitions.add(t);
		}

		return new Loop(s, b1,b2,b3,b4);
	}


	private class Loop{
		private Block[] blocks;
		private Symbol symbol;

		public Loop(Symbol a, Block...blocks){
			symbol = a;
			this.blocks = blocks;
		}
	}


	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		BlockTuringMachine tm = (BlockTuringMachine) localTM;
		
		BlankSymbol blank = new BlankSymbol();
		TapeAlphabet alph = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		BlockSet blocks = tm.getStates();
		
		myLoops = new ArrayList<Loop>();
		myMarker = new Symbol(getBestMarker(alph));

		myShift = (TuringMachineMove) args[0];
		myOpposite = myShift == TuringMachineMove.RIGHT ? TuringMachineMove.LEFT : TuringMachineMove.RIGHT;
		int id = 0;
		Symbol s = (Symbol) args[1];
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new MoveBlock(myShift, alph, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		b2 = new WriteBlock(myMarker, alph, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(s));
		transitions.add(trans);

		
		b1=b2;
		b2 = new MoveBlock(myOpposite, alph, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(myMarker));
		transitions.add(trans);

		
		b1=b2;
		b2 = new WriteBlock(blank.getSymbol(), alph, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		myPivot = new MoveBlock(myOpposite, alph, id++);
		trans = new BlockTransition(b1,myPivot, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=myPivot;
		b2 = new MoveUntilBlock(myShift, myMarker, alph, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(blank.getSymbol()));
		transitions.add(trans);

		b1=b2;
		b2 = new WriteBlock(s, alph, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		b2 = new MoveBlock(myOpposite, alph, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		
		b1=b2;
		b2 = new HaltBlock(id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		tm.getFinalStateSet().add(b2);		
	}

}
