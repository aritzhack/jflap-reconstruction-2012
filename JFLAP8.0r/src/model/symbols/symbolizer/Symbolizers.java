package model.symbols.symbolizer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.regex.RegularExpression;
import model.regex.RegularExpressionGrammar;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class Symbolizers {

	private static Map<Class<? extends FormalDefinition>, 
	Class<? extends DefaultSymbolizer>> myDefaultMap;
	private static Map<Class<? extends FormalDefinition>, 
	Class<? extends CustomSymbolizer>> myCustomMap;

	static{
		myDefaultMap = new HashMap<Class<? extends FormalDefinition>, 
				Class<? extends DefaultSymbolizer>>();
		myCustomMap = new HashMap<Class<? extends FormalDefinition>, 
				Class<? extends CustomSymbolizer>>();

		//add Grammar symbolizers
		myDefaultMap.put(Grammar.class, GrammarDefaultSymbolizer.class);
		myCustomMap.put(Grammar.class, GrammarCustomSymbolizer.class);

		//add Regex symbolizers
		myDefaultMap.put(RegularExpression.class, RegExDefaultSymbolizer.class);
		myCustomMap.put(RegularExpression.class, RegExCustomSymbolizer.class);


	}

	/**
	 * Performs a blind default symbolizing of the input string.
	 * This means that the string will simply be split into individual
	 * character symbols, and will all of of the {@link Symbol} class.
	 * @param in
	 * @return
	 */
	public static SymbolString symbolize(String in){
		DefaultSymbolizer symbolizer = new DefaultSymbolizer();
		return symbolizer.symbolize(in);
	}

	public static SymbolString symbolize(String in, FormalDefinition fd) {
		Symbolizer s = getSymbolizer(fd);
		return s.symbolize(in);
	}

	public static SymbolString symbolize(String in, Alphabet ... alphs) {
		boolean isCustom = intuitMode(alphs);
		Symbolizer s;
		if (isCustom)
			s= new CustomSymbolizer(alphs);
		else
			s = new DefaultSymbolizer(alphs);
		return s.symbolize(in);
	}


	public static Symbolizer getSymbolizer(FormalDefinition def) {
		return getSymbolizer(def,intuitMode(def.getAlphabets()));
	}

	public static Symbolizer getSymbolizerForMode(FormalDefinition def) {
		return getSymbolizer(def, JFLAPPreferences.isCustomMode());

	}

	private static boolean intuitMode(Alphabet ... alphs){
		for (Alphabet alph: alphs){
			for (Symbol s: alph){
				if (s.getString().length() > 1)
					return JFLAPPreferences.CUSTOM_MODE;
			}
		}

		return JFLAPPreferences.DEFAULT_MODE;
	}

	private static Symbolizer getSymbolizer(FormalDefinition def, boolean isCustom){
		if (isCustom)
			return getCustomSymbolizer(def);
		return getDefaultSymbolizer(def);
	}

	public static DefaultSymbolizer getDefaultSymbolizer(FormalDefinition def) {
		return instantiate(myDefaultMap, def, DefaultSymbolizer.class);
	}

	public static CustomSymbolizer getCustomSymbolizer(FormalDefinition def) {
		return instantiate(myCustomMap, def, CustomSymbolizer.class);
	}

	private static <T extends Symbolizer> T instantiate(
			Map<Class<? extends FormalDefinition>, Class<? extends T>> map,
			FormalDefinition def,
			Class<T> defaultClazz) {
		Class<? extends T> clazz = map.get(def.getClass());
		if (clazz == null)
			clazz = defaultClazz;
		try {
			return clazz.getConstructor(FormalDefinition.class).newInstance(def);
		} catch (Exception e) {

		} 
		try {
			return clazz.getConstructor(def.getClass()).newInstance(def);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SymbolizingException("Failure instantiating a " + clazz.getSimpleName() + 
					" from a " + def.getDescriptionName());
		} 
	}

}
