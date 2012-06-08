package file.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import file.DataException;

public class XMLHelper {

	
	/** The instance of the document builder. */
	private static DocumentBuilder docBuilder;
	private static Document docFactory;
	
	static {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			docBuilder = factory.newDocumentBuilder();
			docFactory = docBuilder.newDocument();
		} catch (Throwable e) {
			// Err, this shouldn't happen.
			System.err.println("ERROR!");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Convenience function for creating elements.
	 * 
	 * @param document
	 *            the DOM document we're creating the element in
	 * @param name
	 *            the tagname for the element
	 * @param attributes
	 *            a map from attribute names to attributes, or <CODE>null</CODE>
	 *            if this element should have no attributes
	 * @param text
	 *            the text for the element, which will be made into a text node
	 *            and added as a child of the created element, or <CODE>null</CODE>
	 *            if the element should have no children
	 * @return a new element
	 */
	public static Element createElement(String name, Object text, 
									Map<String, Object> attributes ) {
        Element e = docFactory.createElement(name);
		// Set the attributes.
		if (attributes != null) {
			for(Entry<String, Object> entry: attributes.entrySet()){
				e.setAttribute(entry.getKey(), entry.getValue().toString());
			}
		}
		// Add the text element.
		if (text != null)
			e.appendChild(createTextNode(text.toString()));
		return e;
	}
	
	public static Node createTextNode(String string) {
		return docFactory.createTextNode(string);
	}

	/**
	 * Given a node, returns a map where, for each immediate child of this node
	 * that is an element named A with a Text node with data B, there is an
	 * entry in the map from A to B. If A contains no textual node, A maps to
	 * <TT>null</TT>. If the element A appears more than once, the last
	 * element encountered is respected.
	 * 
	 * @param node
	 *            the node to get the map for
	 * @return the map from children element names to their textual contents
	 */
	public static Map<String, String> elementsToText(Node node) {
		NodeList children = node.getChildNodes();
		Map<String, String> e2t = new HashMap<String, String>();
		for (int i = 0; i < children.getLength(); i++) {
			Node c = children.item(i);
			if (c.getNodeType() != Node.ELEMENT_NODE)
				continue;
			String elementName = ((Element) c).getTagName();
			String text = containedText(c);
			e2t.put(elementName, text);
		}
		return e2t;
	}
	
	
	/**
	 * Convenience function for creating comments.
	 * 
	 * @param document
	 *            the DOM document we're creating the comment in
	 * @param comment
	 *            the comment text
	 * @return a comment node
	 */
	public static Comment createComment(String comment) {
		return docFactory.createComment(comment);
	}
	
	
	
	/**
	 * Given an node, returns the child text node of this element.
	 * 
	 * @param node
	 *            the node to get the text node from
	 * @return the text node that is a child of this node, or <CODE>null</CODE>
	 *         if there is no such child
	 */
	public static String containedText(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node c = children.item(i);
			if (c.getNodeType() != Node.TEXT_NODE)
				continue;
			return ((Text) c).getData();
		}
		return null;
	}

	
	/**
	 * Creates and appends a child node with a tag and value
	 * @param ele = parent node
	 * @param tag = tag of new child node
	 * @param value = value of new child node (i.e. <CODE> Node.setNodeValue(contents) </CODE>)
	 * @param doc = element source
	 */
	public static void appendChildNode(Element ele, String tag, Object value) {
		Node n = createElement(tag, value, null);
		ele.appendChild(n);
	}

	public static Document newDocument() {
		return docBuilder.newDocument();
	}

	public static Document parse(File f) throws SAXException, IOException {
		return docBuilder.parse(f);
	}
	
	
	
}
