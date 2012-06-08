package file.xml.formaldef.components.specific.transitions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SymbolString;
import model.util.UtilFunctions;
import file.xml.Transducer;
import file.xml.XMLHelper;
import file.xml.formaldef.components.FunctionTransducer;
import file.xml.formaldef.components.single.SymbolStringTransducer;

public abstract class TransitionTransducer<T extends Transition> extends FunctionTransducer<T> {

	
	public TransitionTransducer(InputAlphabet alph, Alphabet ... alphs){
		super(UtilFunctions.combine(alphs, alph));
	}
	@Override
	public String getTag() {
		return TRANSITION_TAG;
	}

	@Override
	public T createFunction(Map<String, Object> valueMap) {
		State from = (State) valueMap.remove(FROM_STATE);
		State to = (State) valueMap.remove(TO_STATE);
		SymbolString input = (SymbolString) valueMap.remove(TRANS_INPUT_TAG);
		return createTransition(from, to, input, valueMap);
	}
	
	

	@Override
	public Map<String, Object> createTagToValueMap(T structure) {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(FROM_STATE, structure.getFromState());
		valueMap.put(TO_STATE, structure.getToState());
		valueMap.put(TRANS_INPUT_TAG, structure.getInput());
		
		return addOtherLabelComponentsToMap(valueMap, structure);
	}

	public abstract Map<String, Object> addOtherLabelComponentsToMap(
			Map<String, Object> base, T structure);

	public abstract T createTransition(State from, State to, SymbolString input,
			Map<String, Object> remaining);
	
	

}
