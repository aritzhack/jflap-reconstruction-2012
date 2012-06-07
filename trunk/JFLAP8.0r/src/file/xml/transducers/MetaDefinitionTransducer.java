package file.xml.transducers;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class MetaDefinitionTransducer extends AbstractTransducer<MetaDefinition> {

	@Override
	public MetaDefinition fromStructureRoot(Element root) {
		MetaDefinition md = new MetaDefinition(false);
		NodeList structNodes = root.getElementsByTagName(Transducer.STRUCTURE_NAME);
		//TODO: ERROR CHECKING
		for (int i = 0; i < structNodes.getLength(); i++){
			Transducer t = TransducerHelper.getTransducer((Element) structNodes.item(i));
			FormalDefinition fd = (FormalDefinition) t.fromStructureRoot((Element) structNodes.item(i));
			md.add(fd);
		}
			
		
		return md;
	}

	@Override
	public String getType() {
		return "metaDef";
	}

	@Override
	protected Document toDOM(MetaDefinition structure, Document doc) {
		Element se = doc.getDocumentElement();
		for (FormalDefinition fd: structure.getDefinitions()){
			Document child = TransducerHelper.getTransducer(fd).toDOM(fd);
			se.appendChild(doc.importNode(child.getDocumentElement(), true));
		}
		return doc;
	}


}
