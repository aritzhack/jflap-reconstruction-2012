package file.xml.formaldef.components.functions.output;

import java.util.Map;

import model.automata.State;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.formaldef.components.symbols.SymbolString;

public class MealyOutputFuncTransducer extends
		OutputFunctionTransducer<MealyOutputFunction> {

	public MealyOutputFuncTransducer(OutputAlphabet alph) {
		super(alph);
	}

	@Override
	public MealyOutputFunction createOutputFunction(State s,
			SymbolString output, Map<String, Object> valueMap) {
		SymbolString input = (SymbolString) valueMap.get(TRANS_INPUT_TAG);
		return new MealyOutputFunction(s, input, output);
	}

	@Override
	public Map<String, Object> finishTagToValueMap(Map<String, Object> map,
			MealyOutputFunction func) {
		map.put(TRANS_INPUT_TAG, func.getInput());
		return map;
	}

}
