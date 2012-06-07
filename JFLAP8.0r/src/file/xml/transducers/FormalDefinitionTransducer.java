package file.xml.transducers;

import java.lang.reflect.ParameterizedType;

import jflap.debug.JFLAPDebug;
import jflap.file.xml.AlphabetXMLFactory;
import jflap.file.xml.Transducer;
import jflap.file.xml.TransducerHelper;
import jflap.model.ModelMapping;
import jflap.model.formaldef.FormalDefinition;
import jflap.model.formaldef.alphabets.Alphabet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public abstract class FormalDefinitionTransducer<T extends FormalDefinition> extends AbstractTransducer<T> {

	public static final String COMPLETE_TAG = "complete";
	
	@Override
	public T fromStructureRoot(Element root) {
		T fd = null;
		try {
			fd = ModelMapping.getClassForTransducer(this).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		fd.eraseAlphabets();
		for (Alphabet a: AlphabetXMLFactory.getAlphabets(root, fd.getClass())){
			fd.add(a);
		}
		Element specific_root = (Element) root.getElementsByTagName(getStructureSpecificTag()).item(0);

		fd = fromSpecificContentRoot(specific_root, fd);
//		fd.trimAlphabets(); No longer auto-trimming, because this prevents users from saving incomplete files,
//						    i.e. files that will but have not yet used all symbols
		return fd;
	}

	
	public abstract T fromSpecificContentRoot(Element root, T def);
	
	/**
	 * Given a JFLAP formaldefinition, this will return the corresponding DOM encoding
	 * of the structure.
	 * 
	 * @param structure
	 *            the JFLAP formaldef to encode
	 * @return a DOM document instance
	 */
	@Override
	public Document toDOM(T structure, Document doc) {
		appendAlphabets(structure, doc);
		Element de = doc.getDocumentElement();
		
		Element content_root = TransducerHelper.createElement(doc, getStructureSpecificTag(), null, null);
		de.appendChild(content_root);
		
		appendStructure(structure, content_root, doc);
		return doc;
	}

	public abstract String getStructureSpecificTag();


	private Document appendAlphabets(T structure, Document doc) {
		Element se = doc.getDocumentElement();
//		se.setAttribute(COMPLETE_TAG, structure.isComplete().isTrue() ? "1" : "0");
		for (Alphabet a: structure.getAlphabets()){
			se.appendChild(AlphabetXMLFactory.createAlphabetElement(doc, a));
		}
		return doc;
	}

	protected abstract Document appendStructure(T structure, Element content_root, Document doc);

}
