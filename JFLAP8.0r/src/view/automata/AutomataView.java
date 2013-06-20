package view.automata;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import model.automata.Automaton;
import model.automata.Transition;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import view.automata.tools.RedoTool;
import view.automata.tools.StateTool;
import view.automata.tools.ToolBar;
import view.automata.tools.UndoTool;
import view.formaldef.BasicFormalDefinitionView;
import view.undoing.UndoPanel;

public class AutomataView<T extends Automaton<S>, S extends Transition<S>>
		extends BasicFormalDefinitionView<T> {
	private static final Dimension AUTOMATON_SIZE = new Dimension(500, 600);

	public AutomataView(T model, UndoKeeper keeper, boolean editable) {
		super(model, keeper, editable);
		setPreferredSize(AUTOMATON_SIZE);
		JScrollPane pane = getScroller();
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	@Override
	public JComponent createCentralPanel(T model, UndoKeeper keeper,
			boolean editable) {
		//TODO: all this
		return new AutomatonEditorPanel<T, S>(model, keeper, editable);
	
	}

	@Override
	public String getName() {
		return "Automaton Editor";
	}
	
	@Override
	public Component createToolbar(T definition, UndoKeeper keeper) {
		// TODO: figure this out :p
		AutomatonEditorPanel<T, S> panel = (AutomatonEditorPanel<T, S>) getCentralPanel();
		
		StateTool<T, S> state = new StateTool<T, S>(panel);
		UndoTool undo = new UndoTool(keeper);
		RedoTool redo = new RedoTool(keeper);
		
		panel.setTool(state);
		ToolBar bar = new ToolBar(state, undo, redo);
		bar.addToolListener(panel);
		return bar;
	}

}
