package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import model.undo.UndoKeeper;
import view.action.grammar.LanguageGeneratorAction;
import view.action.grammar.parse.BruteParseAction;
import view.action.grammar.parse.CYKParseAction;
import view.action.lsystem.LSystemRenderAction;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;
import view.lsystem.LSystemInputView;
import view.lsystem.LSystemRenderView;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;

public class InputMenu extends JMenu implements TabChangeListener {
	
	public InputMenu(JFLAPEnvironment e){
		super("Input");
		e.addTabListener(this);
		this.update(e.getCurrentView());
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		update(e.getCurrentView());
	}
	
	private void update(Component view) {
		this.removeAll();
		this.setVisible(true);
		if (view instanceof GrammarView){
			GrammarView v = (GrammarView) view;
			this.add(new BruteParseAction(v));
			this.add(new CYKParseAction(v));
			this.add(new LanguageGeneratorAction(v.getDefinition()));
		}
		if (view instanceof LSystemInputView){
			LSystemInputView v = (LSystemInputView) view;
			this.add(new LSystemRenderAction(v));	
		}
		if (view instanceof LSystemRenderView || view instanceof PumpingLemmaChooserView
				|| view instanceof PumpingLemmaInputView)
			this.setVisible(false);
	}

}
