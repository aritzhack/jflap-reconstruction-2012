package file.xml.formaldef.lsystem;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.XMLHelper;
import file.xml.XMLTransducer;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterValue;

/**
 * Transducer specific to the Parameter value of an LSystem.
 * 
 * @author Ian McMahon
 *
 */
public class ParameterValueTransducer implements XMLTransducer<ParameterValue>{

	@Override
	public ParameterValue fromStructureRoot(Element root) {
		List<Element> children = XMLHelper.getChildrenWithTag(root, getTag());
		String newValue = null;
		
		if(children != null){
			Element value = children.get(0);
			if(value != null){
				newValue = value.getTextContent();
			}
		}
		return new ParameterValue(newValue);
	}

	@Override
	public Element toXMLTree(Document doc, ParameterValue structure) {
		Element e = XMLHelper.createElement(doc, getTag(), structure, null);
		return e;
	}

	@Override
	public String getTag() {
		return PARAMETER_VALUE_TAG;
	}

	@Override
	public boolean matchesTag(String tag) {
		return tag == null ? false : tag.equals(getTag());
	}
}
