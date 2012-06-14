package file.xml.formaldef.components;

import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.Copyable;


import file.xml.StructureTransducer;

public abstract class SetComponentTransducer<T extends SetSubComponent<T>> extends StructureTransducer<SetComponent<T>> {

	@Override
	public SetComponent<T> fromStructureRoot(Element root) {
		NodeList list = root.getElementsByTagName(getSubNodeTag());
		SetComponent<T> comp = createEmptyComponent();
		for (int i = 0; i < list.getLength(); i++){
			comp.add(decodeSubNode((Element)list.item(i)));
		}
		return comp;
	}


	public abstract T decodeSubNode(Element item);

	public abstract SetComponent<T> createEmptyComponent();

	public abstract String getSubNodeTag();
	
	@Override
	public Element appendComponentsToRoot(Document doc,
			SetComponent<T> structure, Element root) {
		for (T item: structure){
			root.appendChild(createSubNode(doc, item));
		}
		return root;
	}


	public abstract Element createSubNode(Document doc, T item);


}
