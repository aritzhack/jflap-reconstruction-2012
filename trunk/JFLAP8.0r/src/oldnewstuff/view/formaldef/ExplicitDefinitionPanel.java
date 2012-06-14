package oldnewstuff.view.formaldef;

import gui.undo.UndoKeeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.view.EditingView;
import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.formaldef.componentpanel.ComponentPanelFactory;
import oldnewstuff.view.formaldef.componentpanel.DefinitionComponentPanel;
import preferences.JFLAPPreferences;




import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;

public class ExplicitDefinitionPanel<T extends FormalDefinition> extends EditingView<T> implements JFLAPGUIResources{

	
	public ExplicitDefinitionPanel(T fd, UndoKeeper keeper, boolean editable) {
		super(fd, keeper, editable);
	}

	
	private void updateBorder() {
		TitledBorder b = 
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, JFLAPPreferences.getDefinitionFontSize()/5), 
													this.getModel().toNtupleString());
		this.setBorder(b);
		b.setTitleFont(JFLAPPreferences.getFormalDefinitionFont());
	}

	@Override
	public void update(T fd) {
		this.removeAll();
		this.updateBorder();
		Collection<Alphabet> comps = fd.getAlphabets();
		for (FormalDefinitionComponent comp: comps){
			DefinitionComponentPanel panel = 
					ComponentPanelFactory.createForComponent(comp, getKeeper(), this.isEditable());
			if (panel == null) continue;
			panel.setEditable(this.isEditable());
			panel.setAlignmentX(LEFT_ALIGNMENT);
			panel.setAlignmentY(CENTER_ALIGNMENT);
			panel.setDefinitionParent(this);
			this.add(panel);
		}
		this.setToolTipText(this.getModel().getDescription());
		this.repaint();
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		update(this.getModel());
	}
	@Override
	public void initializeComponents(T model, Object ... args) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	};
	@Override
	public void cancelAllEditing() {
		
	}


	@Override
	public String getName() {
		return getModel().getDescriptionName();
	}



}
