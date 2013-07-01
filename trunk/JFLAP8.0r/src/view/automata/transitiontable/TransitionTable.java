package view.automata.transitiontable;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.change.events.AddEvent;
import model.change.events.SetToEvent;
import view.automata.AutomatonEditorPanel;
import view.grammar.productions.LambdaRemovingEditor;

/**
 * Table that will pop up to implement the editing of transitions in an
 * automaton graph.
 * 
 * @author Ian McMahon
 */
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

		// Set the look and model
		setModel(createModel());
		setGridColor(Color.gray);
		setBorder(new EtchedBorder());

		TableCellEditor edit = new LambdaRemovingEditor();
		for (int i = 0; i < getColumnCount(); i++) {
			getColumnModel().getColumn(i).setCellEditor(edit);
		}

		// Add mouse listener to the AutomatonEditorPanel to stop editing.
		myListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				stopEditing(false);
			}
		};
		myPanel.addMouseListener(myListener);
	}

	public abstract TableModel createModel();

	public abstract S modifyTransition();

	@Override
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
			int condition, boolean pressed) {
		if (ks.getKeyCode() == KeyEvent.VK_ENTER && !ks.isOnKeyRelease()) {
			stopEditing(false);

			if (e.isShiftDown()) {
				// Keep creating transitions
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

	/** Returns the table's transition. */
	public S getTransition() {
		return myTrans;
	}

	/** Returns the table's Automaton. */
	public T getAutomaton() {
		return myAutomaton;
	}

	/**
	 * Stops editing the table and modifies the transition accordingly. Will add
	 * events to the UndoKeeper if applicable.
	 */
	@SuppressWarnings("unchecked")
	public void stopEditing(boolean cancel) {
		try {
			getCellEditor().stopCellEditing();
		} catch (NullPointerException e) {
		}

		if (!cancel) {
			S t = modifyTransition();
			if (t != null) {
				TransitionSet<S> transitions = myAutomaton.getTransitions();
				S temp = myTrans.copy();

				boolean wasInTransitions = transitions.contains(temp);
				myTrans.setTo(t);

				if (!transitions.contains(myTrans)) {
					transitions.add(myTrans);
					myPanel.getKeeper().registerChange(
							new AddEvent<S>(transitions, myTrans){
								@Override
								public boolean undo() {
									myPanel.clearSelection();
									return super.undo();
								}
								
								@Override
								public boolean redo() {
									myPanel.clearSelection();
									return super.redo();
								}
							});
				} else if (isNotDuplicate(transitions, temp, wasInTransitions))
					myPanel.getKeeper().registerChange(
							new SetToEvent<S>(myTrans, temp, t.copy()){
								@Override
								public boolean undo() {
									myPanel.clearSelection();
									return super.undo();
								}
								
								@Override
								public boolean redo() {
									myPanel.clearSelection();
									return super.redo();
								}
							});
			}
		}
		removeSelf();
	}

	/**
	 * Clears, revalidates, and notifies the AutomatonEditorPanel that this
	 * Table is no longer needed.
	 */
	private void removeSelf() {
		myPanel.clearSelection();
		myPanel.removeMouseListener(myListener);
		myPanel.remove(this);
		myPanel.validate();
		myPanel.repaint();
		myPanel.requestFocus();
	}

	/**
	 * Returns true if myTransition differs from what it was before modification
	 * and there is no transition equal to it still in the TransitionSet.
	 */
	private boolean isNotDuplicate(TransitionSet<S> transitions, S temp,
			boolean wasInTransitions) {
		return !temp.equals(myTrans) && wasInTransitions
				&& !transitions.contains(temp);
	}
}
