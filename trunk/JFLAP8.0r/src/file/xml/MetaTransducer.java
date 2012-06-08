package file.xml;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class MetaTransducer<T> extends StructureTransducer<T> {

	@Override
	public T fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildArray(root, STRUCTURE_TAG);
		return fromSubStructureList(list);
	}

	public T fromSubStructureList(List<Element> list) {
		List<Object> subComp = new ArrayList<Object>();
		for(Element child: list){
			StructureTransducer t = 
					TransducerFactory.getTransducer(child);
			subComp.add(t.fromStructureRoot(child));
		}
		return buildStructure(subComp.toArray());
	}

	public abstract T buildStructure(Object[] subComp);

	@Override
	public Element appendComponentsToRoot(Document doc, T structure, Element root) {
		
		for(Object o: getConstituentComponents(structure)){
			StructureTransducer t = TransducerFactory.getTransducerForStructure(o);
			root.appendChild(t.toXMLTree(doc, o));
		}
		return root;
	};
	
	public abstract Object[] getConstituentComponents(T structure);
	
}
