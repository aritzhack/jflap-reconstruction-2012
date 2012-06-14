package oldnewstuff.view;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class JFLAPView<T> extends JPanel implements Updateable<T>{
	
	private T myModel;

	public JFLAPView(T model, Object ... args) {
		initializeComponents(model, args);
		setModel(model);
	}

	public void setModel(T model) {
		myModel = model;
		update(myModel);
	}
	
	public T getModel(){
		return myModel;
	}

	public abstract String getName();
	
	public abstract void initializeComponents(T model, Object ... args);

	public void updateAndRepaint(){
		this.update(getModel());
		this.repaint();
	}
}
