package model.automata.simulate;

import java.util.ArrayList;
import java.util.Collection;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.formaldef.components.symbols.SymbolString;

public class AutoSimulator extends AutomatonSimulator{

	private SingleInputSimulator mySimulator;

	public AutoSimulator(Automaton a, int specialCase) {
		super(a);
		mySimulator  = new SingleInputSimulator(a, specialCase);
	}

	public Collection<ConfigurationChain> getNextAccept(){
				
		while (!mySimulator.getChains().isEmpty()){
			Collection<ConfigurationChain> chains = mySimulator.step();
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
	public void beginSimulation(SymbolString[]... input) {
		mySimulator.beginSimulation(input);
	}

	@Override
	public int getSpecialAcceptCase() {
		return mySimulator.getSpecialAcceptCase();
	}

	public void beginSimulation(SymbolString input) {
		mySimulator.beginSimulation(input);
	}

}
