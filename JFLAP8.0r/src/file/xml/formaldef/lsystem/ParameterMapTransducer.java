package file.xml.formaldef.lsystem;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;

import file.xml.XMLHelper;
import file.xml.XMLTransducer;
import file.xml.formaldef.lsystem.wrapperclasses.Parameter;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterMap;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterName;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterValue;

/**
 * Transducer specific to the Parameter Map of an LSystem.
 * 
 * @author Ian McMahon
 *
 */
public class ParameterMapTransducer implements XMLTransducer<ParameterMap>{
	private ParameterTransducer subTrans = new ParameterTransducer();
	
	
	@Override
	public String getTag() {
		return PARAMETER_MAP_TAG;
	}

	@Override
	public ParameterMap fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, PARAMETER_TAG);
		ParameterMap parameters = new ParameterMap();
		JFLAPDebug.print(list.size());
		for (int i = 0; i < list.size(); i++){
			Parameter current = subTrans.fromStructureRoot(list.get(i));
			ParameterName name = current.getName();
			ParameterValue value = current.getValue();
			
			if(name != null && name.toString() != null){
				if(value == null || value.toString() == null)
					value = new ParameterValue("");
				parameters.put(name, value);
			}
		}
		return parameters;
	}

	@Override
	public Element toXMLTree(Document doc, ParameterMap structure) {
		Element root = XMLHelper.createElement(doc, PARAMETER_MAP_TAG, null, null);
		
		for(String name : structure.keySet()){
			String value = structure.get(name);
			Parameter param = new Parameter(name, value);
			
			root.appendChild(subTrans.toXMLTree(doc, param));
		}
		return root;
	}

	@Override
	public boolean matchesTag(String tag) {
		return getTag().equals(tag);
	}

}
