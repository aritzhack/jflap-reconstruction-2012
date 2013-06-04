package file.xml.formaldef.lsystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.StructureTransducer;
import file.xml.XMLHelper;

public class ParameterMapTransducer extends StructureTransducer<Map<String,String>>{
	private static final String PARAMETER_MAP_TAG = "parameter_map";
	private ParameterTransducer subTrans = new ParameterTransducer();
	
	
	@Override
	public String getTag() {
		return PARAMETER_MAP_TAG;
	}

	@Override
	public Map<String, String> fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, PARAMETER_TAG);
		Map<String, String> parameters = new HashMap<String, String>();
		
		for (int i = 0; i < list.size(); i++){
			Parameter current = subTrans.fromStructureRoot((Element)list.get(i));
			String name = current.getName();
			String value = current.getValue();
			
			if(name != null){
				if(value == null)
					value = "";
				parameters.put(name, value);
			}
		}
		return parameters;
	}

	@Override
	public Element appendComponentsToRoot(Document doc,
			Map<String, String> structure, Element root) {
		for(String name : structure.keySet()){
			Parameter param = new Parameter(name, structure.get(name));
			root.appendChild(subTrans.toXMLTree(doc, param));
		}
		return root;
	}

}
