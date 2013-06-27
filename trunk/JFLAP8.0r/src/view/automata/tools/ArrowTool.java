package view.automata.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.StartState;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;
import util.Point2DAdv;
import view.automata.AutomatonEditorPanel;
import view.automata.ControlMoveEvent;
import view.automata.StateMoveEvent;

/**
 * Tool for selection and editing of Automaton graphs.
 * 
 * @author Ian McMahon
 */
public class ArrowTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {

	private Object myObject;
	private Point2D myInitialPoint;
	private T myDef;
	private StateMenu myStateMenu;

	public ArrowTool(AutomatonEditorPanel<T, S> panel, T def) {
		super(panel);
		myDef = def;
		myObject = null;
		myStateMenu = new StateMenu();
	}

	@Override
	public String getToolTip() {
		return "Attribute Editor";
	}

	@Override
	public char getActivatingKey() {
		return 'a';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/arrow.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		panel.clearSelection();
		myObject = panel.objectAtPoint(e.getPoint());

		if (SwingUtilities.isLeftMouseButton(e)) {
			myInitialPoint = e.getPoint();

			if (isStateClicked(e) || isTransitionClicked(e)) {
				// States and Transitions must be selected
				panel.selectObject(myObject);

				if (myObject instanceof State) {
					myInitialPoint = new Point2DAdv(
							panel.getPointForVertex((State) myObject));
				} else
					panel.editTransition((S) myObject, false);
			} else if (myObject instanceof State[])
				myInitialPoint = panel.getControlPoint((State[]) myObject);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			if (isStateClicked(e))
				showStateMenu(e.getPoint());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (myObject instanceof State)
				// Move the state
				panel.moveState((State) myObject, e.getPoint());
			else if (myObject instanceof State[]) {
				// Move the control point
				State from = ((State[]) myObject)[0], to = ((State[]) myObject)[1];
				panel.moveCtrlPoint(from, to, e.getPoint());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();

		if (myObject instanceof State) {
			// Clear the selection and notify the undo keeper
			panel.clearSelection();
			getKeeper().registerChange(
					new StateMoveEvent(panel, myDef, (State) myObject,
							myInitialPoint, e.getPoint()));
		} else if (myObject instanceof State[]) {
			// Notify the undo keeper
			panel.getKeeper().registerChange(
					new ControlMoveEvent(panel, (State[]) myObject,
							myInitialPoint, e.getPoint()));
		}
		myInitialPoint = null;
	}

	private void showStateMenu(Point2D point) {
		myStateMenu.show(getPanel(), (int) point.getX(), (int) point.getY());
	}

	/**
	 * Returns true if the selected object is a State and the user clicked once.
	 */
	private boolean isStateClicked(MouseEvent e) {
		return e.getClickCount() == 1 && myObject instanceof State;
	}

	/**
	 * Returns true if the selected object is a Transition and the user double
	 * clicked.
	 */
	private boolean isTransitionClicked(MouseEvent e) {
		return e.getClickCount() == 2 && myObject instanceof Transition;
	}

	private class StateMenu extends JPopupMenu {

		public StateMenu() {

			if (myDef instanceof Acceptor)
				addFinalButton();

			addInitialButton();
//			changeLabel = new JMenuItem("Change Label");
//			deleteLabel = new JMenuItem("Clear Label");
//			deleteAllLabels = new JMenuItem("Clear All Labels");
			// editBlock = new JMenuItem("Edit Block");
			// copyBlock = new JMenuItem("Duplicate Block");
			// replaceSymbol = new JMenuItem("Replace Symbol");
			// setName = new JMenuItem("Set Name");
			// if (shouldAllowOnlyFinalStateChange())
			// return;
			// makeInitial.addActionListener(this);
			// changeLabel.addActionListener(this);
			// deleteLabel.addActionListener(this);
			// deleteAllLabels.addActionListener(this);
			// editBlock.addActionListener(this);
			// setName.addActionListener(this);
			// copyBlock.addActionListener(this);
			// replaceSymbol.addActionListener(this);
			// this.add(makeInitial);
			// this.add(changeLabel);
			// this.add(deleteLabel);
			// this.add(deleteAllLabels);
			// this.add(setName);
		}

		private void addFinalButton() {
			final JMenuItem makeFinal = new JCheckBoxMenuItem("Final");

			makeFinal.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Acceptor accept = (Acceptor) myDef;

					if (makeFinal.isSelected())
						accept.getFinalStateSet().add((State) myObject);
					else
						accept.getFinalStateSet().remove((State) myObject);
				}
			});
			this.add(makeFinal);
		}

		private void addInitialButton() {
			final JMenuItem makeInitial = new JCheckBoxMenuItem("Initial");

			makeInitial.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (makeInitial.isSelected())
						myDef.setStartState((State) myObject);
					else
						myDef.setStartState(null);
				}
			});
			this.add(makeInitial);
		}

		@Override
		public void show(Component invoker, int x, int y) {
			super.show(invoker, x, y);
		}
	}
}
