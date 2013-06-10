package view.environment;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import oldnewstuff.main.JFLAP;

import model.pumping.PumpingLemma;

import universe.JFLAPUniverse;
import util.JFLAPConstants;
import view.EditingPanel;
import view.ViewFactory;
import view.formaldef.FormalDefinitionView;
import view.menus.JFLAPMenuBar;
import view.pumping.CFPumpingLemmaChooser;
import view.pumping.CompCFPumpingLemmaInputView;
import view.pumping.ComputerFirstView;
import view.pumping.HumanCFPumpingLemmaInputView;
import view.pumping.PumpingLemmaChooser;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;
import view.pumping.RegPumpingLemmaChooser;
import debug.JFLAPDebug;
import file.SavingException;
import file.XMLFileChooser;
import file.xml.XMLCodec;

public class JFLAPEnvironment extends JFrame {

	private File myFile;
	private JTabbedPane myTabbedPane;
	private Component myPrimaryView;
	private boolean amDirty;
	private int myID;
	private List<TabChangeListener> myListeners;

	public JFLAPEnvironment(Object model, int id) {
		this(ViewFactory.createView(model), id);
	}

	public JFLAPEnvironment(File f, int id) {
		this(ViewFactory.createView(f), id);
		setFile(f);
		if (myPrimaryView instanceof PumpingLemmaInputView)
			addPLChooser();
	}

	public JFLAPEnvironment(Component component, int id) {
		super(JFLAPConstants.VERSION_STRING);

		myListeners = new ArrayList<TabChangeListener>();

		myID = id;
		myTabbedPane = new SpecialTabbedPane();
		this.add(myTabbedPane);
		myPrimaryView = component;
		addSelectedComponent(component);
		JFLAPMenuBar menu = MenuFactory.createMenu(this);
		this.setJMenuBar(menu);
		// I believe this is needed to make sure it doesn't close on
		// a cancelled save.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!JFLAPEnvironment.this.close(true)) {
					return;
				}

			}

		});
		this.pack();
		this.setVisible(true);
//		setSize(component.getPreferredSize());
		setVisible(true);
	}

	public void addTabListener(TabChangeListener menu) {
		myListeners.add(menu);
	}

	private void setFile(File f) {
		myFile = f;
		this.setTitle(JFLAPConstants.VERSION_STRING + "(" + myFile.getName()
				+ ")");
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
		// Should check if there's any actual information to save
		if (save && this.isDirty() && getSavableObject() != null) {
			int result = JOptionPane.showConfirmDialog(this,
					"Save changes before closing?");
			if (result == JOptionPane.CLOSED_OPTION
					|| result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
			if (result == JOptionPane.YES_OPTION) {
				if (!this.save(false))
					return false;
			}
		}
		this.dispose();
		return true;
	}

	public boolean save(boolean saveAs) {
		// Used to change myFile back to its current state if save is cancelled
		File temp = myFile;

		// The getSavableObject() may need to be modified
		Object obj = getSavableObject();
		if (obj == null) {
			throw new SavingException("No data to save");
		}

		if (saveAs || myFile == null) {
			XMLFileChooser chooser = new XMLFileChooser();
			int n = chooser.showSaveDialog(this);
			if (n == JFileChooser.CANCEL_OPTION
					|| n == JFileChooser.ERROR_OPTION)
				return false;
			myFile = chooser.getSelectedFile();
		}

		// This may need to change based on directories/batches or if
		// getParent() returns null
		if (!myFile.getName().endsWith(".jff"))
			myFile = new File(myFile.getParent(), myFile.getName() + ".jff");

		// If file exists, ask about overwriting
		if (myFile.exists()) {
			int n = JOptionPane.showConfirmDialog(this,
					"File already exists. Overwrite file?");
			if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.NO_OPTION
					|| n == JOptionPane.CLOSED_OPTION) {
				myFile = temp;
				return false;
			}
		}
		this.setTitle(JFLAPConstants.VERSION_STRING + "(" + myFile.getName()
				+ ")");
		XMLCodec codec = new XMLCodec();

		codec.encode(obj, myFile, null);
		amDirty = false;
		for (EditingPanel ep : getEditingPanels()) {
			ep.setDirty(false);
		}

		return true;

	}

	public Object getSavableObject() {
		for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
			Component c = myTabbedPane.getComponent(i);
			if (c != null) {
				if (c instanceof FormalDefinitionView) {
					return ((FormalDefinitionView) c).getDefinition();
				} else if (c instanceof PumpingLemmaInputView) {
					return ((PumpingLemmaInputView) c).getLemma();
				}
			}
		}
		return null;
	}

	private EditingPanel[] getEditingPanels() {
		List<EditingPanel> editPanels = new ArrayList<EditingPanel>();
		for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
			Component c = myTabbedPane.getComponent(i);
			if (c instanceof EditingPanel)
				editPanels.add((EditingPanel) c);

		}
		return editPanels.toArray(new EditingPanel[0]);
	}

	public boolean isDirty() {
		if (amDirty)
			return true;
		for (EditingPanel ep : getEditingPanels()) {
			if (ep.isDirty())
				return true;
		}
		return false;
	}

	public Component getPrimaryView() {
		return myPrimaryView;
	}

	public void addView(Component component) {
		myTabbedPane.add(component);
		// myTabbedPane.setSelectedComponent(component);

		if (component instanceof EditingPanel)
			amDirty = true;
		distributeTabChangedEvent();
		myTabbedPane.revalidate();
		update();
	}

	public void addSelectedComponent(Component component) {
		addView(component);
		myTabbedPane.setSelectedIndex(myTabbedPane.getTabCount() - 1);
	}

	private void distributeTabChangedEvent() {
		for (TabChangeListener l : myListeners) {
			l.tabChanged(new TabChangedEvent(myTabbedPane
					.getSelectedComponent(), myTabbedPane.getTabCount()));
		}
	}

	public void closeActiveTab() {
		closeTab(myTabbedPane.getSelectedIndex());
	}

	public void closeTab(int i) {
		Component c = myTabbedPane.getComponent(i);
		if (c instanceof PumpingLemmaInputView) {
			int result = JOptionPane.showConfirmDialog(this,
					"Save changes before closing?");
			if (result == JOptionPane.CLOSED_OPTION
					|| result == JOptionPane.CANCEL_OPTION) {
				return;
			}
			if (result == JOptionPane.YES_OPTION) {
				if (!this.save(false))
					return;
			}
		}
		
		if (c instanceof EditingPanel)
			amDirty = true;
		myTabbedPane.remove(i);
		distributeTabChangedEvent();
		myTabbedPane.revalidate();
		myTabbedPane.setSelectedIndex(myTabbedPane.getTabCount() - 1);
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

	private class SpecialTabbedPane extends JTabbedPane {

		@Override
		public void setSelectedComponent(Component c) {
			setSelectedIndex(indexOfTabComponent(c));

		}

		@Override
		public void setSelectedIndex(int index) {
			super.setSelectedIndex(index);
			Dimension newSize = this.getComponentAt(index).getPreferredSize();
			
			JFLAPEnvironment.this.setPreferredSize(newSize);
			JFLAPEnvironment.this.setSize(newSize);
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
		if (!hasFile())
			return "";
		return myFile.getName();
	}

	public Component getCurrentView() {
		return myTabbedPane.getSelectedComponent();
	}

	@Override
	public String toString() {
		return "Environment: " + this.getFileName() + " | id: " + this.getID();
	}

	private void addPLChooser() {
		PumpingLemmaChooser plc;
		PumpingLemmaChooserView pane;

		if (myPrimaryView instanceof CompCFPumpingLemmaInputView
				|| myPrimaryView instanceof HumanCFPumpingLemmaInputView){
			plc = new CFPumpingLemmaChooser();
			pane = new PumpingLemmaChooserView((CFPumpingLemmaChooser) plc);
		}
		else{
			plc = new RegPumpingLemmaChooser();
			pane = new PumpingLemmaChooserView((RegPumpingLemmaChooser) plc);
		}
		
		// As PumpingLemmaChooserView instatiates as Human First by default:
		if (myPrimaryView instanceof ComputerFirstView)
			pane.setComputerFirst();

		// Place the PLCV under the active tab
		myTabbedPane.removeAll();
		myTabbedPane.add(pane, 0);
		myTabbedPane.add(myPrimaryView, 1);
		myTabbedPane.setSelectedIndex(1);
		myPrimaryView = pane;	//To follow the hierarchy as before, with the chooser as the "Primary View"
		
		myTabbedPane.revalidate();
	}
}
