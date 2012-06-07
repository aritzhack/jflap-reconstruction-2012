package universe.preferences;

import java.awt.event.InputEvent;
import java.io.File;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;
import model.regex.EmptySub;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;

/**
 * A class designed to hold all of the user preferences associated
 * with JFLAP. This class imports/exports from/to an xml file 
 * specifically named:
 * 
 *		"preferences.xml"
 *
 * The file can be adjusted through the Preferences menu in the 
 * JFLAP Main Menu or directly via text editors (in the latter case
 * see documentation on file format.)
 * 
 * @author Julian
 *
 */
public class JFLAPPreferences {

	private static final File PREFERENCE_FILE = new File("preferences.xml");
	
	public static void importFromFile() {
		// TODO Auto-generated method stub
		
	}
	
    public static String LAMBDA = "\u03BB";     // Jinghui Lim added stuff
    public static String EPSILON = "\u03B5";    // see MultipleSimulateAction
    private static char BLANK = '\u25A1';
	
	/** The main mask for keystrokes in a menu. */
	private static int MAIN_MENU_MASK;

	public static String getSymbolStringDelimiter() {
		return " ";
	}

	public static String getEmptyStringSymbol() {
		return LAMBDA;
	}
	
	/**
	 * Returns the main mask for menu items. The main mask is the mask of keys
	 * that are held down to typically invoke a menu item. This varies from
	 * platform to platform. On Windows it's the control key, and on everything
	 * else (presumably either Mac OS or some other Unix based system) it's meta
	 * (on the MacOS this is interpreted as the command key).
	 * 
	 * @return the main modifier for menu items
	 */
	public static int getMainMenuMask() {
		return MAIN_MENU_MASK;
	}

	/**
	 * Initializes the value for the main menu mask.
	 */
	private static void initMainMenuMask() {
		String s = System.getProperty("os.name");
		if ((s.lastIndexOf("Windows") != -1)
				|| (s.lastIndexOf("windows") != -1))
			MAIN_MENU_MASK = InputEvent.CTRL_MASK;
		else
			MAIN_MENU_MASK = InputEvent.META_MASK;
	}

	public static Symbol getTMBlankSymbol() {
		return new Symbol(BLANK + "");
	}

	public static OpenGroup getCurrentRegExOpenGroup() {
		return new OpenGroup("(");
	}

	public static CloseGroup getCurrentRegExCloseGroup() {
		return new CloseGroup(")");
	}

	public static String getDefaultStateNameBase() {
		return "q";
	}

	public static UnionOperator getUnionOperator() {
		return new UnionOperator("+");
	}

	public static EmptySub getSubForEmptyString() {
		return new EmptySub("!");
	}

	public static Terminal getEndOfStringMarker() {
		return new Terminal("$");
	}


}