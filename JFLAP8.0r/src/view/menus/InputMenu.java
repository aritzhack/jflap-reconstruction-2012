package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import model.undo.UndoKeeper;
import view.action.grammar.BruteParseAction;
import view.action.grammar.CYKParseAction;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;

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
		UndoKeeper keeper;
		if (view instanceof GrammarView){
			GrammarView v = (GrammarView) view;
			this.add(new BruteParseAction(v));
			this.add(new CYKParseAction(v));
		}
	}

}
