package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

public interface JFLAPGUIResources {

	
	public static final Border DEF_PANEL_BORDER = BorderFactory.createLineBorder(Color.BLACK, 3);
	public final static Font DEF_PANEL_FONT = new Font("Dialog", 1, 15);
	public static final Color DEFAULT_SWING_BG = UIManager.getColor ( "Panel.background" ),
								  SPECIAL_SYMBOL = new Color(235, 235, 150),
								  BAR_SELECTED = new Color(140, 175, 255);
	public static final String TAB_CHANGED = "tab_change_event";
}
