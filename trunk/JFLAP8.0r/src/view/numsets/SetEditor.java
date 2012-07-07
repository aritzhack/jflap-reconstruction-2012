package view.numsets;

import model.undo.UndoKeeper;
import view.EditingPanel;
import view.grammar.SizeSlider;

@SuppressWarnings("serial")
public class SetEditor extends EditingPanel {

	private SetDefinitionPanel myDefinitionPanel;
	
	public SetEditor () {
		this(new UndoKeeper(), true);
		
	}

	public SetEditor(UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		
		myDefinitionPanel = new SetDefinitionPanel(keeper);
		
		SizeSlider slider = new SizeSlider(myDefinitionPanel);
		slider.distributeMagnification();
		
		add(myDefinitionPanel);
			
	}


	public String getName () {
		return "Set Editor";
	}



}
