package view.grammar.parsing.brute;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.grammar.Grammar;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import oldnewstuff.view.tree.InputTableModel;
import oldnewstuff.view.tree.SelectNodeDrawer;
import oldnewstuff.view.tree.TreePanel;
import oldnewstuff.view.tree.UnrestrictedTreePanel;
import view.grammar.parsing.old.OldParsePane;
import view.grammar.productions.ProductionTable;

/**
 * This is a brute force parse pane.
 * 
 * @author Thomas Finley
 */
public class BruteParsePane extends OldParsePane {
	public int row = -1;
	/** The tree pane. */
	protected UnrestrictedTreePanel treePanel = new UnrestrictedTreePanel(this);

	/** The selection node drawer. */
	protected SelectNodeDrawer nodeDrawer = new SelectNodeDrawer();

	/** The progress bar. */
	protected JLabel progress = new JLabel(" ");

	/** The current parser object. */
	protected UnrestrictedBruteParser parser = null;

	protected InputTableModel myModel = null;

	public BruteParsePane(Grammar grammar, ProductionTable table) {
		super(grammar, table);
		this.setName("Brute Parser");
		initView();
	}

	/**
	 * Inits a parse table.
	 * 
	 * @return a table to hold the parse table
	 */
	protected JTable initParseTable() {
		return null;
	}

	/**
	 * Returns the interface that holds the input area.
	 */
	protected JPanel initInputPanel() {
		JPanel bigger = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.0;
		panel.add(new JLabel("Input"), c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(getInputField(), c);
		getInputField().addActionListener(startAction);
		// c.weightx = 0.0;
		// JButton startButton = new JButton(startAction);
		// startButton.addActionListener(listener);
		// panel.add(startButton, c);

		panel.add(progress, c);

		bigger.add(panel, BorderLayout.CENTER);
		bigger.add(initInputToolbar(), BorderLayout.NORTH);

		return bigger;
	}

	/**
	 * Returns a toolbar for the parser.
	 * 
	 * @return the toolbar for the parser
	 */
	protected JToolBar initInputToolbar() {
		JToolBar tb = super.initInputToolbar();
		return tb;
	}

	public void parseMultiple() {
		String[][] inputs = myModel.getInputs();
		int size = 1;
		Grammar currentGram = getGrammar();
		if (row < (inputs.length - 1)) {
			row++;
			SymbolString input = Symbolizers.symbolize(inputs[row][0], getGrammar());
			parseInput(input, parser);
		}
	}

	public void parseInput(SymbolString string, UnrestrictedBruteParser newParser) {
		if (newParser == null)
			parser = UnrestrictedBruteParser.createNewBruteParser(getGrammar());
		else
			parser = newParser;
		parser.setInput(string);
		final Timer timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parser == null)
					return;
				String nodeCount = "Nodes generated: "
						+ parser.getNumberOfNodes();
				progress.setText("Parser running.  " + nodeCount);
			}
		});

		String nodeCount = parser.getNumberOfNodes() + " nodes generated.";
		String status = null;
		timer.start();
		while(!parser.isDone()){
			parser.step();
		}
		status = "Parser started.";
		getStatusDisplay().setText(status);
		stepAction.setEnabled(true);

		progress.setText(status + "  " + nodeCount);
		if (parser.isDone()) {
			timer.stop();
			// parser = null;

			if (parser.isReject()) {
				progress.setText("String rejected. Try another String.");
				// Rejected!
				treePanel.setAnswer(null);
				treePanel.repaint();
				stepAction.setEnabled(false);
				return;
			}
			status = "String accepted!";
			Derivation derivation = parser.getDerivation();
			
			getStatusDisplay().setText("Press step to show derivations.");
//			this needs to be done. JULIAN????
//			treePanel.setAnswer(answer);
			treePanel.repaint();
		}

	}

	/**
	 * This method is called when there is new input to parse.
	 * 
	 * @param string
	 *            a new input string
	 */
	public void input(SymbolString input) {
		if (parser != null) {
			parser.setInput(input);
		}
		parseInput(input, null);
	}

	/**
	 * Returns the choices for the view.
	 * 
	 * @return an array of strings for the choice of view
	 */
	protected String[] getViewChoices() {
		return new String[] { "Noninverted Tree", "Derivation Table" };
	}

	/**
	 * This method is called when the step button is pressed.
	 */
	public boolean step() {
		//TODO: Tree/Derviation drawing
		JFLAPDebug.print("TODO!!");
		// controller.step();
//		boolean worked = false;
//		if (treePanel.next()) {
//			stepAction.setEnabled(false);
//			worked = true;
//		}
//
//		treePanel.repaint();
//		return worked;
		return false;
	}

	/**
	 * Inits a new tree panel. This overriding adds a selection node drawer so
	 * certain nodes can be highlighted.
	 * 
	 * @return a new display for the parse tree
	 */
	protected TreePanel initTreePanel() {
		return treePanel;
	}

}
