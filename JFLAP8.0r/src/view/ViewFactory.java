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
import view.pumping.CompCFPumpingLemmaInputView;
import view.pumping.CompRegPumpingLemmaInputView;
import view.pumping.HumanCFPumpingLemmaInputView;
import view.pumping.HumanRegPumpingLemmaInputView;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;
import view.pumping.RegPumpingLemmaChooser;
import debug.JFLAPDebug;
import file.xml.XMLCodec;
//import view.sets.SetsView;

public class ViewFactory {

	private static Map<Class, Class<? extends Component>> myClassToComponent;

	static{
		myClassToComponent = new HashMap<Class, Class<? extends Component>>();
		myClassToComponent.put(Grammar.class, GrammarView.class);
		myClassToComponent.put(CFPumpingLemmaChooser.class, PumpingLemmaChooserView.class);
		myClassToComponent.put(RegPumpingLemmaChooser.class, PumpingLemmaChooserView.class);
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
            PumpingLemmaInputView inputPane;
            if (player.equals(PumpingLemma.COMPUTER))
            	inputPane = new CompRegPumpingLemmaInputView(reg);                    	
            else
            	inputPane = new HumanRegPumpingLemmaInputView(reg);
            inputPane.update();
            return inputPane;
		}
		
		//Context-Free Pumping Lemma
		
         ContextFreePumpingLemma cf = (ContextFreePumpingLemma) pl;
         PumpingLemmaInputView inputPane;            
         if (player.equals(PumpingLemma.COMPUTER))            	
         	inputPane = new CompCFPumpingLemmaInputView(cf);
         else
         	inputPane = new HumanCFPumpingLemmaInputView(cf);
         inputPane.update();
         return inputPane;
	}

}
