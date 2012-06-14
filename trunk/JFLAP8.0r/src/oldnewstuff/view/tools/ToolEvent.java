package oldnewstuff.view.tools;

import java.awt.event.ActionEvent;

import oldview.tools.Tool;


public class ToolEvent {

	private Tool myOld, myNew;

	public ToolEvent(Tool oldTool, Tool newTool) {
		myOld= oldTool;
		myNew = newTool;
	}

	public Tool getNewTool() {
		return myNew;
	}

	public Tool getOldTool() {
		return myOld;
	}

}
