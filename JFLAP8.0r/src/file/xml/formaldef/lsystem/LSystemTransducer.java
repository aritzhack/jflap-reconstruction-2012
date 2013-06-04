package file.xml.formaldef.lsystem;

import java.util.List;
import java.util.Map;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.lsystem.LSystem;
import model.symbols.SymbolString;
import file.xml.XMLTransducer;
import file.xml.formaldef.FormalDefinitionTransducer;

public class LSystemTransducer extends FormalDefinitionTransducer<LSystem> {
	private static final String LSYSTEM_TAG = "lsystem";
	
	@Override
	public String getTag() {
		return LSYSTEM_TAG;
	}

	@Override
	public XMLTransducer getTransducerForStructureNode(String string,
			List<Alphabet> alphs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFunctionSetsToMap(Map<Object, XMLTransducer> map,
			LSystem structure) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public FormalDefinitionComponent[] getConstituentComponents(
			LSystem structure) {
		return super.getConstituentComponents(structure);
	}

	@Override
	public LSystem buildStructure(Object[] subComp) {
		return new LSystem(	retrieveTarget(SymbolString.class, subComp), 
							retrieveTarget(Grammar.class, subComp), 
							retrieveTarget(Map.class, subComp));
	}

}
