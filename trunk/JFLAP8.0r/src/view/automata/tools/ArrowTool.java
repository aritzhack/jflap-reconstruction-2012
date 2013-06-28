package view.automata.tools;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.StartState;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.change.events.AddEvent;
import model.change.events.RemoveEvent;
import model.change.events.SetToEvent;
import model.change.events.StartStateSetEvent;
import model.undo.UndoKeeper;
import util.Point2DAdv;
import view.automata.AutomatonEditorPanel;
import view.automata.ControlMoveEvent;
import view.automata.StateMoveEvent;
import debug.JFLAPDebug;

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
	private Point2D myInitialControlPoint;
	private Rectangle mySelectionBounds;

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

			if (myObject != null) {
				panel.selectObject(myObject);

				if (isStateClicked(e))
					myInitialPoint = new Point2DAdv(
							panel.getPointForVertex((State) myObject));
				else if (myObject instanceof State[]) {
					State[] edge = (State[]) myObject;
					myInitialControlPoint = panel.getControlPoint(edge);
					for (S trans : myDef.getTransitions())
						if (trans.getFromState().equals(edge[0])
								&& trans.getToState().equals(edge[1]))
							panel.selectObject(trans);
				} else if (isTransitionClicked(e))
					panel.editTransition((S) myObject, false);
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			if (isStateClicked(e))
				showStateMenu(e.getPoint());
			else
				for (State s : myDef.getStates())
					panel.selectObject(s);
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
			} else if (myObject == null) {
				int nowX = e.getPoint().x;
				int nowY = e.getPoint().y;
				int leftX = (int) myInitialPoint.getX();
				int topY = (int) myInitialPoint.getY();
				if (nowX < leftX)
					leftX = nowX;
				if (nowY < topY)
					topY = nowY;
				mySelectionBounds = new Rectangle(leftX, topY,
						Math.abs(nowX - (int) myInitialPoint.getX()), Math.abs(nowY
								- (int) myInitialPoint.getY()));
				panel.repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (myObject instanceof State) {
				// Clear the selection and notify the undo keeper
				panel.clearSelection();
				getKeeper().registerChange(
						new StateMoveEvent(panel, myDef, (State) myObject,
								myInitialPoint, e.getPoint()));
			} else if (myObject instanceof State[]
					&& !myInitialPoint.equals(e.getPoint())) {
				// Notify the undo keeper
				panel.getKeeper().registerChange(
						new ControlMoveEvent(panel, (State[]) myObject,
								myInitialControlPoint, e.getPoint()));
			}
		}
		myInitialPoint = null;
		mySelectionBounds = null;
		panel.repaint();
		panel.requestFocus();
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

		private JCheckBoxMenuItem makeFinal;
		private JCheckBoxMenuItem makeInitial;
		private JMenuItem changeLabel;
		private JMenuItem deleteLabel;
		private JMenuItem deleteAllLabels;
		private JMenuItem editBlock;
		private JMenuItem copyBlock;
		private JMenuItem setName;

		public StateMenu() {
			if (myDef instanceof Acceptor)
				addFinalButton();

			addInitialButton();
			addLabelButtons();

			if (myDef instanceof BlockTuringMachine)
				addBlockButtons();
			addSetNameButton();
		}

		private void addFinalButton() {
			makeFinal = new JCheckBoxMenuItem("Final");

			makeFinal.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Acceptor accept = (Acceptor) myDef;
					FinalStateSet finalStates = accept.getFinalStateSet();
					UndoKeeper keeper = getKeeper();

					if (makeFinal.isSelected())
						keeper.applyAndListen(new AddEvent<State>(finalStates,
								(State) myObject));
					else
						keeper.applyAndListen(new RemoveEvent<State>(
								finalStates, (State) myObject));
				}
			});
			this.add(makeFinal);
		}

		private void addInitialButton() {
			makeInitial = new JCheckBoxMenuItem("Initial");

			makeInitial.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					UndoKeeper keeper = getKeeper();
					StartState start = myDef
							.getComponentOfClass(StartState.class);
					State internal = start.getState();

					if (makeInitial.isSelected())
						keeper.applyAndListen(new StartStateSetEvent(start,
								internal, (State) myObject));
					else
						keeper.applyAndListen(new StartStateSetEvent(start,
								internal, null));

				}
			});
			this.add(makeInitial);
		}

		private void addLabelButtons() {

			changeLabel = new JMenuItem("Change Label");
			changeLabel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFLAPDebug.print("Need to implement this!");
				}
			});
			deleteLabel = new JMenuItem("Clear Label");
			deleteLabel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFLAPDebug.print("Need to implement this!");
				}
			});
			deleteAllLabels = new JMenuItem("Clear All Labels");
			deleteAllLabels.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFLAPDebug.print("Need to implement this!");
				}
			});
			add(changeLabel);
			add(deleteLabel);
			add(deleteAllLabels);
		}

		private void addBlockButtons() {
			editBlock = new JMenuItem("Edit Block");
			editBlock.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFLAPDebug.print("Need to implement this!");
				}
			});
			copyBlock = new JMenuItem("Duplicate Block");
			copyBlock.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFLAPDebug.print("Need to implement this!");
				}
			});
			add(editBlock);
			add(copyBlock);
		}

		private void addSetNameButton() {
			setName = new JMenuItem("Set Name");
			setName.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					State state = (State) myObject;
					String oldName = state.getName();
					String name = (String) JOptionPane.showInputDialog(
							getPanel(), "Input a new name, or \n"
									+ "set blank to remove the name",
							"New Name", JOptionPane.QUESTION_MESSAGE, null,
							null, oldName);
					if (name == null || name.equals(oldName))
						return;
					State newState = new State(name, state.getID());
					getKeeper()
							.applyAndListen(
									new SetToEvent<State>(state, state.copy(),
											newState));
				}
			});
			add(setName);
		}

		@Override
		public void show(Component invoker, int x, int y) {
			super.show(invoker, x, y);
			if (makeFinal != null) // only happens when def instanceof Acceptor
				makeFinal.setSelected(Acceptor.isFinalState((Acceptor) myDef,
						(State) myObject));
			makeInitial.setSelected(Automaton.isStartState(myDef,
					(State) myObject));
		}

	}

	@Override
	public void draw(Graphics g) {
		if (mySelectionBounds != null)
			g.drawRect(mySelectionBounds.x, mySelectionBounds.y, mySelectionBounds.width, mySelectionBounds.height);
	}
}
