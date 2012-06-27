package view.grammar;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import universe.preferences.JFLAPPreferences;
import util.view.SelectingEditor;

public class LambdaRemovingEditor extends SelectingEditor {

	public LambdaRemovingEditor() {
		super();
	}

	public LambdaRemovingEditor(JTextField textField) {
		super(textField);
	}

	@Override
	public Runnable createRunnable(JTextComponent jtc) {
		return new LambdaRemoveRunnable(jtc);
	}
	
	
	private class LambdaRemoveRunnable implements Runnable{

		private JTextComponent myJTC;

		public LambdaRemoveRunnable(JTextComponent jtc){
			myJTC = jtc;
		}

		@Override
		public void run() {
			myJTC.selectAll();
			String s = myJTC.getText();
			
			if (s.equals(JFLAPPreferences.getEmptyStringSymbol())){
				myJTC.replaceSelection("");
			}
		}

	}
	
}
