package view.sets;

/**
 * @author Peggy Li
 */


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import debug.JFLAPDebug;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.Magnifiable;
import view.action.sets.RemoveSetAction;
import view.environment.JFLAPEnvironment;
import view.sets.edit.EditingPanelFactory;

@SuppressWarnings("serial")
public class ActiveSetDisplay extends JPanel implements Magnifiable {

	private UndoKeeper myKeeper;

	private JLabel myTitle;
	private static JList myActiveSets;

	public ActiveSetDisplay(UndoKeeper keeper) {

		myKeeper = keeper;
		initComponents ();

		JScrollPane scroll = new JScrollPane(myActiveSets);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BorderLayout());
		this.add(myTitle, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);

		myActiveSets.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFLAPDebug.print(getSelectedSets().get(0).getSetAsString());

				if (getSelectedSets().size() == 1) {
					AbstractSet set = getSelectedSets().get(0);
					JFLAPUniverse.getActiveEnvironment().addSelectedComponent(
							EditingPanelFactory.createPanelFromSet(myKeeper, set));
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void initComponents () {
		myTitle = new JLabel("Active Sets", JLabel.CENTER);
		myActiveSets = new JList();
	}


	public void update(Object[] actives) {
		myActiveSets.setListData(actives);
		myActiveSets.repaint();
	}


	public ArrayList<AbstractSet> getSelectedSets() {
		ArrayList<AbstractSet> selected = new ArrayList<AbstractSet>();
		for (Object list : myActiveSets.getSelectedValues()) {
			AbstractSet set = SetsManager.ACTIVE_REGISTRY.getSetByName(list.toString());
			selected.add(set);
		}

		return selected;

	}

	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		for (Component c: this.getComponents())
			c.setFont(this.getFont().deriveFont(size));
	}

	/*
	 * Right-click menu for each list
	 */
	public JPopupMenu getPopupMenu(AbstractSet set) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new RemoveSetAction(myKeeper, set));
		return menu;
	}


	public void doClickResponse(AbstractSet set, MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3)
			getPopupMenu(set).show(e.getComponent(), e.getX(), e.getY());
	}



}
