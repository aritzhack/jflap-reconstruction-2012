package view.grammar.parsing.old;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import model.grammar.Grammar;
import model.undo.UndoKeeper;
import oldnewstuff.view.tree.DefaultTreeDrawer;
import oldnewstuff.view.tree.LeafNodePlacer;
import oldnewstuff.view.tree.TreePanel;
import universe.JFLAPUniverse;
import util.view.SplitFactory;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.environment.JFLAPEnvironment;
import view.grammar.parsing.cyk.CYKParseTablePanel;
import view.grammar.productions.ProductionTable;
import debug.JFLAPDebug;

/**
 * Main pane for CYK Parsing
 * 
 * @author Ian McMahon
 * 
 */
public class CYKParsePane extends EditingPanel {

	private Grammar myGrammar;
	private DefaultTreeDrawer treeDrawer;
	private TreePanel treePanel;
	private JPanel TDCardPane;
	private ProductionTable myProductionTable;
	private CYKParseController myController;
	private JTextField inputField;
	private JPanel contentPane;
	private Container treeDerivationPane;
	private CYKParseTablePanel parseTable;
	private SizeSlider slider;

	public CYKParsePane(Grammar grammar, ProductionTable table) {
		super(new UndoKeeper(), false);
		myGrammar = grammar;

		myProductionTable = new ProductionTable(myGrammar, getKeeper(), false);
		myProductionTable.setModel(table.getModel());

		setLayout(new BorderLayout());
		setName("CYK Parser");

		initView();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initView() {
		JFLAPEnvironment environment = JFLAPUniverse.getActiveEnvironment();
		JLabel statusDisplay = new JLabel(
				"Input a string and press Start to begin");

		initTreeDerivationPane();

		parseTable = new CYKParseTablePanel(myGrammar);

		slider = new SizeSlider(myProductionTable, parseTable);
		slider.distributeMagnification();

		myController = new CYKParseController(parseTable, this, statusDisplay);

		JScrollPane parsePane = new JScrollPane(parseTable);
		JScrollPane grammarTable = new JScrollPane(myProductionTable);

		contentPane = new JPanel(new CardLayout());
		contentPane.add(parsePane, "0");

		JPanel bottomPane = new JPanel(new BorderLayout());
		bottomPane.add(contentPane, BorderLayout.CENTER);
		bottomPane.add(slider, BorderLayout.SOUTH);

		JSplitPane topSplit = SplitFactory.createSplit(environment, true, 0.3, grammarTable,
				initInputPanel());
		JPanel topPane = new JPanel(new BorderLayout());
		topPane.add(topSplit, BorderLayout.CENTER);
		topPane.add(initParseToolbar(), BorderLayout.SOUTH);

		JSplitPane mainSplit = SplitFactory.createSplit(environment, false, 0.25, topPane, bottomPane);

		add(mainSplit, BorderLayout.CENTER);
		add(statusDisplay, BorderLayout.SOUTH);
	}

	/**
	 * Initializes pane that contains the Tree/Derivation view
	 */
	private void initTreeDerivationPane() {
		TDCardPane = new JPanel(new CardLayout());

		treePanel = initTreePanel();
		TDCardPane.add(treePanel, "0");

		JScrollPane derivationPane = initDerivationPane();
		TDCardPane.add(derivationPane, "1");

		JToolBar treeControls = initTreeControls();

		treeDerivationPane = new JPanel(new BorderLayout());
		treeDerivationPane.add(treeControls, BorderLayout.NORTH);
		treeDerivationPane.add(TDCardPane, BorderLayout.CENTER);
	}

	/**
	 * Initializes the TreePanel to be used for drawing derivation tree
	 */
	private TreePanel initTreePanel() {
		treeDrawer = new DefaultTreeDrawer(new DefaultTreeModel(
				new DefaultMutableTreeNode())) {
			protected Color getNodeColor(TreeNode node) {
				return node.isLeaf() ? LEAF : INNER;
			}

			private final Color INNER = new Color(100, 200, 120),
					LEAF = new Color(255, 255, 100);
		};
		treeDrawer.hideAll();
		treeDrawer.setNodePlacer(new LeafNodePlacer());
		return new TreePanel(treeDrawer);
	}

	/**
	 * Initializes table within a JScrollPane that will be used to display the
	 * derivation.
	 */
	private JScrollPane initDerivationPane() {
		DefaultTableModel derivationModel = new DefaultTableModel(new String[] {
				"Production", "Derivation" }, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		JTable derivationTable = new JTable(derivationModel);
		derivationTable.setGridColor(Color.lightGray);
		JScrollPane derivationPane = new JScrollPane(derivationTable);
		return derivationPane;
	}

	/**
	 * Initializes a toolbar with a JComboBox (used to select tree vs inverted
	 * tree vs derivation view) and a button used to step through the
	 * derivation.
	 */
	private JToolBar initTreeControls() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		final JComboBox box = new JComboBox(getViewChoices());
		box.setSelectedIndex(0);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeView((String) box.getSelectedItem());
			}
		};
		box.addActionListener(listener);

		toolbar.add(box);
		toolbar.addSeparator();
		toolbar.add(new AbstractAction("Next derivation") {
			public void actionPerformed(ActionEvent e) {
				stepTreeDerivation();
			}
		});

		return toolbar;
	}

	/**
	 * Returns the choices used in the JComboBox for each type of view.
	 */
	private String[] getViewChoices() {
		return new String[] { "Noninverted Tree", "Inverted Tree",
				"Derivation Table" };
	}

	/**
	 * Initializes a JPanel with a JTextField used for user input (responds to
	 * the enter key)
	 */
	private JPanel initInputPanel() {
		inputField = new JTextField();
		JPanel panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.0;
		panel.add(new JLabel("Input"), c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(inputField, c);
		inputField.addActionListener(myController.startAction);

		return panel;
	}

	/**
	 * Returns the tool bar for the main parse control.
	 */
	private JToolBar initParseToolbar() {
		JToolBar toolbar = new JToolBar();

		toolbar.add(myController.startAction);

		toolbar.add(myController.selectedAction);
		myController.selectedAction.setEnabled(false);

		toolbar.add(myController.stepAction);
		myController.stepAction.setEnabled(false);

		toolbar.add(myController.allAction);
		myController.allAction.setEnabled(false);

		toolbar.add(myController.nextAction);
		myController.nextAction.setEnabled(false);

		toolbar.add(myController.derivationAction);
		myController.derivationAction.setEnabled(false);

		toolbar.setFloatable(false);
		return toolbar;
	}

	/**
	 * Changes the view of the tree/derivation pane
	 * 
	 * @param name
	 *            the view button name that was pressed
	 */
	private void changeView(String name) {
		CardLayout treeDerivationLayout = (CardLayout) TDCardPane.getLayout();

		if (name.equals("Derivation Table")) {
			treeDerivationLayout.last(TDCardPane);
			return;
		}
		if (name.equals("Noninverted Tree"))
			treeDrawer.setInverted(false);
		else
			treeDrawer.setInverted(true);

		treeDerivationLayout.first(TDCardPane);
		treePanel.repaint();
	}


	/**
	 * Switches contentPane to tree/derivation view.
	 */
	public void switchToDerivationView() {	
		JFLAPEnvironment environment = JFLAPUniverse.getActiveEnvironment();
		
		// Copies the parseTable
		CYKParseTablePanel table = new CYKParseTablePanel(parseTable.getModel());
		slider.addListener(table);
		slider.distributeMagnification();

		JSplitPane derivationSplit = SplitFactory.createSplit(environment,
				true, 0.425, table, treeDerivationPane);
		contentPane.add(derivationSplit, "1");
		CardLayout card = (CardLayout) contentPane.getLayout();
		card.show(contentPane, "1");
	}

	/**
	 * Switches contentPane to parse table view.
	 */
	public void switchToParseView() {
		CardLayout card = (CardLayout) contentPane.getLayout();
		card.first(contentPane);
	}

	/**
	 * Returns the text entered by the user in the input field.
	 */
	public String getInputText() {
		return inputField.getText();
	}

	/**
	 * Returns a copy of the grammar for the parser to be initialized.
	 */
	public Grammar getGrammar() {
		return myGrammar.copy();
	}

	/**
	 * This method steps the tree/derivation to the next level (unless it is
	 * already at the final step).
	 */
	public void stepTreeDerivation() {
		// TODO: Wait for tree drawing to done
		// boolean worked = false;
		// if (treePanel) {
		// stepAction.setEnabled(false);
		// worked = true;
		// }
		//
		// treePanel.repaint();
		// return worked;
		JFLAPDebug.print("TODO!!");
	}

}