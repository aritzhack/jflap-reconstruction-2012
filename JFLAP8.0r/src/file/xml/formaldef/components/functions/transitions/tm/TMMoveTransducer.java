package file.xml.formaldef.components.functions.transitions.tm;

import model.automata.turing.TuringMachineMove;
import file.xml.formaldef.components.SingleNodeTransducer;

public class TMMoveTransducer extends SingleNodeTransducer<TuringMachineMove> {

	public TMMoveTransducer() {
		super(MOVE_TAG);
	}

	@Override
	public Object extractData(TuringMachineMove move) {
		return move.char_abbr;
	}

	@Override
	public TuringMachineMove createInstance(String text) {
		for (TuringMachineMove move: TuringMachineMove.values()){
			if (text.startsWith(move.char_abbr + ""))
				return move;
		}
		return null;
	}


}
