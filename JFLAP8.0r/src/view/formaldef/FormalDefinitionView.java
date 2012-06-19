package view.formaldef;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import view.grammar.Magnifiable;
import view.grammar.TableTextSizeSlider;

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
		myCentralPanel = createCentralPanel(definition, keeper, editable);
		JScrollPane scroller = new JScrollPane(myCentralPanel);
		TableTextSizeSlider slider = new TableTextSizeSlider(myDefinitionPanel);
		if (myCentralPanel instanceof Magnifiable)
			slider.addListener((Magnifiable) myCentralPanel);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(myDefinitionPanel);
		panel.add(slider);
		this.add(scroller, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
	}

	public abstract JComponent createCentralPanel(T definition, UndoKeeper keeper, boolean editable);
	
	
}
