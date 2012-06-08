package file.xml.formaldef;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;

public class FSATransducer extends FormalDefinitionTransducer<FiniteStateAcceptor> {

	@Override
	public FiniteStateAcceptor buildStructure(Object[] subComp) {
		return new FiniteStateAcceptor((StateSet) subComp[0], 
										(InputAlphabet) subComp[1], 
										(TransitionSet<FSTransition>) subComp[2], 
										(StartState) subComp[3], 
										(FinalStateSet) subComp[4]);
	}

	@Override
	public String getTag() {
		return "fsa";
	}


}
