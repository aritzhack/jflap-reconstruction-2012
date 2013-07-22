package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import view.action.automata.SimulateAction;
import view.action.grammar.LanguageGeneratorAction;
import view.action.grammar.parse.BruteParseAction;
import view.action.grammar.parse.CYKParseAction;
import view.action.lsystem.LSystemRenderAction;
import view.automata.views.AcceptorView;
import view.automata.views.AutomataView;
import view.automata.views.TuringMachineView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;
import view.grammar.LanguageGeneratorView;
import view.grammar.parsing.ParserView;
import view.lsystem.LSystemInputView;
import view.lsystem.LSystemRenderView;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;

public class InputMenu extends JMenu implements TabChangeListener {

	public InputMenu(JFLAPEnvironment e) {
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
		if (view instanceof LSystemRenderView
				|| view instanceof PumpingLemmaChooserView
				|| view instanceof PumpingLemmaInputView
				|| view instanceof ParserView
				|| view instanceof LanguageGeneratorView)
			this.setVisible(false);
		else {
			this.setVisible(true);

			if (view instanceof GrammarView) {
				GrammarView v = (GrammarView) view;
				this.add(new BruteParseAction(v));
				this.add(new CYKParseAction(v));
				this.add(new LanguageGeneratorAction(v.getDefinition()));
			}
			if (view instanceof LSystemInputView) {
				LSystemInputView v = (LSystemInputView) view;
				this.add(new LSystemRenderAction(v));
			}
			if (view instanceof AutomataView){
				AutomataView v = (AutomataView) view;
				this.add(new SimulateAction(v, true));
			}
			if (view instanceof AcceptorView && !(view instanceof TuringMachineView)){
				AcceptorView v = (AcceptorView) view;
				this.add(new SimulateAction(v, false));
//				this.add(new FastRunAction(v));
//				this.add(new MultipleRunAction(v));
			}
			if (view instanceof TuringMachineView){
				TuringMachineView v = (TuringMachineView) view;
//				this.add(new MultipleTransducerRunAction(v));
			}
		}
	}
}
