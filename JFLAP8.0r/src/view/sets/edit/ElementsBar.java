package view.sets.edit;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import model.sets.elements.Element;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import view.EditingPanel;
import view.formaldef.componentpanel.SetComponentBar;
import debug.JFLAPDebug;

public class ElementsBar extends EditingPanel implements JFLAPConstants{
	
	private Bar myElementsBar;
	
	public ElementsBar(UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		
		myElementsBar = new Bar(DEFAULT_SWING_BG);
		myElementsBar.setTo(new Element(""));
		createElementsBar();
	}
	
	private void createElementsBar() {
		this.add(new JLabel("{"));
		this.add(myElementsBar);
		this.add(new JLabel("}"));
	}
	
	
	
	private class Bar extends SetComponentBar<Element> {

		public Bar(Color highlight) {
			super(highlight);
		}

		@Override
		public void doClickResponse(Element item, MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				JFLAPDebug.print("right click");
				myElementsBar.add(new JButton("!"));
			}
			
		}
		
	}
	
}
