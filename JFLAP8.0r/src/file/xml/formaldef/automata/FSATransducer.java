package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import file.xml.XMLTransducer;
import file.xml.formaldef.FormalDefinitionTransducer;
import file.xml.formaldef.components.FunctionSetTransducer;
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

public class FSATransducer extends AutomatonTransducer<FiniteStateAcceptor> {

	@Override
	public FiniteStateAcceptor buildStructure(Object[] subComp) {
		return new FiniteStateAcceptor((StateSet) subComp[0], 
										(InputAlphabet) subComp[1], 
										(TransitionSet<FSATransition>) subComp[2], 
										(StartState) subComp[3], 
										(FinalStateSet) subComp[4]);
	}

	@Override
	public String getTypeTag() {
		return FSA_TAG;
	}

	@Override
	public TransitionSetTransducer createTransitionFuncTransducer(
			List<Alphabet> alphs) {
		InputAlphabet inputAlph = null;
		if (alphs != null)
			inputAlph = (InputAlphabet) alphs.get(0);
		FSATransitionTransducer single = 
				new FSATransitionTransducer(inputAlph);
		TransitionSetTransducer<FSATransition> ducer = 
				new TransitionSetTransducer<FSATransition>(single);
		return ducer;
	}

	
}
