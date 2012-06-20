package model.automata.turing.buildingblock.library;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import oldnewstuff.main.JFLAP;

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
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.typetest.matchers.RightLinearChecker;

/**
 * This block is used as part of the ShiftBlock block.
 * it will only shift if the symbol passed into the constructor
 * is currently being read.
 * 
 * @author Julian
 *
 */
public class SingleShiftBlock extends BaseBlockTMBlock {

	private TuringMachineMove myShift;
	private TuringMachineMove myOpposite;
	private ArrayList<Loop> myLoops;
	private Block myPivot;
	private Symbol myMarker;

	public SingleShiftBlock(Symbol s, TuringMachineMove direction, TapeAlphabet alph, BlankSymbol blank, int id) {
		super(alph, blank, BlockLibrary.SHIFT + BlockLibrary.UNDSCR +direction.char_abbr+"_" + s, id);
		if (direction == TuringMachineMove.STAY)
			throw new AutomatonException("You may not shift with a stay option.");

		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		BlockSet blocks = tm.getStates();
		myLoops = new ArrayList<Loop>();
		myMarker = new Symbol(getBestMarker(alph));

		myShift = direction;
		myOpposite = direction == TuringMachineMove.RIGHT ? TuringMachineMove.LEFT : TuringMachineMove.RIGHT;
		id = 0;

		Block b1 = new StartBlock(alph, blank, id++);
		tm.setStartState(b1);
		Block b2 = new MoveBlock(myShift, alph, blank, id++);
		BlockTransition trans = new BlockTransition(b1, b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		b2 = new WriteBlock(myMarker, alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(s));
		transitions.add(trans);

		
		b1=b2;
		b2 = new MoveBlock(myOpposite, alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(myMarker));
		transitions.add(trans);

		Block forPrint = b2;
		
		b1=b2;
		b2 = new WriteBlock(blank.getSymbol(), alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		myPivot = new MoveBlock(myOpposite, alph, blank, id++);
		trans = new BlockTransition(b1,myPivot, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		//do all loops
		updateTuringMachine(alph);

		b1=myPivot;
		b2 = new MoveUntilBlock(myShift, blank.getSymbol(), alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		b2 = new WriteBlock(s, alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		b1=b2;
		b2 = new MoveBlock(myOpposite, alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);

		
		b1=b2;
		b2 = new FinalBlock(alph, blank, id++);
		trans = new BlockTransition(b1,b2, new SymbolString(new Symbol(TILDE)));
		transitions.add(trans);
		tm.getFinalStateSet().add(b2);
		
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> sym = super.getSymbolsUsedForAlphabet(a);
		if (a instanceof InputAlphabet)
			sym.remove(myMarker);
		return sym;
	}

	private String getBestMarker(TapeAlphabet alph) {
		return "#";
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
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
			Loop loop = createLoop(s, tape);
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

	private Loop createLoop(Symbol s, TapeAlphabet tape) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		BlockSet blocks = tm.getStates(); 
		BlankSymbol blank = new BlankSymbol();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();

		Block b1 = new WriteBlock(blank.getSymbol(), tape, blank, blocks.getNextUnusedID());
		blocks.add(b1);
		Block b2 = new MoveBlock(myShift, tape, blank, blocks.getNextUnusedID());
		blocks.add(b2);
		Block b3 = new WriteBlock(s, tape, blank, blocks.getNextUnusedID());
		blocks.add(b3);
		Block b4 = new MoveBlock(myOpposite, tape, blank, blocks.getNextUnusedID());
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

}
