package view.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import universe.preferences.JFLAPPreferences;
import universe.preferences.PreferenceChangeListener;
import view.action.file.OpenAction;
import view.action.file.ExitAction;
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

public class FileMenu extends JMenu{

	public FileMenu(JFLAPEnvironment e) {
		super("File");

		//New and Open options
		add(createNewMenu());
		add(new OpenAction()); 
		add(new RecentlyOpenendMenu());
		addSeparator();

		//Close and Quite options
		add(new CloseTabAction(e, false));
		add(new CloseWindowAction(e));
		addSeparator();

		//Save options
		add(new SaveAction(e));
		add(new SaveAsAction(e));
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

}
