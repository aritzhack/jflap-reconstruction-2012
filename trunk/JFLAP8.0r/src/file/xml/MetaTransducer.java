package file.xml;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class MetaTransducer<T> extends StructureTransducer<T> {

	@Override
	public T fromStructureRoot(Element root) {
		NodeList list = root.getElementsByTagName(STRUCTURE_NAME);
		Object[] subComp = new  Object[list.getLength()];
		for(int i =0; i < list.getLength(); i++){
			Element child = (Element) list.item(i);
			StructureTransducer t = 
					TransducerFactory.getTransducer(child);
			subComp[i] = t.fromStructureRoot(child);
		}
		return buildStructure(subComp);
	}

	public abstract T buildStructure(Object[] subComp);

	@Override
	public Element appendComponentsToRoot(T structure, Element root) {
		
		for(Object o: getConstituentComponents(structure)){
			StructureTransducer t = TransducerFactory.getTransducerForStructure(o);
			root.appendChild(t.toXMLTree(o));
		}
		return root;
	};
	
	public abstract Object[] getConstituentComponents(T structure);
	
}
