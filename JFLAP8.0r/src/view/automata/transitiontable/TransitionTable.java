package view.automata.transitiontable;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import debug.JFLAPDebug;

import view.automata.AutomatonEditorPanel;
import view.grammar.productions.LambdaRemovingEditor;

import model.automata.Automaton;
import model.automata.Transition;

public abstract class TransitionTable<T extends Automaton<S>, S extends Transition<S>>
		extends JTable {

	private T myAutomaton;
	private S myTrans;
	private AutomatonEditorPanel<T, S> myPanel;
	private MouseAdapter myListener;

	public TransitionTable(int row, int col, S trans, T automaton,
			AutomatonEditorPanel<T, S> panel) {
		super(row, col);
		myTrans = trans;
		myAutomaton = automaton;
		myPanel = panel;
		myPanel.add(this);

		setModel(createModel());
		setGridColor(Color.gray);
		setBorder(new EtchedBorder());

		TableCellEditor edit = new LambdaRemovingEditor();
		for (int i = 0; i < getColumnCount(); i++) {
			getColumnModel().getColumn(i).setCellEditor(edit);
		}

		myListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				stopEditing(false);
			}
		};
		myPanel.addMouseListener(myListener);
	}

	@Override
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
			int condition, boolean pressed) {
		if (ks.getKeyCode() == KeyEvent.VK_ENTER && !ks.isOnKeyRelease()) {
			stopEditing(false);
			if (e.isShiftDown()) {
				myPanel.createTransition(myTrans.getFromState(),
						myTrans.getToState());
			}
			return true;
		} else if (ks.getKeyCode() == KeyEvent.VK_ESCAPE) {
			stopEditing(true);
			return true;
		}
		return super.processKeyBinding(ks, e, condition, pressed);
	}

	private void stopEditing(boolean cancel) {
		try {
			getCellEditor().stopCellEditing();
		} catch (NullPointerException e) {
		}

		if (!cancel) {
			TableModel oldModel = createModel();
			S t = modifyTransition();
			if (t != null)
				myTrans.setTo(t);
		}
		// if (this instanceof TMTransitionCreator) {
		// TMTransitionCreator stop = (TMTransitionCreator) this;
		// stop.setBlockTransition(false);
		// }
		myPanel.removeMouseListener(myListener);
		myPanel.remove(this);
		myPanel.validate();
		myPanel.repaint();
		myPanel.requestFocus();
	}

	public S getTransition() {
		return myTrans;
	}

	public T getAutomaton() {
		return myAutomaton;
	}

	public abstract TableModel createModel();

	public abstract S modifyTransition();

}
