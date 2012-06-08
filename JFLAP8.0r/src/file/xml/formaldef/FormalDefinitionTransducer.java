package file.xml.formaldef;

import java.util.List;

import org.w3c.dom.Element;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import file.xml.MetaTransducer;
import file.xml.StructureTransducer;

public abstract class FormalDefinitionTransducer<T extends FormalDefinition> extends MetaTransducer<T> {

	
	
	

	@Override
	public T fromSubStructureList(List<Element> list) {
		List<Alphabet> alphs = retrieveAlphabets(list);
		return super.fromSubStructureList(list);
	}

	private List<Alphabet> retrieveAlphabets(List<Element> list) {
		
		for (Element e: list){
			
		}
		return null;
	}

	@Override
	public FormalDefinitionComponent[] getConstituentComponents(T structure) {
		return structure.getComponents();
	}


}
