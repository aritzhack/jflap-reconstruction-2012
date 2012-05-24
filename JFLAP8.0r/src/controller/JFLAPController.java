package controller;


import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import universe.JFLAPUniverse;
import view.JFLAPGUIResources;
import view.JFLAPView;
import action.windows.CloseButton;


public class JFLAPController extends JFrame {

	private int myID;
	private JTabbedPane myTabbedPane;

	//-----------Initialize Controller-----------//
	public JFLAPController(int id, JComponent primaryView) {
		super("JFLAP");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		myID = id;
		setUpForView(primaryView);
	}

	private void setUpForView(JComponent primaryView) {
		setUpMenu(primaryView);
		initListeners();		
		myTabbedPane = new JTabbedPane();
		myTabbedPane.add(primaryView);
		this.add(myTabbedPane);
	}

	private void setUpMenu(JComponent primaryView) {
		JMenuBar bar = ControllerMenuFactory.createMenuForView(primaryView);
		bar.add(Box.createGlue());
		bar.add(new CloseButton(this));
		this.setJMenuBar(bar);
	}

	private void initListeners() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close(true);
			}

		});
	}

	private void distributeTabChangedEvent() {
		this.firePropertyChange(JFLAPGUIResources.TAB_CHANGED, null, null);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof JFLAPController) && ((JFLAPController) obj).getID() == this.getID();
	}

	//-------------------------Adding new windows---------------------//


	public void addView(JComponent comp){
		myTabbedPane.add(comp);
		myTabbedPane.setSelectedComponent(comp);
		if (myTabbedPane.getTabCount() > 1)
			myTabbedPane.setEnabledAt(0, false);
		distributeTabChangedEvent();
	}
	
	




	//-----------Close controller and associated windows--------------//	

	/**
	 * Closes and saves all constituent windows if they can be saved.
	 * @param save
	 * @return true if this is the last controller open
	 */
	public void close(boolean save) {
		while (myTabbedPane.getTabCount() > 0)
			closeActiveTab(save);
	}

	public void closeActiveTab(boolean save) {
		int i = this.closeTab(myTabbedPane.getSelectedIndex(), save);
		if (i == -1){
			JFLAPUniverse.unregisterController(this);
			this.dispose();
		}
	}

	
	public boolean canCloseATab() {
		return myTabbedPane.getTabCount() > 1;
	}

	/**
	 * Closes the tab at that index, prompts for a save,
	 * and then sets the selected tab to the last one
	 * in the JTabbedPane
	 * 
	 * @param selectedIndex
	 * @param save
	 * @return the index of the newly selected pane
	 */
	private int closeTab(int selectedIndex, boolean save) {
		Component c = myTabbedPane.getTabComponentAt(selectedIndex);
		if (c instanceof JFLAPView && save){
			if (!promptAndSave((JFLAPView) c))
				return selectedIndex;
		}
		myTabbedPane.remove(selectedIndex);
		
		int n = myTabbedPane.getTabCount();
		if (n == 1)
			myTabbedPane.setEnabledAt(n-1, true);
		myTabbedPane.setSelectedIndex(n-1);
		distributeTabChangedEvent();
		return n-1;
	}

	private boolean promptAndSave(JFLAPView view) {
		int result = JOptionPane.showConfirmDialog(this, "Save " + view.getName()
				+ " before closing?");
		if (result == 2) {
			return false;
		}
		if (result == 0){
			view.save();
		}
		return true;
	}

	//--------------Getters and Setters-----------//

	/**
	 * Retrieves the integer ID associated with this controller.
	 * @return
	 */
	public int getID() {
		return myID;
	}



}
