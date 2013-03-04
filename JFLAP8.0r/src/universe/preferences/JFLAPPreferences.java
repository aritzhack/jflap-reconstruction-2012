package universe.preferences;

import java.awt.Font;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenu;

import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.lsystem.commands.BeginPolygonCommand;
import model.lsystem.commands.DecrementColorCommand;
import model.lsystem.commands.DecrementPColorCommand;
import model.lsystem.commands.DecrementWidthCommand;
import model.lsystem.commands.EndPolygonCommand;
import model.lsystem.commands.ForwardCommand;
import model.lsystem.commands.DrawCommand;
import model.lsystem.commands.IncrementColorCommand;
import model.lsystem.commands.IncrementPColorCommand;
import model.lsystem.commands.IncrementWidthCommand;
import model.lsystem.commands.LeftYawCommand;
import model.lsystem.commands.PitchDownCommand;
import model.lsystem.commands.PitchUpCommand;
import model.lsystem.commands.PopCommand;
import model.lsystem.commands.PushCommand;
import model.lsystem.commands.RightYawCommand;
import model.lsystem.commands.LeftRollCommand;
import model.lsystem.commands.RightRollCommand;
import model.lsystem.commands.YawCommand;
import model.lsystem.commands.BeginPolygonCommand;
import model.lsystem.commands.DecrementColorCommand;
import model.lsystem.commands.DecrementPColorCommand;
import model.lsystem.commands.DecrementWidthCommand;
import model.lsystem.commands.EndPolygonCommand;
import model.lsystem.commands.ForwardCommand;
import model.lsystem.commands.DrawCommand;
import model.lsystem.commands.IncrementColorCommand;
import model.lsystem.commands.IncrementPColorCommand;
import model.lsystem.commands.IncrementWidthCommand;
import model.lsystem.commands.LeftYawCommand;
import model.lsystem.commands.PitchDownCommand;
import model.lsystem.commands.PitchUpCommand;
import model.lsystem.commands.PopCommand;
import model.lsystem.commands.PushCommand;
import model.lsystem.commands.RightYawCommand;
import model.lsystem.commands.LeftRollCommand;
import model.lsystem.commands.RightRollCommand;
import model.lsystem.commands.YawCommand;
import model.regex.EmptySub;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;
import model.symbols.PermanentSymbol;
import model.symbols.Symbol;

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
	
	private static LinkedList<File> RECENTLY_OPENED;
	private static List<PreferenceChangeListener> LISTENERS;
	
	static{
		RECENTLY_OPENED = new LinkedList<File>();
		LISTENERS = new ArrayList<PreferenceChangeListener>();
		importFromFile();
		initMainMenuMask();
	}
	
	
	public static void importFromFile() {
		// TODO Auto-generated method stub
		
	}
	
    public static String LAMBDA = "\u03BB";     // Jinghui Lim added stuff
    public static String EPSILON = "\u03B5";    // see MultipleSimulateAction
    private static String BLANK = "\u25A1";
    public static String EMPTY_SET = "\u2205";
	
	/** The main mask for keystrokes in a menu. */
	private static int MAIN_MENU_MASK;

	public static String getSymbolStringDelimiter() {
		return " ";
	}

	public static String getSymbolizeDelimiter() {
		return " ";
	}
	
	public static String getEmptyStringSymbol() {
		return LAMBDA;
	}
	
	public static final Terminal SLR_MARKER = new Terminal("_");

	public static final String RECENT_CHANGED = "recent_changed";

	public static final String MODE_CHANGED = "mode";
	
	public static JFLAPMode DEFAULT_MODE = JFLAPMode.DEFAULT;
	
	public static boolean CYK_DIAGONAL = false;

	public static int getDefaultTMBufferSize(){
		return 5;
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
		return new PermanentSymbol(BLANK);
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
	
	public static BeginPolygonCommand getBeginPolygonCommand(){
		return new BeginPolygonCommand("{");
	}
	
	public static EndPolygonCommand getEndPolygonCommand(){
		return new EndPolygonCommand("}");
	}
	
	public static DecrementColorCommand getDColorCommand(){
		return new DecrementColorCommand("@");
	}

	public static IncrementColorCommand getIColorCommand(){
		return new IncrementColorCommand("#");
	}
	
	public static DecrementPColorCommand getDPolyColorCommand(){
		return new DecrementPColorCommand("@@");
	}
	
	public static IncrementPColorCommand getIPolyColorCommand(){
		return new IncrementPColorCommand("##");
	}
	
	public static DecrementWidthCommand getDWidthCommand(){
		return new DecrementWidthCommand("~");
	}
	
	public static IncrementWidthCommand getIWidthCommand(){
		return new IncrementWidthCommand("!");
	}
	
	public static ForwardCommand getForwardCommand(){
		return new ForwardCommand("f");
	}
	
	public static DrawCommand getDrawCommand(){
		return new DrawCommand("g");
	}
	
	public static LeftYawCommand getLeftYawCommand(){
		return new LeftYawCommand("-");
	}
	
	public static RightYawCommand getRightYawCommand(){
		return new RightYawCommand("+");
	}
	
	public static PitchDownCommand getPitchDownCommand(){
		return new PitchDownCommand("&");
	}
	
	public static PitchUpCommand getPitchUpCommand(){
		return new PitchUpCommand("^");
	}
	
	public static PushCommand getPushCommand(){
		return new PushCommand("[");
	}
	
	public static PopCommand getPopCommand(){
		return new PopCommand("]");
	}
	
	public static RightRollCommand getRightRollCommand(){
		return new RightRollCommand("/");
	}
	
	public static LeftRollCommand getLeftRollCommand(){
		return new LeftRollCommand("*");
	}
	
	public static YawCommand getYawCommand(){
		return new YawCommand("%");
	}
	
	public static EmptySub getSubForEmptyString() {
		return new EmptySub("!");
	}

	public static Terminal getEndOfStringMarker() {
		return new Terminal("$");
	}

	public static int getDefaultTextSize() {
		return 50;
	}

	public static boolean useDefinitionAbbreviations() {
		return false;
	}

	public static Font getFormalDefinitionFont() {
		return new Font(getDefinitionFontName(), getDefinitionFontStyle(), getDefaultTextSize());
	}

	private static String getDefinitionFontName() {
		return "Dialog";
	}

	private static int getDefinitionFontStyle() {
		return 1;
	}

	public static void setDefaultMode(JFLAPMode mode){
		DEFAULT_MODE = mode;
		distributeChange(MODE_CHANGED, mode);
	}
	
	public static void setCYKDiagonal(){
		CYK_DIAGONAL = true;
	}
	
	public static void setCYKHorizontal(){
		CYK_DIAGONAL = false;
	}
	
	
	public static JFLAPMode getDefaultMode() {
		return DEFAULT_MODE;
	}
	
	public static Variable getDefaultStartVariable() {
		return new Variable("S");
	}

	public static GroupingPair getDefaultGrouping() {
		return new GroupingPair('<', '>');
	}

	public static JMenu getPreferenceMenu() {
		return new JMenu("Preferences");
	}

	public static int getNumSwitchOptions() {
		return 5;
	}
	
	public static File[] getRecentlyOpenedFiles(){
		return RECENTLY_OPENED.toArray(new File[0]);
	}

	public static void addRecentlyOpenend(File f) {
		RECENTLY_OPENED.addFirst(f);
		distributeChange(RECENT_CHANGED, RECENTLY_OPENED);
	}

	public static void distributeChange(String s, Object o){
		for (PreferenceChangeListener l: LISTENERS)
			l.preferenceChanged(s, o);
	}
	
	public static void addChangeListener(PreferenceChangeListener l){
		LISTENERS.add(l);
	}

	public static boolean isCYKtableDiagonal(){
		return CYK_DIAGONAL;
	}
	
}
