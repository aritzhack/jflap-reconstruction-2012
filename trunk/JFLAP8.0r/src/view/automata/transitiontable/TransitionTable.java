package view.automata.transitiontable;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.change.events.AddEvent;
import model.change.events.SetToEvent;
import view.automata.AutomatonEditorPanel;
import view.grammar.productions.LambdaRemovingEditor;

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
				S trans = myPanel.createTransition(myTrans.getFromState(),
						myTrans.getToState());
				myPanel.editTransition(trans, true);
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
			S t = modifyTransition();
			if (t != null){
				S temp = myTrans.copy();
				myTrans.setTo(t);
				
				TransitionSet<S> transitions = myAutomaton.getTransitions();
				if(!transitions.contains(myTrans)){
					transitions.add(myTrans);
					myPanel.getKeeper().registerChange(new AddEvent<S>(transitions, myTrans));
				}
				else
					myPanel.getKeeper().registerChange(new SetToEvent<S>(myTrans, temp, t.copy()));
			}
		}

		myPanel.clearSelection();
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
