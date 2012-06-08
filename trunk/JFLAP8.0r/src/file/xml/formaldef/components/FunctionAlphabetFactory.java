package file.xml.formaldef.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import file.xml.XMLTags;

import model.automata.InputAlphabet;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSTransition;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.StackAlphabet;
import model.automata.transducers.OutputAlphabet;
import model.automata.turing.TapeAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

@SuppressWarnings("unchecked")
public class FunctionAlphabetFactory implements XMLTags{

	private static Map<String, Class<? extends Alphabet>[]> MAP;
	
	static {
		MAP = new TreeMap<String, Class<? extends Alphabet>[]>();
		addMapping(TRANS_INPUT_TAG, InputAlphabet.class);
		addMapping(PDA_POP_TAG, StackAlphabet.class);
		addMapping(PDA_PUSH_TAG, StackAlphabet.class);
		addMapping(OUTPUT_TAG, OutputAlphabet.class);
		addMapping(TM_READ_TAG, TapeAlphabet.class);
		addMapping(TM_WRITE_TAG, TapeAlphabet.class);
		addMapping(RHS_TAG, VariableAlphabet.class, TerminalAlphabet.class);
		addMapping(LHS_TAG, VariableAlphabet.class, TerminalAlphabet.class);
	}

	private static void addMapping(String tag, Class<? extends Alphabet> ... clz) {
		MAP.put(tag, clz);
	}

	public static Alphabet[] discerneAlphabets(String tag, Alphabet ... alphs){
		Class<? extends Alphabet>[] applicable = MAP.get(tag);
		List<Alphabet> found = new ArrayList<Alphabet>();
		for (Alphabet a: alphs){
			for (Class<? extends Alphabet> clz: applicable){
				if (clz.isAssignableFrom(a.getClass()))
					found.add(a);
			}
		}
		return found.toArray(new Alphabet[0]);
	}

}
