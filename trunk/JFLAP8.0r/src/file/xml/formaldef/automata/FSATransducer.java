package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import file.xml.Transducer;
import file.xml.formaldef.FormalDefinitionTransducer;
import file.xml.formaldef.components.specific.transitions.FSATransitionTransducer;
import file.xml.formaldef.components.specific.transitions.TransitionSetTransducer;
import file.xml.formaldef.components.specific.transitions.TransitionTransducer;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.components.alphabets.Alphabet;

public class FSATransducer extends FormalDefinitionTransducer<FiniteStateAcceptor> {

	@Override
	public FiniteStateAcceptor buildStructure(Object[] subComp) {
		return new FiniteStateAcceptor((StateSet) subComp[0], 
										(InputAlphabet) subComp[1], 
										(TransitionSet<FSATransition>) subComp[2], 
										(StartState) subComp[3], 
										(FinalStateSet) subComp[4]);
	}

	@Override
	public String getTag() {
		return "fsa";
	}

	@Override
	public FiniteStateAcceptor createFromAlphabets(List<Alphabet> alphs,
			List<Element> remains) {
		
		return null;
	}

	@Override
	public void addFunctionSets(Map<Object, Transducer> map,
			FiniteStateAcceptor fsa) {
		TransitionSet<FSATransition> trans = fsa.getTransitions();
		FSATransitionTransducer single = new FSATransitionTransducer(null);
		TransitionSetTransducer<FSATransition> ducer = 
				new TransitionSetTransducer<FSATransition>(single);
		map.put(trans, ducer);
	}

	
}
