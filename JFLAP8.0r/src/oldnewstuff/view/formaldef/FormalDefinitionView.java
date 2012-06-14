package oldnewstuff.view.formaldef;

import gui.undo.UndoKeeper;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.view.EditingView;
import oldnewstuff.view.JFLAPView;
import oldnewstuff.view.tools.JFLAPToolBar;





import model.formaldef.FormalDefinition;

public abstract class FormalDefinitionView<T extends FormalDefinition> extends EditingView<T> implements ChangeListener{


	private JFLAPView<T> myCenterComponent;
	private ExplicitDefinitionPanel<T> myDefinitionComponent;
	private JToolBar myToolbar;

	public FormalDefinitionView(T fd, UndoKeeper keeper, boolean editable) {
		super(fd, keeper, editable);
	}

	@Override
	public void setModel(T model) {
		if (this.getModel() != null)
			this.getModel().removeListener(this);
		super.setModel(model);
		this.getModel().addListener(this);
	};
	
	@Override
	public void stateChanged(ChangeEvent e) {
		this.registerChange();
	}
	
	@Override
	public void initializeComponents(T model, Object ... args) {
		super.initializeComponents(model, args);
		this.setLayout(new BorderLayout());
		myToolbar = createToolbar(model);
		myCenterComponent = createCenterComponent(model);
		myDefinitionComponent = new ExplicitDefinitionPanel<T>(model, getKeeper(), isEditable());
		this.add(myToolbar, BorderLayout.NORTH);
		this.add(myCenterComponent, BorderLayout.CENTER);
		this.add(myDefinitionComponent, BorderLayout.SOUTH);
	};
	
	public JToolBar createToolbar(T model){
		return new JFLAPToolBar(getKeeper());
	}

	public abstract JFLAPView<T> createCenterComponent(T model);

	@Override
	public void update(T model) {
		myCenterComponent.update(model);
		myDefinitionComponent.update(model);
	}
	
	
}
