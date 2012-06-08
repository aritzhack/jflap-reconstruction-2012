package file.xml;

import java.lang.Thread.State;
import java.util.HashMap;
import java.util.Map;
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

public class TransducerFactory {

	private static Map<Class, Transducer> myClassToTransducerMap;
	
	static{
		myClassToTransducerMap = new HashMap<Class, Transducer>();
		addMapping(RegularExpression.class, new RegExTransducer());
		addMapping(FiniteStateAcceptor.class, new FSATransducer());
		addMapping(FinalStateSet.class, new FinalStateSetTransducer());
		addMapping(InputAlphabet.class, new InputAlphabetTransducer());
		addMapping(StateSet.class, new StateSetTransducer());
		addMapping(StartState.class, new StartStateTransducer());
		addMapping(State.class, new StateTransducer());
		addMapping(State.class, new FromStateTransducer());
		addMapping(State.class, new ToStateTransducer());
	}

	public static void addMapping(Class c, Transducer struct) {
		myClassToTransducerMap.put(c,struct);
	}
	
	public static <T> Transducer<T> getTransducerForStructure(T object){
		return myClassToTransducerMap.get(object.getClass());
	}
	
	public static Transducer getTransducerForTag(String tag){
		for (Transducer trans: myClassToTransducerMap.values())
		{
			if (trans.getTag().equals(tag))
				return trans;
		}
		return null;
	}
	
	/**
	 * Given a DOM document, this will return an appropriate instance of a
	 * transducer for the type of document. Note that the type of the structure
	 * should be specified with in the "type" tags.
	 * 
	 * @param document
	 *            the document to get the transducer for
	 * @return the correct transducer for this document
	 * @throws IllegalArgumentException
	 *             if the document does not map to a transducer, or if it does
	 *             not contain a "type" tag at all
	 */
	public static Transducer getTransducer(Element root) {
		// Check for the type tag.
		NodeList structureNodes = root.getElementsByTagName(StructureTransducer.STRUCTURE_TYPE_NAME);
		
		if (structureNodes.getLength() > 1)
			throw new DataException("Multiple type nodes \n" +
										"exist in this structure");
		
		Node n = structureNodes.item(0);
		String type = ((Text) n.getChildNodes().item(0)).getData();
		
		
		return getTransducerForTag(type);
		
	}
}
