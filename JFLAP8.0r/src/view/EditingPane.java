package view;

import java.io.File;

import javax.swing.JPanel;

public class EditingPane<T> extends JPanel implements Saveable {

	private T myOldModel;
	private T myCurrentModel;

	public EditingPane(T model){
		myOldModel = model;
		setModel(model);
	}
	
	public void setModel(T model) {
		
	}
	
	public T getModel(){
		return myCurrentModel;
	}

	@Override
	public boolean save(File f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldBeSaved() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
