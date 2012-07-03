package view;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.grammar.Grammar;
import model.numbersets.control.SetsManager;
import view.grammar.GrammarView;
import view.numsets.SetView;
import debug.JFLAPDebug;
import file.xml.XMLCodec;

public class ViewFactory {

	private static Map<Class, Class<? extends Component>> myClassToComponent;

	static{
		myClassToComponent = new HashMap<Class, Class<? extends Component>>();
		myClassToComponent.put(Grammar.class, GrammarView.class);
		
		myClassToComponent.put(SetsManager.class, SetView.class);
	}
	
	public static Component createView(File f) {
		return createView(new XMLCodec().decode(f));
	}

	public static Component createView(Object decode) {
		JFLAPDebug.print("Creating View");
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
