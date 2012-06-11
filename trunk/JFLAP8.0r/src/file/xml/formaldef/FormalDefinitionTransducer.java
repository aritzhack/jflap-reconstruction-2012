package file.xml.formaldef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;

import debug.JFLAPDebug;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import file.xml.MetaTransducer;
import file.xml.StructureTransducer;
import file.xml.XMLTransducer;
import file.xml.TransducerFactory;
import file.xml.formaldef.components.alphabet.AlphabetTransducer;

public abstract class FormalDefinitionTransducer<T extends FormalDefinition> extends MetaTransducer<T> {

	
	
	

	@Override
	public T fromSubStructureList(List<Element> list) {
		List<Alphabet> alphs = retrieveAlphabets(list);
		List<Object> comps = new ArrayList<Object>(alphs);
		Map<Element, XMLTransducer> tdMap = new HashMap<Element, XMLTransducer>();
		for (Element e: list){
			XMLTransducer trans = StructureTransducer.getStructureTransducer(e);
			if (trans == null)
				trans = getTransducerForStructureNode(StructureTransducer.retrieveTypeTag(e),
																alphs);
			JFLAPDebug.print(e.getAttribute(STRUCTURE_TYPE_ATTR));
			comps.add(trans.fromStructureRoot(e));
		}
		return buildStructure(comps.toArray());
	}

	public abstract XMLTransducer getTransducerForStructureNode(String string, List<Alphabet> alphs);

	private List<Alphabet> retrieveAlphabets(List<Element> list) {
		List<Alphabet> alphs = new ArrayList<Alphabet>();
		for (Element e: list.toArray(new Element[0])){
			XMLTransducer trans = StructureTransducer.getStructureTransducer(e);
			if (trans instanceof AlphabetTransducer){
				alphs.add((Alphabet) trans.fromStructureRoot(e));
				list.remove(e);
			}
		}
		return alphs;
	}

	@Override
	public FormalDefinitionComponent[] getConstituentComponents(T structure) {
		Set<FormalDefinitionComponent> comps = new HashSet<FormalDefinitionComponent>();
		for (FormalDefinitionComponent  comp: structure.getComponents()){
			if (comp instanceof LanguageFunction) continue;
			comps.add(comp);
		}
		return comps.toArray(new FormalDefinitionComponent[0]);
	}

	@Override
	public Map<Object, XMLTransducer> createTransducerMap(T structure) {
		Map<Object, XMLTransducer> map = super.createTransducerMap(structure);
		addFunctionSetsToMap(map, structure);
		return map;
	}

	public abstract void addFunctionSetsToMap(Map<Object, XMLTransducer> map, T structure);
	

	public static <T extends Alphabet> T retrieveAlphabet(List<Alphabet> alphs,
															Class<T> target){
		if (alphs == null) return null;
		for (Alphabet alph: alphs){
			if (target.isAssignableFrom(alph.getClass()))
				return (T) alph;
		}
		return null;
	}

}
