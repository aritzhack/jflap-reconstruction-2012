package view.automata;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import universe.preferences.JFLAPPreferences;

public class Note{

	private AutomatonEditorPanel myPanel;
	private Point myPoint;
	private String myString;

	public Note(AutomatonEditorPanel panel, Point p) {
		this(panel, p, null);
	}
	
	public Note(AutomatonEditorPanel panel, String message){
		this(panel, new Point(0,0), message);
	}

	public Note(AutomatonEditorPanel panel, Point p, String message) {
		myPanel = panel;
		myPoint = p;
		myString = message;
	}
	
	public void setText(String text){
		myString = text;
		myPanel.repaint();
	}
	
	public void setLocation(Point p){
		myPoint = p;
		myPanel.repaint();
	}
	
	public void draw(Graphics2D g2d){
		FontMetrics metrics = g2d.getFontMetrics();
		int w = metrics.stringWidth(myString);
		int h = metrics.getMaxAscent();
		int x = myPoint.x - w / 2;
		int y = myPoint.y + h / 2;
		
		Color current = g2d.getColor();
		g2d.setColor(JFLAPPreferences.getStateColor());
		g2d.fillRect(x-1, y-h, w+2, h+2);
		g2d.setColor(current);
		g2d.drawString(myString, x, y);
		
	}
}
