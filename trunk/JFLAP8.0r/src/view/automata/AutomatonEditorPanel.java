package view.automata;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;
import util.arrows.GeometryHelper;
import util.view.magnify.MagnifiablePanel;
import view.EditingPanel;
import view.automata.tools.Tool;
import view.automata.tools.ToolBar;
import view.automata.tools.ToolListener;
import view.graph.GraphDrawer;

public class AutomatonEditorPanel<T extends Automaton<S>, S extends Transition<S>> extends EditingPanel {

	private Tool myTool;
	private T myAutomaton;
	private TransitionGraph<S> myGraph;
	private AutomatonDrawer<S> myDrawer;
	
	public AutomatonEditorPanel(T m, UndoKeeper keeper, boolean editable){
		super(keeper,editable);
		myAutomaton = m;
		myGraph = new TransitionGraph<S>(m);
		StateDrawer vDraw = new StateDrawer();
		myDrawer = new AutomatonDrawer<S>(vDraw);
	}
	
	
	public void stopAllEditing(){
		
	}
	
	public void editNote(Note n){
		
	}
	

	public void setTool(Tool t){
		this.removeMouseListener(myTool);
		myTool = t;
		this.addMouseListener(myTool);
	}
	
	public void selectObject(Object o){
		myGraph.setSelected(o, true);
	}
	
	public void clearSelection(){
		myGraph.clearSelection();
	}
	
	public void showMenu(Point2D p){
		Object o = objectAtPoint(p);
		
	}
		
	public Object objectAtPoint(Point2D p){
		Object o = stateAtPoint(p);
		if (o==null) o = transitionAtPoint(p);
		if (o==null) o = arrowAtPoint(p);
		if (o==null) o = noteAtPoint(p);
		return o;
	}
	
	private Note noteAtPoint(Point2D p) {
		// TODO Auto-generated method stub
		return null;
	}


	private State[] arrowAtPoint(Point2D p) {
		return null;
	}


	private S transitionAtPoint(Point2D p) {
		// TODO Auto-generated method stub
		return null;
	}


	private State stateAtPoint(Point2D p) {
		// TODO Auto-generated method stub
		return null;
	}


	private LabelBounds getLabelBounds(Point2D center, Point2D pFrom, Point2D pTo, Graphics g) {

		double angle = GeometryHelper.calculateAngle(pFrom,pTo);
		//calculate bounds
		FontMetrics metrics = g.getFontMetrics();
		String label = this.toString();
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX()-w/2);
		int y = (int) (center.getY()-h/2);

		return new LabelBounds(angle, new Rectangle(x, y, w, h));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.white);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		myTool.draw(g);
		myDrawer.draw(myGraph, g);
	}

}
