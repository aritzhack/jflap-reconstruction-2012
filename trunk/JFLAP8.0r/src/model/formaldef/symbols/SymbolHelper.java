package model.formaldef.symbols;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SymbolHelper {

	public static boolean[] createSimilarityArray(String string, String sub) {
		boolean[] comp = new boolean[string.length()];
		char[] chars = string.toCharArray();
		for (int i = 0; i<comp.length; i++){
			comp[i] = sub.indexOf(chars[i]) >= 0; //if the char is in the substring, then the boolean is true
		}
		
		return comp;
	}
	
	public static boolean checkSimilarity(String string, String sub) {
		boolean[] common = SymbolHelper.createSimilarityArray(string, sub);
		for (int i = 0; i < string.length(); i++){
			boolean b = true;
			String s = "";
			for(int j = i; j < string.length(); j++){
				if (!(b = b && common[j])) break; //if the current char is not common, end this iteration
				s += string.charAt(j);
				if (SymbolHelper.containsCharacters(s, sub.toCharArray()))
					return true;
			}
			
		}
		return false;
	}
	
	public static boolean containsSubstring(Symbol s1, Symbol s2){
		return s1.getString().contains(s2.getString());
	}
	
	//TODO: The efficiency here could be improved if it becomes an issue - 
		//get comp from the "contains char function"
//		private static boolean containsSubstring(String string, String sub) {
//			if (!SymbolHelper.containsCharacters(string, sub.toCharArray()))
//				return false;
//			else if (string.length() < sub.length())
//				return false;
//			else{
//				return checkSimilarity(string, sub);
//			}
//			return string.contains(sub);
//		}
		
		public static boolean containsCharacters(Symbol symbol, char ... chars) {
			
			return SymbolHelper.containsCharacters(symbol.getString(), chars);
		}


		public static boolean containsCharacters(String string, char ... chars) {
			for (char c: chars){
				if (string.indexOf(c) < 0)
					return false;
			}
			return true;
		}

		public static <T extends Symbol> T trim(T s1, int i) {
			try {
				return (T) s1.getClass().getConstructors()[0].newInstance(s1.getString().substring(i, s1.length()-i));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error trimming the symbol " + s1);
			} 
		}

		public static <T extends Symbol> Set<T> getAllSymbolSetOfClass(
				Class<T> clazz, List<SymbolString> strings) {
			
			Set<T> symbols = new HashSet<T>();
			for (SymbolString s: strings){
				for (Symbol sym: s){
					if (sym.getClass().isAssignableFrom(clazz))
						symbols.add((T) sym);
				}
			}
			
			return symbols;
		}
	
		
		
		
}
