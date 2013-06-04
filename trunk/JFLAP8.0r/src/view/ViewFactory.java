package view;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.grammar.Grammar;
import model.lsystem.LSystem;
import model.pumping.ContextFreePumpingLemma;
import model.pumping.PumpingLemma;
import model.pumping.RegularPumpingLemma;
import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.lsystem.LSystemInputView;
import view.pumping.CFPumpingLemmaChooser;
import view.pumping.CompCFPumpingLemmaInputPane;
import view.pumping.CompRegPumpingLemmaInputPane;
import view.pumping.HumanCFPumpingLemmaInputPane;
import view.pumping.HumanRegPumpingLemmaInputPane;
import view.pumping.PumpingLemmaChooserPane;
import view.pumping.PumpingLemmaInputPane;
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
		myClassToComponent.put(LSystem.class, LSystemInputView.class);
//		myClassToComponent.put(SetsManager.class, SetsView.class);

	}
	
	public static Component createView(File f) {
		return createView(new XMLCodec().decode(f));
	}

	public static Component createView(Object decode) {		
		if(decode instanceof PumpingLemma)
			return createPumpingLemmaView((PumpingLemma) decode);
		
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
	
	public static Component createPumpingLemmaView(PumpingLemma pl){
		String player = pl.getFirstPlayer();
		
		if(pl instanceof RegularPumpingLemma){            
            RegularPumpingLemma reg = (RegularPumpingLemma) pl;
            PumpingLemmaInputPane inputPane;
            if (player.equals(PumpingLemma.COMPUTER))
            	inputPane = new CompRegPumpingLemmaInputPane(reg);                    	
            else
            	inputPane = new HumanRegPumpingLemmaInputPane(reg);
            inputPane.update();
            return inputPane;
		}
		
		//Context-Free Pumping Lemma
		
         ContextFreePumpingLemma cf = (ContextFreePumpingLemma) pl;
         PumpingLemmaInputPane inputPane;            
         if (player.equals(PumpingLemma.COMPUTER))            	
         	inputPane = new CompCFPumpingLemmaInputPane(cf);
         else
         	inputPane = new HumanCFPumpingLemmaInputPane(cf);
         inputPane.update();
         return inputPane;
	}

}
