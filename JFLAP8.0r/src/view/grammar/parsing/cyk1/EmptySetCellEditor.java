package view.grammar.parsing.cyk1;

import java.awt.Component;
import java.util.HashSet;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;
import util.view.SelectingEditor;

public class EmptySetCellEditor extends SelectingEditor {

	public EmptySetCellEditor() {
		super();
	}

	public EmptySetCellEditor(JTextField textField) {
		super(textField);
	}

	@Override
	public Runnable createRunnable(JTextComponent jtc) {
		return new EmptySetRunnable(jtc);
	}
	
	private class EmptySetRunnable implements Runnable {

		private JTextComponent myJTC;

		public EmptySetRunnable(JTextComponent jtc) {
			myJTC = jtc;
			String s = jtc.getText();
			s = s.replaceAll("\\[", "");
			s = s.replaceAll("\\]", "");
			jtc.setText(s);
		}

		@Override
		public void run() {
			int caret = myJTC.getText().length();
			myJTC.selectAll();
			myJTC.setCaretPosition(caret);
		}

	}
}
