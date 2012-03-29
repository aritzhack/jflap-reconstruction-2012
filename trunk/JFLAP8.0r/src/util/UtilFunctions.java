package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import model.formaldef.components.alphabets.symbols.Symbol;

public class UtilFunctions {

	public static String createDelimitedString(Iterable iterable, String delimiter){
		Iterator<Symbol> iter = iterable.iterator();
		String string = "";
		while (iter.hasNext()){
			string += iter.next().toString();
			if (iter.hasNext()) 
				string += delimiter;
		}
		return string;
		
	}
	
	
	public static <T> T[] combineArrays(T[] array, T ... toAdd){
		ArrayList<T> toReturn = new ArrayList<T>(Arrays.asList(array));
		toReturn.addAll(Arrays.asList(toAdd));
		return toReturn.toArray(array);
	}
	
	
}
