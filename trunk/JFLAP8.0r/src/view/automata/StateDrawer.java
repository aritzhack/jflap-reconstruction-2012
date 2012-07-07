package view.automata;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import model.automata.State;
import util.ColoredStroke;
import view.graph.VertexDrawer;

public class StateDrawer extends VertexDrawer<State> {

	private ColoredStroke myTextStroke;
	
	public StateDrawer(){
		myTextStroke = new ColoredStroke(1, Color.black);
	}
	
	public void setTextColor(Color c){
		myTextStroke.setColor(c);
	}
	@Override
	public void draw(double x, double y, State obj, Graphics g) {
		super.draw(x, y, obj, g);
		g = g.create();
		
		int dx = ((int) g.getFontMetrics().getStringBounds(obj.getName(), g)
				.getWidth()) >> 1;
		int dy = ((int) g.getFontMetrics().getAscent()) >> 1;
		
		myTextStroke.apply((Graphics2D) g);
		
		g.drawString(obj.getName(),(int) x - dx, (int) (y + dy));
	}

	
	
}
