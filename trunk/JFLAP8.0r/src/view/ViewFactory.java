package view;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.grammar.Grammar;
import model.lsystem.NLSystem;
import view.grammar.GrammarView;
import view.lsystem.LSystemInputView;
import view.pumping.CFPumpingLemmaChooser;
import view.pumping.PumpingLemmaChooserPane;
import view.pumping.RegPumpingLemmaChooser;
import debug.JFLAPDebug;
import file.xml.XMLCodec;
//import view.sets.SetsView;

public class ViewFactory {

	private static Map<Class, Class<? extends Component>> myClassToComponent;

	static{
		myClassToComponent = new HashMap<Class, Class<? extends Component>>();
		myClassToComponent.put(Grammar.class, GrammarView.class);
		myClassToComponent.put(CFPumpingLemmaChooser.class, PumpingLemmaChooserPane.class);
		myClassToComponent.put(RegPumpingLemmaChooser.class, PumpingLemmaChooserPane.class);
		myClassToComponent.put(NLSystem.class, LSystemInputView.class);
//		myClassToComponent.put(SetsManager.class, SetsView.class);

	}
	
	public static Component createView(File f) {
		return createView(new XMLCodec().decode(f));
	}

	public static Component createView(Object decode) {
		Class argClass = decode.getClass();
		Class<? extends Component> viewClass = myClassToComponent.get(argClass);
		JFLAPDebug.print(argClass+" "+viewClass);
		try {
			return viewClass.getConstructor(argClass).newInstance(decode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}

}
