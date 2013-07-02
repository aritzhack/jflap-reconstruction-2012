package view.automata;

import java.awt.Point;

import javax.swing.JTextArea;

public class Note extends JTextArea {

	public static final String BLANK_TEXT = "insert_text";

	private AutomatonEditorPanel myPanel;

	private Point myPoint;

	public Note(AutomatonEditorPanel panel, Point p) {
		this(panel, p, BLANK_TEXT);
	}

	public Note(AutomatonEditorPanel panel, Point p, String message) {
		myPanel = panel;
		setPoint(p);
		setText(message);
		panel.add(this);

		setEnabled(true);
		setEditable(true);
		setCaretColor(null);
		this.setSelectionStart(0);
		this.requestFocus();
	}
	
	public Point getPoint(){
		return myPoint;
	}
	
	public void setPoint(Point p) {
		myPoint = p;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Note){
			return myPoint.equals(((Note) obj).myPoint) && getText().equals(((Note) obj).getText());
		}
		return false;
	}
}
