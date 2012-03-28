package util;

import java.util.Iterator;

import model.formaldef.symbols.Symbol;

public class StringUtil {

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
	
	
}
