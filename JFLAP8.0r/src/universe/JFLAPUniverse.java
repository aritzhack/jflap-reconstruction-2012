package universe;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JComponent;


import controller.JFLAPController;

import universe.menu.MainMenu;
import universe.preferences.JFLAPPreferences;




/**
 * The universe which holds all windows/controllers in the currently
 * running JFLAP program. This is a singleton class which can be called 
 * statically by anything in the program. Automatically handles
 * swing-based focus changes and window activations.
 * 
 * @author Julian Genkins
 *
 */
public class JFLAPUniverse{

	/**
	 * The collection of all controllers. 
	 */
	public static List<JFLAPController> CONTROLLER_REGISTRY;
	
	/**
	 * The main menu object. Singleton, there needs only be one.
	 */
	private static MainMenu MAIN_MENU;
	
	/**
	 * The currently active controller, i.e. the frame that holds
	 * the swing focus.
	 */
	private static JFLAPController myActiveController;
	
	
	/**
	 * Intializing static block - 
	 */
	static {
		MAIN_MENU = new MainMenu();
		CONTROLLER_REGISTRY = new ArrayList<JFLAPController>();
		JFLAPPreferences.importFromFile();
	}
	
	public static Collection<JFLAPController> getRegistry(){
		return CONTROLLER_REGISTRY;
	}
	
	public static JFLAPController createAndRegisterController(JComponent comp){
		try {
			JFLAPController controller = new JFLAPController(CONTROLLER_REGISTRY.size(), comp);
			registerController(controller);
			return controller;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	public static void registerController(JFLAPController controller) {
		CONTROLLER_REGISTRY.add(controller);
		setActiveContoller(controller.getID());
		setUpWindowListener(controller);
	}

	private static void setUpWindowListener(JFLAPController controller) {
		//TODO: Do I need both of these?
		controller.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == WindowEvent.WINDOW_ACTIVATED) {
		            setActiveContoller(((JFLAPController)e.getWindow()));
		        }
			}
		});
		controller.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				JFLAPUniverse.setActiveContoller((JFLAPController) e.getComponent());
			}
		});
	}
	

	protected static void clearActiveController() {
		myActiveController = null;
	}

	public static boolean unregisterController(JFLAPController controller) {
		if (controller.equals(getActiveController())) 
			clearActiveController();
		boolean b = CONTROLLER_REGISTRY.remove(controller);
		if (b && !isRegistryEmpty()) 
			myActiveController = CONTROLLER_REGISTRY.get(0);
		return b;
	}
	
	public static JFLAPController getControllerByID(int ID){
		for (JFLAPController cont:  CONTROLLER_REGISTRY){
			if (cont.getID() == ID) {
				return cont;
			}
		}
		return null;
	}
	
	private static void setActiveContoller(int id) {
		setActiveContoller(getControllerByID(id));
	}

	private static void setActiveContoller(JFLAPController cont) {
		if (cont == null) return;
		myActiveController = cont;
		cont.invalidate();
		cont.repaint();
	}

	public static void hideMainMenu() {
		MAIN_MENU.setVisible(false);
	}

	public static void showMainMenu() {
		MAIN_MENU.setVisible(true);
	}
	
    public static void closeMainMenu() {
    	MAIN_MENU.dispose();
        MAIN_MENU = null;
    }

	public static JFLAPController getActiveController() {
		return myActiveController;
	}

	public static boolean isRegistryEmpty() {
		return CONTROLLER_REGISTRY.isEmpty();
	}

	public static void exit(boolean should_save) {
		
	}
	
	

}
