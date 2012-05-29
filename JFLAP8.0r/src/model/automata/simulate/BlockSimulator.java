package model.automata.simulate;

import model.automata.Automaton;


public class BlockSimulator extends SingleInputSimulator {

	public BlockSimulator(Automaton a, int specialCase) {
		super(a, specialCase);
	}

	private SingleInputSimulator myParent;

//	public BlockSimulator(Configuration blockConfig,
//			SingleInputSimulator parent) {
//		super(getBlockMachine(blockConfig), 
//				parent.getSpecialAcceptCase());
//		myParent = parent;
//		blockConfig.setState(getBlockMachine(blockConfig).getInitialState());
//		this.beginSimulation(blockConfig);
//	}
//
//	private static Automaton getBlockMachine(Configuration blockConfig) {
//		return ((Block) blockConfig.getState()).getInnerMachine();
//	}
//
//	public SingleInputSimulator getParent(){
//		return myParent;
//	}

	
	
	
}
