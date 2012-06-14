package test;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;

import preferences.JFLAPPreferences;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.regex.RegularExpression;
import errors.ThrowableCatcher;
import file.xml.XMLCodec;

public class UITesting {

	public static void main(String[] args) {
		try {
			System.setProperty("sun.awt.exception.handler",
					ThrowableCatcher.class.getName());
		} catch (SecurityException e) {
			System.err.println("Warning: could not set the "
					+ "AWT exception handler.");
		}
		JFrame frame = new JFrame();
		String toSave = System.getProperties().getProperty("user.dir") +"/filetest";
		File f = new File(toSave + "/grammar.jff");
		
		Grammar regex = (Grammar) new XMLCodec().decode(f);
//		frame.add(new GrammarView((Grammar) regex, new UndoKeeper(), true));
		frame.pack();
		frame.setVisible(true);
	}
}
