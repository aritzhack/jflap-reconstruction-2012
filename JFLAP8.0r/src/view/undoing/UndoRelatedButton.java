package view.undoing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;
import util.JFLAPConstants;
import util.view.ActionLinkedButton;

public abstract class UndoRelatedButton extends ActionLinkedButton implements UndoKeeperListener {

	private ImageIcon myIcon;
	private boolean amUsingIcon;


	public UndoRelatedButton(UndoRelatedAction a, boolean useIcon) {
		super(a);
		amUsingIcon = useIcon;
		a.getKeeper().addUndoListener(this);
	}


	public abstract String getIconFilename();
	
	@Override
	public String getText() {
		return amUsingIcon ? "" : super.getText();
	}

	@Override
	public Icon getIcon() {
		if (!amUsingIcon)
			return null;
		if (myIcon == null){
			String url = JFLAPConstants.RESOURCE_ROOT + "/ICON/" + getIconFilename();
			myIcon = new ImageIcon(url);
		}
		return myIcon;
	}

	@Override
	public void keeperStateChanged() {
		updateEnabled();
	}
	
}
