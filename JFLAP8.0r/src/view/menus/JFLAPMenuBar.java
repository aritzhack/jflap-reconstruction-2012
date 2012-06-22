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

import oldnewstuff.action.save.SaveAction;

import view.action.windows.CloseButton;
import view.action.windows.CloseTabAction;
import view.environment.JFLAPEnvironment;

public class JFLAPMenuBar extends JMenuBar {

	private CloseButton myCloseButton;

	public JFLAPMenuBar(JFLAPEnvironment e){
		addMenu("File", getFileMenuComponents(e));
		addMenu("Edit", getModifyMenuComponents(e));
		addMenu("Transform", getInputMenuComponents(e));
		addMenu("Test", getTestMenuComponents(e));
		addMenu("Convert", getConvertMenuComponents(e));
		this.add(new HelpMenu());
		this.add(Box.createGlue());
		this.add(myCloseButton = new CloseButton(e));
	}
	
	private void addMenu(String title, Collection<JMenuItem> menuItems) {
		if (menuItems == null) return;
		
		JMenu menu = new JMenu(title);
		for (JMenuItem item: menuItems){
			menu.add(item);
		}
		this.add(menu);
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
		return null;
	}

	public List<JMenuItem> getFileMenuComponents(JFLAPEnvironment e){
		return combineActionLists(new ArrayList<JMenuItem>(), 
				new JMenuItem(new NewAction()), 
				new JMenuItem(new OpenAction()), 
				new JMenuItem(new SaveAction(e)),
				new JMenuItem(new SaveAsAction(e)),
				constructImageSaveMenu(),
				new JMenuItem(new CloseTabAction(e, false)),
				new JMenuItem(new CloseWindowAction(e)),
//				new JMenuItem(new PrintAction()),
				new JMenuItem(new QuitAction()));
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

	public void update() {
		myCloseButton.updateEnabled();
	}

}
