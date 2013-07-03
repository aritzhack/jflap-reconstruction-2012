package view.automata;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.Transition;
import model.undo.UndoKeeper;
import view.automata.tools.ArrowTool;
import view.automata.tools.DeleteTool;
import view.automata.tools.StateTool;
import view.automata.tools.ToolBar;
import view.automata.tools.TransitionTool;
import view.formaldef.BasicFormalDefinitionView;
import view.undoing.UndoPanel;

public class AutomataView<T extends Automaton<S>, S extends Transition<S>>
		extends BasicFormalDefinitionView<T> {
	private static final Dimension AUTOMATON_SIZE = new Dimension(500, 600);

	public AutomataView(T model) {
		this(model, new UndoKeeper(), true);
	}
	
	public AutomataView(T model, UndoKeeper keeper, boolean editable) {
		super(model, keeper, editable);
		setPreferredSize(AUTOMATON_SIZE);
		
		JScrollPane pane = getScroller();
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.revalidate();
		repaint();
	}

	@Override
	public JComponent createCentralPanel(T model, UndoKeeper keeper,
			boolean editable) {
		return new AutomatonEditorPanel<T, S>(model, keeper, editable);
	}

	@Override
	public String getName() {
		return "Automaton Editor";
	}

	@Override
	public void repaint() {
		//Needed for validating the bounds of the Automaton
//		if (getScroller() != null)
//			getScroller().revalidate();
		super.repaint();
	}
	
	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		repaint();
	}

	@Override
	public Component createToolbar(T definition, UndoKeeper keeper) {
		// TODO: figure this out :p
		return createDefaultToolbar(definition, keeper);
	}
	
	private Component createDefaultToolbar(T definition, UndoKeeper keeper) {
		AutomatonEditorPanel<T, S> panel = (AutomatonEditorPanel<T, S>) getCentralPanel();

		ArrowTool<T, S> arrow = new ArrowTool<T, S>(panel, definition);
		StateTool<T, S> state = new StateTool<T, S>(panel, definition);
		TransitionTool<T, S> trans = new TransitionTool<T, S>(panel);
		DeleteTool<T, S> delete = new DeleteTool<T, S>(panel);

		panel.setTool(arrow);
		ToolBar bar = new ToolBar(arrow, state, trans, delete);
		bar.addToolListener(panel);
		bar.add(new UndoPanel(keeper));
		return bar;
	}

}
