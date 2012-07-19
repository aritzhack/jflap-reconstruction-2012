package view.sets;

/**
 * @author Peggy Li
 */

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import util.view.magnify.Magnifiable;

import model.sets.Loader;
import model.sets.operations.SetOperation;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class SetOperationsPanel extends JPanel implements Magnifiable {

	public SetOperationsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		initOperations();
	}

	private void initOperations() {
		Class[] opClasses = Loader.getLoadedClasses(System
				.getProperty("user.dir") + "/bin/model/sets/operations");
		for (Class c : opClasses) {
			if (!Modifier.isAbstract(c.getModifiers())
					&& SetOperation.class.isAssignableFrom(c)) {
				add(createNewButton(c));
			}
		}
	}

	private JComponent createNewButton(Class c) {
		try {
			SetOperation op = (SetOperation) c.getDeclaredConstructor().newInstance();
			System.out.println(op.getName());
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

	@Override
	public void setMagnification(double mag) {
		for (Component c : this.getComponents()) {
			if (c instanceof Magnifiable)
				((Magnifiable) c).setMagnification(mag);
		}
		
		
	}

}
