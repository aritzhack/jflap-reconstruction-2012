package view.sets;

/**
 * @author Peggy Li
 */


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiableList;
import util.view.magnify.MagnifiablePanel;
import view.action.sets.RemoveSetAction;
import view.sets.edit.EditingPanelFactory;

@SuppressWarnings("serial")
public class ActiveSetDisplay extends MagnifiablePanel {

	private UndoKeeper myKeeper;

	private MagnifiableLabel myTitle;
	private static MagnifiableList myActiveSets;

	public ActiveSetDisplay(UndoKeeper keeper) {

		myKeeper = keeper;
		initComponents ();

		JScrollPane scroll = new JScrollPane(myActiveSets);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(myTitle);
		myTitle.setAlignmentX(CENTER_ALIGNMENT);
		
		this.add(scroll);
		myActiveSets.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				doClickResponse(getSelectedSets().get(0), e);
			}

		});

	}

	private void initComponents () {
		myTitle = new MagnifiableLabel("Active Sets", JFLAPPreferences.getDefaultTextSize());
		myActiveSets = new MagnifiableList(JFLAPPreferences.getDefaultTextSize());
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


	/*
	 * Right-click menu for each list
	 */
	public JPopupMenu getPopupMenu(AbstractSet set) {
		JPopupMenu menu = new JPopupMenu();
		menu.add((Action) new RemoveSetAction(myKeeper, set));
		return menu;
	}


	public void doClickResponse(AbstractSet set, MouseEvent e) {
		if (set == null)	return;
		// right click for delete
		if (e.getButton() == MouseEvent.BUTTON3)
			getPopupMenu(set).show(e.getComponent(), e.getX(), e.getY());
		// double click for edit
		if (e.getClickCount() == 2) {
//			JFLAPUniverse.getActiveEnvironment().addSelectedComponent(
//					EditingPanelFactory.createPanelFromSet(myKeeper, set));

//			SetDefinitionPanel source = new SetDefinitionPanel(myKeeper);
//			source.createFromExistingSet(set);
//			FinishConstructionAction fin = new FinishConstructionAction(myKeeper, new ModifyState(source, set));
			JFLAPUniverse.getActiveEnvironment().addSelectedComponent(EditingPanelFactory.createPanelFromSet(myKeeper, set));
		
		}
	}


}
