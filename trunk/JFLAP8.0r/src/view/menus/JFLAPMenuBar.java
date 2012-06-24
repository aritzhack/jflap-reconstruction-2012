package view.menus;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


import view.action.file.OpenAction;
import view.action.file.ExitAction;
import view.action.file.SaveAction;
import view.action.file.SaveAsAction;
import view.action.file.imagesave.SaveGraphBMPAction;
import view.action.file.imagesave.SaveGraphGIFAction;
import view.action.file.imagesave.SaveGraphJPGAction;
import view.action.file.imagesave.SaveGraphPNGAction;
import view.action.newactions.NewAction;
import view.action.windows.CloseButton;
import view.action.windows.CloseTabAction;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.undoing.redo.MenuRedoAction;
import view.undoing.undo.MenuUndoAction;

public class JFLAPMenuBar extends JMenuBar implements TabChangeListener {

	private CloseButton myCloseButton;

	public JFLAPMenuBar(JFLAPEnvironment e){
		add(new FileMenu(e));
		addMenu(createEditMenu(e));
//		addMenu("Transform", getInputMenuComponents(e));
//		addMenu("Test", getTestMenuComponents(e));
//		addMenu("Convert", getConvertMenuComponents(e));
		this.add(new HelpMenu());
		this.add(Box.createGlue());
		this.add(myCloseButton = new CloseButton(e));
	}
	
	private JMenu createEditMenu(JFLAPEnvironment e) {
		return new EditMenu(e);
	}

	private void addMenu(JMenu menu) {
		if (menu == null) return;
		add(menu);
		
	}

	public Collection<JMenuItem> getConvertMenuComponents(JFLAPEnvironment e) {
		return null;
	}

	public Collection<JMenuItem> getTestMenuComponents(JFLAPEnvironment e) {
		return null;
	}

	public Collection<JMenuItem> getInputMenuComponents(JFLAPEnvironment e) {
		return null;
	}

	public Collection<JMenuItem> getModifyMenuComponents(JFLAPEnvironment e) {
		return combineActionLists(new ArrayList<JMenuItem>(),
				new JMenuItem(new MenuUndoAction(e)),
				new JMenuItem(new MenuRedoAction(e)));
	}

	protected List<JMenuItem> combineActionLists(List<JMenuItem> list,JMenuItem ... items ) {
		list.addAll(Arrays.asList(items));
		return list;
	}

	protected JMenuItem constructImageSaveMenu() {
		JMenu saveImageMenu = new JMenu("Save Image As...");
		saveImageMenu.add(new SaveGraphJPGAction());
		saveImageMenu.add(new SaveGraphPNGAction());
		saveImageMenu.add(new SaveGraphGIFAction());
		saveImageMenu.add(new SaveGraphBMPAction());
		return saveImageMenu;
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		myCloseButton.updateEnabled();

	}

}
