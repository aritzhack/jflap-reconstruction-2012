package view.formaldef;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import model.formaldef.FormalDefinition;
import model.undo.UndoKeeper;

public abstract class FormalDefinitionView<T extends FormalDefinition> extends JPanel{

	
	private T myDefinition;
	private FormalDefinitionPanel myDefinitionPanel;
	private JComponent myCentralPanel;
	private UndoKeeper myKeeper;

	public FormalDefinitionView(T definition, UndoKeeper keeper, boolean editable){
		myDefinition = definition;
		myKeeper = keeper;
		this.setLayout(new BorderLayout());
		myDefinitionPanel = new FormalDefinitionPanel(definition, keeper, editable);
		myCentralPanel = createCentralPanel(definition, keeper);
		this.add(myDefinitionPanel, BorderLayout.SOUTH);
		this.add(myCentralPanel, BorderLayout.CENTER);
	}

	public abstract JComponent createCentralPanel(T definition, UndoKeeper keeper);
	
	
}
