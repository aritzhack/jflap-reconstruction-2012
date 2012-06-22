package view;

import java.awt.Component;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

import view.grammar.GrammarView;

import model.grammar.Grammar;

import file.xml.XMLCodec;

public class ViewFactory {

	private static Map<Class, Class<? extends Component>> myClassToComponent;

	static{
		myClassToComponent = new TreeMap<Class, Class<? extends Component>>();
		myClassToComponent.put(Grammar.class, GrammarView.class);
	}
	
	public static Component createView(File f) {
		return createView(new XMLCodec().decode(f));
	}

	private static Component createView(Object decode) {
		Class argClass = decode.getClass();
		Class<? extends Component> viewClass = myClassToComponent.get(argClass);
		try {
			return viewClass.getConstructor(argClass).newInstance(decode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}

}
