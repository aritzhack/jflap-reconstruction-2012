package view.menus;

import java.awt.Component;

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

public class FileMenu extends JMenu implements TabChangeListener{
	private JMenuItem closeTabItem, saveItem, saveAsItem;
	private CloseTabAction closeTab;
	private SaveAction save;
	private SaveAsAction saveAs;
	
	public FileMenu(JFLAPEnvironment e) {
		super("File");
		e.addTabListener(this);

		//New and Open options
		add(createNewMenu());
		add(new OpenAction()); 
		add(new RecentlyOpenendMenu());
		addSeparator();

		//Close and Quite options
		closeTabItem = new JMenuItem(closeTab = new CloseTabAction(e, false));
		add(closeTabItem);
		add(new CloseWindowAction(e));
		addSeparator();

		//Save options
		saveItem = new JMenuItem (save = new SaveAction(e));
		add(saveItem);
		saveAsItem = new JMenuItem(saveAs = new SaveAsAction(e));
		add(saveAsItem);
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

	@Override
	public void tabChanged(TabChangedEvent e) {
		update();
	}
	
	private void update(){
		closeTabItem.setEnabled(closeTab.isEnabled());
		saveItem.setEnabled(save.isEnabled());
		saveAsItem.setEnabled(saveAs.isEnabled());
	}

}
