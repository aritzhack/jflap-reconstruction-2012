package view.grammar.parsing.derivation;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import model.algorithms.testinput.parse.Derivation;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;

import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiableTabbedPane;

public class DerivationView extends MagnifiableTabbedPane{

	public DerivationView(DerivationPanel ... panels){
		for (DerivationPanel p: panels){
			this.add(p);
		}
	}

	/**
	 * Creates a default {@link DerivationView} which will contain a
	 * non-inverted derivation tree and a derivation table.
	 * @param d
	 */
	public DerivationView(Derivation d){
		this(new DerivationTreePanel(d, false), new DerivationTable(d));
	}


	public void setDerivation(Derivation d){
		for(Component c: this.getComponents()){
			if (c instanceof DerivationPanel)
				((DerivationPanel)c).setDerivation(d);
		}
	}
	
	
//	public static void main(String[] args) {
//		Variable S = new Variable("S");
//		Variable A = new Variable("A");
//		Terminal b = new Terminal("b");
//		
//		Production p1 = new Production(S, A,A);
//		Production p2 = new Production(S, A,A,S);
//		Production p3 = new Production(A,b);
//		
//		//derive bbbb
//		
//		Derivation d = new Derivation(p2);
//		d.addStep(p1, 2);
//		d.addAll(new Production[]{p3,p3,p3,p3}, new Integer[]{3,0,2,1});
//		
//		JFrame fram = new JFrame();
//		fram.add(new DerivationView(d));
//		fram.pack();
//		fram.setVisible(true);
//	}

}
