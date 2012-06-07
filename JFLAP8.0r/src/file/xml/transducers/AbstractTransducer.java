package file.xml.transducers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jflap.file.xml.Transducer;
import jflap.file.xml.TransducerHelper;
import jflap.model.JFLAPModel;

public abstract class AbstractTransducer<T extends JFLAPModel> implements Transducer<T> {

	/** The instance of the document builder. */
	private static DocumentBuilder docBuilder;
	
	static {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			docBuilder = factory.newDocumentBuilder();
		} catch (Throwable e) {
			// Err, this shouldn't happen.
			System.err.println("ERROR!");
			e.printStackTrace();
		}
	}

	@Override
	public Document toDOM(T structure) {
		return toDOM(structure, newEmptyJFLAPDocument());
	}

	
	protected abstract Document toDOM(T structure, Document doc);


	/**
	 * Returns a new DOM document instance. This will have the structure tags
	 * with the type instantiated, and the processing instruction signifying
	 * that this is an XML document, but nothing else.
	 * 
	 * @return a new document
	 */
	private Document newEmptyJFLAPDocument() {
		Document doc = docBuilder.newDocument();
		// Add the processing instruction.
		/*
		 * doc.appendChild(doc.createProcessingInstruction ("xml",
		 * "version=\"1.0\""));
		 */
		// Add the credit string for JFLAP.
		doc.appendChild(TransducerHelper.createComment(doc, "Created with JFLAP "
				+ jflap.view.help.AboutBox.VERSION + "."));
		// Create and add the <structure> element.
		Element structureElement = TransducerHelper.createElement(doc, Transducer.STRUCTURE_NAME, null,
				null);
		doc.appendChild(structureElement);
		// Add the type of this document.
		structureElement.appendChild(TransducerHelper.createElement(doc, Transducer.STRUCTURE_TYPE_NAME,
				null, getType()));
		
		// Return the skeleton document.
		return doc;
	}
	
}
