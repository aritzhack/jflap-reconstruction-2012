package file.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import file.DataException;

public class TransducerHelper {

	
	
	
	
	/**
	 * Convenience function for creating elements.
	 * 
	 * @param document
	 *            the DOM document we're creating the element in
	 * @param tagname
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
	public static Element createElement(Document document, String tagname,
			Map<String, Object> attributes, Object text) {
		// Create the new element.
        tagname = tagname.replaceAll("'", "");
        tagname = tagname.replaceAll("&", "");
        tagname = tagname.replaceAll("\"", "");
        tagname = tagname.replaceAll("<", "");
        tagname = tagname.replaceAll(">", "");
        tagname = tagname.replaceAll(" ", "");

        
      //  System.out.println("TAG NAME = "+tagname);
		Element element = document.createElement(tagname);
		// Set the attributes.
		if (attributes != null) {
			Iterator it = attributes.keySet().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();
				String value = attributes.get(name).toString();
				element.setAttribute(name, value);
			}
		}
		// Add the text element.
		if (text != null)
			element.appendChild(document.createTextNode(text.toString()));
		return element;
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
	public static Comment createComment(Document document, String comment) {
		return document.createComment(comment);
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
		NodeList structureNodes = root.getElementsByTagName(Transducer.STRUCTURE_TYPE_NAME);
		
		if (structureNodes.getLength() > 1)
			throw new DataException("Multiple type nodes \n" +
										"exist in this structure");
		
		Node n = structureNodes.item(0);
		String type = ((Text) n.getChildNodes().item(0)).getData();
		
		
		return TransducerFactory.getTransducerForTag(type);
		
	}
	
	public static <T> Transducer<T> getTransducer(T structure) {
		return TransducerFactory.getTransducerForModel(structure);
	}

	
	/**
	 * Creates and appends a child node with a tag and value
	 * @param ele = parent node
	 * @param tag = tag of new child node
	 * @param value = value of new child node (i.e. <CODE> Node.setNodeValue(contents) </CODE>)
	 * @param doc = element source
	 */
	public static void appendChildNode(Element ele, String tag, Object value, Document doc) {
		Node n = createElement(doc, tag, null, value == null ? null : value.toString());
		ele.appendChild(n);
	}
	
	
	
}
