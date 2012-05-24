package test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class TestHarness {

	private JTextArea myArea;
	
	public TestHarness(){
		myArea = setUpDisplay();
		runTest();
	}
	
	public abstract void runTest();

	public void outPrintln(String s) {
		myArea.setForeground(Color.BLACK);
		myArea.append(s + "\n");
	}

	public void errPrintln(String str) {
		myArea.setForeground(Color.red);
		myArea.append(str +"\n");
		
	}

	private JTextArea setUpDisplay() {
		JFrame frame = new JFrame("JFLAP Test Print!");
		JTextArea area = new JTextArea();
		JScrollPane panel = new JScrollPane(area);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		return area;
	}
}
