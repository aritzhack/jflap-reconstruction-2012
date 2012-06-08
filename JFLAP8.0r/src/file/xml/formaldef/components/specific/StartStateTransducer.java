package file.xml.formaldef.components.specific;

import model.automata.StartState;
import model.automata.State;

import org.w3c.dom.Element;

import file.xml.StructureTransducer;
import file.xml.formaldef.components.SingleValueTransducer;

public class StartStateTransducer extends SingleValueTransducer<StartState> {


	@Override
	public String getTag() {
		return "start_state";
	}

	@Override
	public StartState createInstance(String s) {
		int i = s.indexOf(" ");
		int id = Integer.valueOf(s.substring(0, i));
		String name = s.substring(i+1);
		return new StartState(new State(name, id));
	}

	@Override
	public Object retrieveData(StartState structure) {
		State s = structure.toStateObject();
		int id = s.getID();
		String name = s.getName();
		return id + " " + name;
	}

}
