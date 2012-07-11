package view.grammar.parsing.cyk;

import javax.swing.JComponent;
import javax.swing.JFrame;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiableToolbar;
import view.grammar.parsing.FindFirstParserView;

public class CYKParseView extends FindFirstParserView <CYKParseTablePanel>{

	public CYKParseView(Parser alg) {
		super(alg);
	}

	@Override
	public CYKParseTablePanel createRunningView(Parser alg) {
		CYKParseTablePanel table = new CYKParseTablePanel((CYKParser) alg);
		return table;
	}
	
	public static void main (String[] args){
		Variable S = new Variable("S");
		Variable A = new Variable("A");
		Terminal a = new Terminal("a");

		Production p1 = new Production(S, A, A);
		Production p2 = new Production(A, A, A);
		Production p3 = new Production(A, a);

		// derive bbbb

		Grammar g = new Grammar();
		g.getProductionSet().add(p1);
		g.getProductionSet().add(p2);
		g.getProductionSet().add(p3);
		g.setStartVariable(S);
		
		CYKParser parser = new CYKParser(g);

		JFrame fram = new JFrame();
		fram.add(new CYKParseView(parser));
		fram.pack();
		fram.setVisible(true);
	}

	@Override
	public MagnifiableToolbar createToolbar(Parser alg) {
		// TODO Auto-generated method stub
		return null;
	}

}
