package file.xml.formaldef.components.specific.transitions;

import java.util.Map;

import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Copyable;
import file.xml.XMLHelper;

public class FSTransitionTransducer extends TransitionTransducer<FSTransition> {


	@Override
	public String getTag() {
		return "fsa_trans";
	}

	@Override
	public FSTransition createFunction(Map<String, SymbolString> valueMap) {
		return new FST;
	}

	@Override
	public Map<String, SymbolString> createTagToValueMap(FSTransition structure) {
		// TODO Auto-generated method stub
		return null;
	}

}
