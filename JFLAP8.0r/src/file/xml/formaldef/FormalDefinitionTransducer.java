package file.xml.formaldef;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import file.xml.MetaTransducer;

public abstract class FormalDefinitionTransducer<T extends FormalDefinition> extends MetaTransducer<T> {

	@Override
	public FormalDefinitionComponent[] getConstituentComponents(T structure) {
		return structure.getComponents();
	}


}
