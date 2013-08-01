package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import view.action.automata.CombineAutomataAction;
import view.action.automata.FSAtoRegGrammarAction;
import view.action.automata.PDAtoCFGAction;
import view.action.automata.TMtoUnrestrictedGrammarAction;
import view.action.automata.TrapStateAction;
import view.action.grammar.conversion.RegGrammarToFSAAction;
import view.automata.views.AutomatonView;
import view.automata.views.FSAView;
import view.automata.views.MultiTapeTMView;
import view.automata.views.PDAView;
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
		
		if(!(view instanceof GrammarView || view instanceof AutomatonView
//				|| view instanceof RegexView
				))
			return;
		setVisible(true);
		
		if(view instanceof GrammarView){
			GrammarView v = (GrammarView) view;
			this.add(new RegGrammarToFSAAction(v));
		}
		
		if(view instanceof AutomatonView){
			AutomatonView v = (AutomatonView) view;
			if(view instanceof FSAView){
				this.add(new TrapStateAction((FSAView) v));
				this.add(new FSAtoRegGrammarAction((FSAView) v));
			}
			if(view instanceof PDAView)
				this.add(new PDAtoCFGAction((PDAView) v));
			if(view instanceof MultiTapeTMView)
				this.add(new TMtoUnrestrictedGrammarAction((MultiTapeTMView) v));
			this.add(new CombineAutomataAction(v));
		}
		
//		if(view instanceof RegexView){
//			
//		}
	}
}
