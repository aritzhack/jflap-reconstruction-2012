package file.xml;

import java.util.List;
import java.util.Map;

import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachine;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import file.xml.formaldef.automata.AutomatonTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;

public class TuringMachineTransducer extends AutomatonTransducer<MultiTapeTuringMachine> {

	@Override
	public String getTag() {
		return "turing";
	}

	@Override
	public TransitionSetTransducer createTransitionFuncTransducer(
			List<Alphabet> alphs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiTapeTuringMachine buildStructure(Object[] subComp) {
		// TODO Auto-generated method stub
		return null;
	}

}
