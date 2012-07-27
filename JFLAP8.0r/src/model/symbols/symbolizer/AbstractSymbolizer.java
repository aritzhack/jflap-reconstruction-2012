package model.symbols.symbolizer;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.management.RuntimeErrorException;

import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;

import debug.JFLAPDebug;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class AbstractSymbolizer implements Symbolizer {

	
	private Alphabet[] myAlphs;
	private Map<String, Collection<Object>> myMap;

	public AbstractSymbolizer(FormalDefinition fd){
		this(fd.getAlphabets());
	}
	
	public AbstractSymbolizer(Alphabet ... alphs) {
		myAlphs = alphs;
		myMap = new HashMap<String, Collection<Object>>();
	}
	
	public Alphabet[] getAlphabets() {
		return myAlphs;
	}
	
	@Override
	public SymbolString symbolize(String in){
		myMap.clear();
		Collection<Object> extracted = new ArrayList<Object>();
		if (in==null) return null;
		for(String s:in.split(JFLAPPreferences.getSymbolStringDelimiter()))
			extracted.addAll(extractSymbols(s));
		SymbolString result = new SymbolString();
		for (Object o : extracted){
			if (o instanceof Symbol)
				result.add((Symbol) o);
			else
				result.addAll(symbolizeUnidentified((String) o));
		}
		return result;
	}

	private SymbolString symbolizeUnidentified(String unIDed){
		String[] split = splitToSymbols(unIDed);
		SymbolString ss = new SymbolString();
		for (String s: split){
			ss.add(toNewSymbol(s));
		}
		return ss;
	}

	public Symbol toNewSymbol(String s) {
		return new Symbol(s);
	}

	public abstract String[] splitToSymbols(String unIDed);

	private Collection<Object> extractSymbols(String in) {

		if (in == null ||in.length() == 0) 
			return new ArrayList<Object>();

		if (myMap.containsKey(in))
			return myMap.get(in);
		
		Alphabet[] alphs = this.getAlphabets();
		ArrayList<List<Object>> options = new ArrayList<List<Object>>();

		
		for (int i = in.length(); i > 0; i--){
			List<Object> temp = new ArrayList<Object>();
			Object sub = in.substring(0,i);
			loop: for (Alphabet alph: alphs){
				Symbol s = alph.getByString((String) sub);
				if (s != null){
					sub = s;
					break loop;
				}
			}
			temp.add(sub);
			
			String remaining = in.substring(i);
			temp.addAll(extractSymbols(remaining));
			options.add(temp);
		}
		
		List<Object> best = options.get(0);
		for (List<Object> option: options){
			if (stringCount(option) < stringCount(best))
				best = option;
		}
		
		myMap.put(in, best);
		return best;
	}

	private int stringCount(List<Object> option) {
		int count = 0;
		for (Object o: option){
			if (o instanceof String)
				count+= ((String) o).length();
		}
		return count;
	}
}

