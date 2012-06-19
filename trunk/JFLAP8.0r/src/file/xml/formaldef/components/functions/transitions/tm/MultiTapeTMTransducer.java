package file.xml.formaldef.components.functions.transitions.tm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.automata.InputAlphabet;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.XMLTransducer;
import file.xml.formaldef.components.SingleNodeTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionTransducer;
import file.xml.formaldef.components.symbols.SymbolTransducer;

public class MultiTapeTMTransducer extends TransitionTransducer<MultiTapeTMTransition> {


	private static final Pattern p = Pattern.compile("("+READ_TAG + "|" + WRITE_TAG + ")([0-9]+)");

	public MultiTapeTMTransducer(InputAlphabet input, TapeAlphabet tape) {
		super(input, tape);
	}

	@Override
	public Map<String, Object> addOtherLabelComponentsToMap(
			Map<String, Object> base, MultiTapeTMTransition trans) {
		for(int i = 0; i < trans.getNumTapes(); i++){
			base.put(READ_TAG + i, trans.getRead(0));
			base.put(WRITE_TAG + i, trans.getWrite(0));
			base.put(MOVE_TAG + i, trans.getMove(0));
		}
		base.put(TAPE_NUM, trans.getNumTapes());
		return base;
	}

	@Override
	public XMLTransducer getTransducerForTag(String tag) {
		if (p.matcher(tag).find())
			return new SymbolTransducer(tag);
		else if (tag.equals(TAPE_NUM))
			return new IntegerTransducer(tag);
		return super.getTransducerForTag(tag);
	}
	
	@Override
	public MultiTapeTMTransition createTransition(State from, State to,
			Map<String, Object> remaining) {
		Symbol[] read = retrieveArray(READ_TAG, remaining, Symbol.class);
		Symbol[] write = retrieveArray(READ_TAG, remaining, Symbol.class);
		TuringMachineMove[] move = 
				retrieveArray(MOVE_TAG, remaining, TuringMachineMove.class);

		
		return new MultiTapeTMTransition(from, to, read, write, move);
	}

	private <T> T[] retrieveArray(String readTag, Map<String, Object> remaining, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		
		return null;
	}


}
