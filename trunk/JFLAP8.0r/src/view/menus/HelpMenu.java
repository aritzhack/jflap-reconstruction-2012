package view.menus;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;




public class HelpMenu extends JMenu {

	public HelpMenu(){
		super("Help");
		this.add(new NewHelpAction());
		this.add(new ShowRulesAction());
		this.add(new AboutAction());
		
	}
	
}
