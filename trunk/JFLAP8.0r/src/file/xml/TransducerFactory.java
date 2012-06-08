package file.xml;

import java.lang.Thread.State;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.regex.RegularExpression;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import debug.JFLAPDebug;

import file.DataException;
import file.xml.formaldef.automata.FSATransducer;
import file.xml.formaldef.components.specific.alphabet.InputAlphabetTransducer;
import file.xml.formaldef.components.specific.states.FinalStateSetTransducer;
import file.xml.formaldef.components.specific.states.StartStateTransducer;
import file.xml.formaldef.components.specific.states.StateSetTransducer;
import file.xml.formaldef.components.specific.states.StateTransducer;
import file.xml.formaldef.components.specific.transitions.FromStateTransducer;
import file.xml.formaldef.components.specific.transitions.ToStateTransducer;
import file.xml.formaldef.regex.RegExTransducer;

public class TransducerFactory{

	private static Map<Class, LinkedHashSet<XMLTransducer>> myClassToTransducerMap;
	
	static{
		myClassToTransducerMap = new HashMap<Class, LinkedHashSet<XMLTransducer>>();
		addMapping(RegularExpression.class, new RegExTransducer());
		addMapping(FiniteStateAcceptor.class, new FSATransducer());
		addMapping(FinalStateSet.class, new FinalStateSetTransducer());
		addMapping(InputAlphabet.class, new InputAlphabetTransducer());
		addMapping(StateSet.class, new StateSetTransducer());
		addMapping(StartState.class, new StartStateTransducer());
		addMapping(State.class, new StateTransducer(),
								new FromStateTransducer(), 
								new ToStateTransducer());
	}

	public static void addMapping(Class c, XMLTransducer ... struct) {
		myClassToTransducerMap.put(c,
				new LinkedHashSet<XMLTransducer>(Arrays.asList(struct)));
	}
	
	public static <T> XMLTransducer<T> getTransducerForStructure(T object){
		LinkedHashSet<XMLTransducer> set = myClassToTransducerMap.get(object.getClass());
		if (set == null) return null;
		return (XMLTransducer<T>) set.toArray()[0];
	}
	
	public static XMLTransducer getTransducerForTag(String tag){
		for (LinkedHashSet<XMLTransducer> set: myClassToTransducerMap.values())
		{
			for (XMLTransducer trans:set){
				if (trans.getTag().equals(tag))
					return trans;
			}
		}
		return null;
	}
	

}
