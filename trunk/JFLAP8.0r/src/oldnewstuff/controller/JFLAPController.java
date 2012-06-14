package oldnewstuff.controller;


import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import oldnewstuff.action.save.SaveAction;
import oldnewstuff.action.save.SaveActionEvent;
import oldnewstuff.action.windows.CloseButton;
import oldnewstuff.universe.JFLAPUniverse;
import oldnewstuff.view.EditingView;
import oldnewstuff.view.JFLAPGUIResources;


import model.JFLAPConstants;



public class JFLAPController extends JFrame implements Dirtyable {

	private int myID;
	private JTabbedPane myTabbedPane;
	private File myFile;

	//-----------Initialize Controller-----------//
	public JFLAPController(int id, JComponent primary, File f) {
		super();
		setFile(f);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		myID = id;
		setUpForView(primary);
	}

	public void setFile(File f) {
		myFile = f;
	}

	public File getFile(){
		return myFile;
	}

	@Override
	public String getName() {
		return "JFLAP " + JFLAPConstants.VERSION + 
				" (" + myFile.getName() + ")";
	}

	private void setUpForView(JComponent primary) {
		this.setUpMenu(primary);
		initListeners();		
		myTabbedPane = new JTabbedPane();
		this.add(myTabbedPane);
		myTabbedPane.add(primary);

	}

	private void setUpMenu(JComponent primary) {
		JMenuBar bar = ControllerMenuFactory.createMenuForView(primary);
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
		return ((JFLAPController) obj).getID() == this.getID();
	}

	//-------------------------Adding new windows---------------------//


	public void addView(JComponent comp){
		myTabbedPane.add(comp);
		myTabbedPane.setSelectedComponent(comp);
		if (myTabbedPane.getTabCount() > 1)
			myTabbedPane.setEnabledAt(0, false);
		distributeTabChangedEvent();
	}

	public JComponent closeActiveTab(){
		return closeTab(myTabbedPane.getSelectedIndex());
	}
	

	//-----------Close controller and associated windows--------------//	

	/**
	 * Closes and saves all constituent windows if they can be saved.
	 * @param save
	 * @return true if this is the last controller open
	 */
	public void close(boolean save) {
		if (save && this.isDirty()){
			if (!promptAndSave()){
				return;
			}
		}
		JFLAPUniverse.unregisterController(this);
		this.dispose();
	}
	
	public JComponent closeTab(int i){
		JComponent comp = (JComponent) myTabbedPane.getComponentAt(i);
		myTabbedPane.remove(i);

		int n = myTabbedPane.getTabCount();
		if (n == 1)
			myTabbedPane.setEnabledAt(0, true);
		myTabbedPane.setSelectedIndex(n-1);
		distributeTabChangedEvent();
		
		return comp;
	}


	private boolean promptAndSave() {
		int result = JOptionPane.showConfirmDialog(this, "Save this window before closing?");
		if (result == 2) {
			return false;
		}
		if (result == 0){
			Action a = new SaveAction();
			a.actionPerformed(new SaveActionEvent(this, this.getFile()));
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

	public boolean isDirty() {
		boolean dirty = false;
		for (Component comp: this.getSubViews()){
			if (comp instanceof EditingView)
				dirty = ((EditingView) comp).isDirty() || dirty;
		}
		
		return dirty;
	}

	public Component[] getSubViews() {
		return myTabbedPane.getComponents();
	}

	public void updateViewAndRepaint() {
		
	}



}
