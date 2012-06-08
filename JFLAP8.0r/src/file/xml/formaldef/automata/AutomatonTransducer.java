package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import model.automata.Automaton;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import file.xml.Transducer;
import file.xml.TransducerFactory;
import file.xml.formaldef.FormalDefinitionTransducer;
import file.xml.formaldef.components.specific.transitions.TransitionSetTransducer;

public abstract class AutomatonTransducer<T extends Automaton> extends FormalDefinitionTransducer<T> {

	@Override
	public T createFromAlphabets(List<Alphabet> alphs, List<Element> remains) {
		Transducer func = createTransitionFuncTransducer(alphs);
		return null;
	}

	@Override
	public void addFunctionSets(Map<Object, Transducer> map, T structure) {
		Transducer func = createTransitionFuncTransducer(null);
		map.put(structure.getTransitions(), func);
	}

	public abstract TransitionSetTransducer createTransitionFuncTransducer(List<Alphabet> alphs);

}
