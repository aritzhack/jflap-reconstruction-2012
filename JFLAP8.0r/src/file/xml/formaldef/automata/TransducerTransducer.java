package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import file.xml.XMLTransducer;
import file.xml.formaldef.components.FunctionSetTransducer;
import file.xml.formaldef.components.specific.output.OutputFunctionSetTransducer;
import model.automata.acceptors.Acceptor;
import model.automata.transducers.Transducer;
import model.formaldef.components.alphabets.Alphabet;

public abstract class TransducerTransducer<T extends Transducer> extends AutomatonTransducer<T>{

	@Override
	public XMLTransducer getTransducerForStructureNode(String s,
			List<Alphabet> alphs) {
		if (s == OUTPUT_FUNC_SET)
			return createOutputSetTransducer(alphs);
		return super.getTransducerForStructureNode(s, alphs);
	}

	public abstract OutputFunctionSetTransducer createOutputSetTransducer(List<Alphabet> alphs);

	@Override
	public void addFunctionSets(Map<Object,XMLTransducer> map, T structure) {
		super.addFunctionSets(map, structure);
		map.put(structure.getOutputFunctionSet(), 
				createOutputSetTransducer(null));
	};

}