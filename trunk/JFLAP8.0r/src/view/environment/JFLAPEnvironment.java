package view.environment;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import debug.JFLAPDebug;

import util.JFLAPConstants;
import view.EditingPanel;
import view.ViewFactory;
import view.menus.JFLAPMenuBar;

import file.XMLFileChooser;
import file.xml.XMLCodec;
import file.xml.XMLTransducer;

public class JFLAPEnvironment extends JFrame{

	
	private File myFile;
	private JTabbedPane myTabbedPane;
	private Component myPrimaryView;
	private boolean amDirty;
	private int myID;
	private List<TabChangeListener> myListeners;
	
	
	public JFLAPEnvironment(Object model, int id){
		this(ViewFactory.createView(model), id);
	}
	
	public JFLAPEnvironment(File f, int id){
		this(ViewFactory.createView(f), id);
		setFile(f);
	}
	
	public JFLAPEnvironment(Component component, int id){
		super(JFLAPConstants.VERSION_STRING);

		myListeners = new ArrayList<TabChangeListener>();
		
		myID = id;
		myTabbedPane = new SpecialTabbedPane();
		this.add(myTabbedPane);
		myPrimaryView = component;
		addView(component);
		JFLAPMenuBar menu = MenuFactory.createMenu(this);
		this.setJMenuBar(menu);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JFLAPEnvironment.this.close(true);
			}

		});
		this.pack();
		this.setVisible(true);
	}
	
	public void addTabListener(TabChangeListener menu) {
		myListeners.add(menu);
	}

	private void setFile(File f) {
		JFLAPDebug.print("Set File: " + f.getName());
		myFile= f;
		this.setTitle(JFLAPConstants.VERSION_STRING + "(" + myFile.getName() + ")");
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JFLAPEnvironment)
			return this.getID() == ((JFLAPEnvironment) obj).getID();
		return super.equals(obj);
	}
	
	public int getID() {
		return myID;
	}

	public boolean close(boolean save) {
		if (save && this.isDirty()){
			int result = JOptionPane.showConfirmDialog(this, 
					"Save changes before closing?");
			if (result == 2) {
				return false;
			}
			if (result == 0){
				this.save(false);
			}
		}
		this.dispose();
		return true;
	}
	
	public boolean save(boolean saveAs) {
		if (saveAs || myFile == null){
			XMLFileChooser chooser = new XMLFileChooser();
			int n = chooser.showSaveDialog(this);
			if (n == JFileChooser.CANCEL_OPTION ||
					n == JFileChooser.ERROR_OPTION)
				return false;
			myFile = chooser.getSelectedFile();
		}
		XMLCodec codec = new XMLCodec();
		//TODO: why is it encoding the environment?
		codec.encode(this, myFile, null);
		amDirty = false;
		for(EditingPanel ep: getEditingPanels()){
			ep.setDirty(false);
		}

		return true;
		
	}

	
	private EditingPanel[] getEditingPanels(){
		List<EditingPanel> editPanels = new ArrayList<EditingPanel>();
		for (int i = 0; i< myTabbedPane.getTabCount(); i++){
			Component c = myTabbedPane.getTabComponentAt(i);
			if (c instanceof EditingPanel)
				editPanels.add((EditingPanel) c);
				
		}
		return editPanels.toArray(new EditingPanel[0]);
	}
	
	public boolean isDirty() {
		if (amDirty) return true;
		for(EditingPanel ep: getEditingPanels()){
			if (ep.isDirty())
				return true;
		}
		return false;
	}

	public Component getPrimaryView(){
		return myPrimaryView;
	}
	
	public void addView(Component component) {
		myTabbedPane.add(component);
//		myTabbedPane.setSelectedComponent(component);

		if (component instanceof EditingPanel)
			amDirty = true;
		distributeTabChangedEvent();
		myTabbedPane.revalidate();
		update();
	}
	
	public void addSelectedComponent(Component component){		
		addView(component);
		myTabbedPane.setSelectedIndex(myTabbedPane.getTabCount()-1);
	}
	
	private void distributeTabChangedEvent() {
		for (TabChangeListener l: myListeners){
			l.tabChanged(new TabChangedEvent(myTabbedPane.getSelectedComponent(), 
												myTabbedPane.getTabCount()));
		}
	}

	public void closeActiveTab() {
		closeTab(myTabbedPane.getSelectedIndex());
	}
	
	public void closeTab(int i) {
		Component c = myTabbedPane.getTabComponentAt(i);
		myTabbedPane.remove(i);
		if (c instanceof EditingPanel)
			amDirty = true;
		distributeTabChangedEvent();
		myTabbedPane.revalidate();
		myTabbedPane.setSelectedIndex(myTabbedPane.getTabCount()-1);
		this.repaint();
	}

	public void update() {
		updatePrimaryPanel();
		this.repaint();
	}

	private void updatePrimaryPanel() {
		boolean enabled = myTabbedPane.getTabCount() == 1;
		if (myPrimaryView instanceof EditingPanel)
			((EditingPanel) myPrimaryView).setEditable(enabled);
		myTabbedPane.setEnabledAt(0, enabled);
	}

	@Override
	public String getName() {
		String file = "";
		if (myFile != null)
			file = " (" + myFile.getName() + ")";
		return super.getName() + file;
	}
	
	
	private class SpecialTabbedPane extends JTabbedPane{
		
		@Override
		public void setSelectedComponent(Component c) {
			setSelectedIndex(indexOfTabComponent(c));
			
		}
		
		@Override
		public void setSelectedIndex(int index) {
			super.setSelectedIndex(index);
			JFLAPEnvironment.this.update();
			distributeTabChangedEvent();
		}
	}


	public int getTabCount() {
		return myTabbedPane.getTabCount();
	}

	public boolean hasFile() {
		return myFile != null;
	}

	public String getFileName() {
		if (!hasFile()) return "";
		return myFile.getName();
	}

	public Component getCurrentView() {
		return myTabbedPane.getSelectedComponent();
	}
	
	@Override
	public String toString() {
		return "Environment: " + this.getFileName() + " | id: " + this.getID();
	}
	
}
