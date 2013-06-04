package file.xml.formaldef.lsystem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.StructureTransducer;
import file.xml.formaldef.components.SingleNodeTransducer;

public class SingleStringTransducer extends SingleNodeTransducer<String>{

	public SingleStringTransducer(String tag) {
		super(tag);
	}

	@Override
	public Object extractData(String structure) {
		return structure;
	}

	@Override
	public String createInstance(String text) {
		if(text == null)
			text = "";
		return text;
	}
}
