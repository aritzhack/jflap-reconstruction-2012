package oldnewstuff.view.util.selection;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import oldnewstuff.view.util.MouseClickAdapter;
import oldnewstuff.view.util.SuperMouseAdapter;



public class SelectionListener<S extends JComponent, T extends ISelector> extends MouseClickAdapter<S> {

	
	private T mySelector;


	public SelectionListener(T selector){
		mySelector = selector;
	}
	
	
	@Override
	public void leftClickResponse(MouseEvent e, S component) {
		mySelector.clearSelection();
		if (component instanceof ISelectable)
			mySelector.select((ISelectable) component);
		if (mySelector instanceof Component){
			((Component) mySelector).repaint();
		}
	}
	

}
