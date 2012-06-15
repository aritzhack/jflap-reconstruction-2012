package model.grammar.parsing.cyk.gui;

/**
 * @author Peggy Li
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.algorithms.transform.grammar.CNFConverter;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.parsing.cyk.CYKParser;
import file.xml.XMLCodec;

@SuppressWarnings("serial")
public class CYKTableView extends JPanel {
	

	public CYKTableView (CYKParser parser) {
		super(new GridLayout(1,0));
		 
        JTable table = new JTable(new CYKTableModel(parser.getParseTable(), new String[] {"", ""}));
        table.setPreferredScrollableViewportSize(new Dimension(200, 50));
        table.setFillsViewportHeight(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	
	
	public static void main (String[] args) {
		
		XMLCodec codec = new XMLCodec();
		File f = new File(System.getProperty("user.dir") + "/filetest/grammar.jff");
		Grammar g = (Grammar) codec.decode(f);

		CNFConverter converter = new CNFConverter(g);		
		converter.stepToCompletion();
		Grammar cnf = converter.getTransformedGrammar();

		CYKParser parser = new CYKParser(cnf);
		parser.setInput(new SymbolString(new Symbol("a"), new Symbol("b")));
		parser.stepToCompletion();
		
		// GUI frame
		JFrame frame = new JFrame("CYK Parse Table"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new CYKTableView(parser));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
