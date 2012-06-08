package file.xml.formaldef.components.specific.transitions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.automata.Transition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SymbolString;
import file.xml.Transducer;
import file.xml.XMLHelper;
import file.xml.formaldef.components.FunctionTransducer;
import file.xml.formaldef.components.single.SymbolStringTransducer;

public abstract class TransitionTransducer<T extends Transition> extends FunctionTransducer<T> {

	@Override
	public String getTag() {
		return TRANSITION_TAG;
	}

}
