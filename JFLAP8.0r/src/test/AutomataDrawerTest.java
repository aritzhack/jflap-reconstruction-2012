package test;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.JFLAPConstants;
import view.automata.AutomatonDrawer;
import view.automata.StateDrawer;
import view.graph.GraphDrawer;
import view.graph.VertexDrawer;

import file.xml.XMLCodec;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.turing.TuringMachine;
import model.automata.turing.universal.UniversalTuringMachine;
import model.graph.LayoutAlgorithm;
import model.graph.LayoutAlgorithmFactory;
import model.graph.TransitionGraph;
import model.regex.GeneralizedTransitionGraph;

public class AutomataDrawerTest extends TestHarness implements JFLAPConstants{

	@Override
	public void runTest() {
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
		File f = new File(toSave + "/blockTM_unaryAdd.jff");
		Automaton a = new UniversalTuringMachine(false);
		outPrintln("After import:\n" + a.toString());
		JFrame frame =  new JFrame();
		JPanel panel = new DrawPanel(a);
		panel.setOpaque(true);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		panel.repaint();
	}

	@Override
	public String getTestName() {
		return "Automaton Drawer";
	}

	public static void main(String[] args) {
		new AutomataDrawerTest();
	}

	private class DrawPanel<T extends Transition<T>> extends JPanel{

		private TransitionGraph<T> myGraph;

		public DrawPanel(Automaton<T> a2){
			myGraph = new TransitionGraph<T>(a2);
			LayoutAlgorithm a = LayoutAlgorithmFactory.getLayoutAlgorithm(3);
			a.layout(myGraph, new TreeSet());
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			this.setBackground(Color.white);
			StateDrawer vDraw = new StateDrawer();
			GraphDrawer<State> drawer = 
					new AutomatonDrawer(vDraw);

			drawer.draw(myGraph, g);
		}
	}

}
