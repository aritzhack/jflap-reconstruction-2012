package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import view.action.automata.CombineAutomataAction;
import view.action.automata.TrapStateAction;
import view.automata.views.AutomataView;
import view.automata.views.FSAView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;

public class ConvertMenu extends JMenu implements TabChangeListener {

	public ConvertMenu(JFLAPEnvironment e) {
		super("Convert");
		e.addTabListener(this);
		this.update(e.getCurrentView());
	}
	@Override
	public void tabChanged(TabChangedEvent e) {
		update(e.getCurrentView());
	}

	private void update(Component view) {
		this.removeAll();
		setVisible(false);
		
		if(!(view instanceof GrammarView || view instanceof AutomataView
//				|| view instanceof RegexView
				))
			return;
		setVisible(true);
		
		if(view instanceof GrammarView){
			//TODO: Grammar conversions
		}
		
		if(view instanceof AutomataView){
			AutomataView v = (AutomataView) view;
			if(view instanceof FSAView)
				this.add(new TrapStateAction((FSAView) view));
			this.add(new CombineAutomataAction(v));
		}
		
//		if(view instanceof RegexView){
//			
//		}
	}
}
