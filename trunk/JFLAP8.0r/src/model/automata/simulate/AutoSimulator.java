package model.automata.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.formaldef.components.symbols.SymbolString;

public class AutoSimulator extends AutomatonSimulator{

	private SingleInputSimulator mySimulator;

	public AutoSimulator(Automaton a, int specialCase) {
		super(a);
		mySimulator  = new SingleInputSimulator(a, specialCase);
	}

	public List<ConfigurationChain> getNextAccept(){
				
		while (!mySimulator.getChains().isEmpty()){
			List<ConfigurationChain> chains = mySimulator.step();
			removeCompletedChains();
			if (!chains.isEmpty()){
				return chains;
			}
		}
		return new ArrayList<ConfigurationChain>();
	}

	private void removeCompletedChains() {
		ArrayList<ConfigurationChain> copy = new ArrayList<ConfigurationChain>(mySimulator.getChains());
		for (ConfigurationChain chain : copy){
			if (chain.isFinished()) 
				mySimulator.removeConfigurationChain(chain);
		}
		
		
	}

	@Override
	public String getDescriptionName() {
		return "Auto Simulate on " + this.getAutomaton().getDescriptionName();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object copy() {
		return new AutoSimulator(getAutomaton(), mySimulator.getSpecialAcceptCase());
	}

	@Override
	public void beginSimulation(SymbolString ... input) {
		mySimulator.beginSimulation(input);
	}
	
	public void beginSimulation(Configuration c){
		mySimulator.beginSimulation(c);
	}

	@Override
	public int getSpecialAcceptCase() {
		return mySimulator.getSpecialAcceptCase();
	}

	public void beginSimulation(SymbolString input) {
		mySimulator.beginSimulation(input);
	}

}
