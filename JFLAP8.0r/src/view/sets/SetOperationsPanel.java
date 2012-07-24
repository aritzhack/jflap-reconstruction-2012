package view.sets;

/**
 * @author Peggy Li
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import model.sets.operations.CartesianProduct;
import model.sets.operations.Difference;
import model.sets.operations.Intersection;
import model.sets.operations.Powerset;
import model.sets.operations.SetOperation;
import model.sets.operations.Union;
import util.view.magnify.MagnifiablePanel;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class SetOperationsPanel extends MagnifiablePanel {

	public SetOperationsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		initOperations();
	}
	
	private void initOperations() {
		for (Class c : myOperationsClasses) {
			if (!Modifier.isAbstract(c.getModifiers()) && 
				SetOperation.class.isAssignableFrom(c)) 
				
				add(createNewButton(c));
		}
	}
	
	/**
	 * 
	 * @param class of the set operation for which a button is being made
	 * @return button associated with a particular set operation
	 */
	private JComponent createNewButton(Class c) {
		try {
			SetOperation op = (SetOperation) c.getDeclaredConstructor().newInstance();
			return new SetOperationButton(op);
			
		// none of these exceptions should be thrown, method should not return null
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	private static final Class[] myOperationsClasses = {
		Intersection.class,
		Union.class,
		Powerset.class,
		Difference.class,
		CartesianProduct.class
	};
	
	
	
}
