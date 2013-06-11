package view.menus;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import debug.JFLAPDebug;

import view.action.file.ExitAction;
import view.action.file.OpenAction;
import view.action.file.SaveAction;
import view.action.file.SaveAsAction;
import view.action.file.imagesave.SaveGraphBMPAction;
import view.action.file.imagesave.SaveGraphGIFAction;
import view.action.file.imagesave.SaveGraphJPGAction;
import view.action.file.imagesave.SaveGraphPNGAction;
import view.action.newactions.NewAction;
import view.action.windows.CloseTabAction;
import view.action.windows.CloseWindowAction;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;

public class FileMenu extends JMenu{
	
	public FileMenu(JFLAPEnvironment e) {
		super("File");

		//New and Open options
		add(createNewMenu());
		add(new OpenAction()); 
		add(new RecentlyOpenendMenu());
		addSeparator();

		//Close and Quite options
		add(new CloseTabButton(new CloseTabAction(e, false)));
		add(new CloseWindowAction(e));
		addSeparator();

		//Save options
		add(new SaveButton(new SaveAction(e)));
		add(new SaveAsButton(new SaveAsAction(e)));
		addSeparator();

		add(constructImageSaveMenu());
	

		addSeparator();
		add(new ExitAction());

		//		new JMenuItem(new PrintAction()),

	}

	private JMenuItem createNewMenu() {
		JMenu newMenu = new JMenu("New...");
		for (NewAction act: NewAction.getAllNewActions())
			newMenu.add(act);
		return newMenu;
	}


	private JMenuItem constructImageSaveMenu() {
		JMenu saveImageMenu = new JMenu("Save Image As...");
		saveImageMenu.add(new SaveGraphJPGAction());
		saveImageMenu.add(new SaveGraphPNGAction());
		saveImageMenu.add(new SaveGraphGIFAction());
		saveImageMenu.add(new SaveGraphBMPAction());
		return saveImageMenu;
	}
	
	
	private class CloseTabButton extends JMenuItem implements PropertyChangeListener{
		private CloseTabAction myAction;
		
		public CloseTabButton(CloseTabAction a){
			super(a);
			myAction = a;
			a.addPropertyChangeListener(this);
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getSource().equals(myAction) && evt.getPropertyName().equals(CloseTabAction.SET_ENABLED))
				setEnabled((Boolean) evt.getNewValue());
		}
		
	}
	
	private class SaveButton extends JMenuItem implements PropertyChangeListener{
		private SaveAction myAction;
		
		public SaveButton(SaveAction a){
			super(a);
			myAction = a;
			a.addPropertyChangeListener(this);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getSource().equals(myAction) && evt.getPropertyName().equals(SaveAction.SAVE))
				setEnabled((Boolean) evt.getNewValue());
		}
	}
	
	private class SaveAsButton extends JMenuItem implements PropertyChangeListener{
		private SaveAsAction myAction;
		
		public SaveAsButton(SaveAsAction a){
			super(a);
			myAction = a;
			a.addPropertyChangeListener(this);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getSource().equals(myAction) && evt.getPropertyName().equals(SaveAsAction.SAVE_AS))
				setEnabled((Boolean) evt.getNewValue());
		}
	}

}
