package model.algorithms.testinput.parse.cyk;

import javax.swing.border.*;
import javax.swing.JSplitPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

import util.JFLAPConstants;
import util.UtilFunctions;

import model.algorithms.testinput.parse.ParserException;
import model.algorithms.transform.grammar.CNFConverter;
import model.automata.*;
import model.grammar.*;
import model.regex.*;
import model.symbols.*;
import model.symbols.symbolizer.Symbolizer;
import model.symbols.symbolizer.Symbolizers;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CYKGUI extends JFrame implements JFLAPConstants{

	private JPanel contentPane;
	private JPanel buttonBar;
	private JButton helpButton;
	private JButton stepButton;
	private JButton StepToCompletion;
	private JTextPane infoDisplay;
	private JScrollPane scrollPane;
	private JTable grammarTable;
	private JButton showDerivation;
	private JPanel panel;
	private JTextField textField;
	private JButton btnParse;
	private JScrollPane panel_1;
	private JTextPane derivationTable;
	private CYKParser myParser;
	private JPanel graphicViewPanel;
	private JScrollPane scrollPane_1;
	private JTable parseTable;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InputAlphabet input = new InputAlphabet();
					input.add(new Symbol(Character.toString('0')));
					input.add(new Symbol(Character.toString('1')));
					RegularExpressionGrammar gram = new RegularExpressionGrammar(input, new OperatorAlphabet());
					gram.trimAlphabets();
					CNFConverter conv = new CNFConverter(gram);		
					conv.stepToCompletion();
					Grammar CNFgram = conv.getTransformedGrammar();
					CYKParser parser = new CYKParser(CNFgram);
					
					CYKGUI frame = new CYKGUI(parser);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CYKGUI(CYKParser cyk) {
		this.myParser = cyk;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 895, 500);
		initContentPane();
	}
	
	public void initContentPane(){
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		initInfoPane();
		initGraphicViewPanel();
	}
	
	public void initGraphicViewPanel() {
		graphicViewPanel = new JPanel();
		contentPane.add(graphicViewPanel, BorderLayout.SOUTH);
		graphicViewPanel.setPreferredSize( new Dimension(400, 250));
		graphicViewPanel.setLayout(new CardLayout(0, 0));
		
		scrollPane_1 = new JScrollPane();
		graphicViewPanel.add(scrollPane_1, "name_1304778301065623");
		
		parseTable = new JTable();
		scrollPane_1.setViewportView(parseTable);
		
		derivationTable = new JTextPane();
		derivationTable.setEditable(false);
		derivationTable.setBounds(0, 49, 869, 251);
		panel_1 = new JScrollPane(derivationTable);

		graphicViewPanel.add(panel_1, "name_1303634671293069");
		panel_1.setPreferredSize(new Dimension(400,300));
		
	}

	public void initInfoPane(){
		
		JSplitPane infoPane = new JSplitPane();
		contentPane.add(infoPane, BorderLayout.NORTH);
		initInputPanel(infoPane);
		initGrammarTable(infoPane);
		
	}
	
		public void initInputPanel(JSplitPane infoPane){
			JPanel inputPanel = new JPanel();
			inputPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			infoPane.setRightComponent(inputPanel);
			inputPanel.setLayout(new BorderLayout(0, 0));
			
			buttonBar = new JPanel();
			inputPanel.add(buttonBar, BorderLayout.SOUTH);
			initButtonBar();

			infoDisplay = new JTextPane();
			infoDisplay.setText("Here is where text telling the user what to do would go");
			infoDisplay.setPreferredSize(new Dimension(400, 100));
			infoDisplay.setEditable(false);
			inputPanel.add(infoDisplay, BorderLayout.EAST);
			
			panel = new JPanel();
			inputPanel.add(panel, BorderLayout.WEST);
			panel.setLayout(new BorderLayout(0, 0));
			
			textField = new JTextField();
			textField.setColumns(10);
			panel.add(textField, BorderLayout.SOUTH);
			
			btnParse = new JButton("Parse!");
			btnParse.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					CardLayout cl = (CardLayout) graphicViewPanel.getLayout();
					cl.show(graphicViewPanel, "name_1304778301065623");
					Symbolizer s = Symbolizers.getSymbolizer(myParser.getGrammar());
					SymbolString input = s.symbolize(textField.getText());
					if(input.size()>0){
						myParser.setInput(input);
						StepToCompletion.setEnabled(true);
						stepButton.setEnabled(true);
						helpButton.setEnabled(true);
						String[] terminals = new String[input.size()];
						for(int i=0;i<terminals.length;i++){
							terminals[i] = input.get(i).toString();
						}
						parseTable.setModel(new DefaultTableModel(
							new Object[terminals.length][terminals.length],
							terminals
						));
					}
				}
			});
			
			panel.add(btnParse, BorderLayout.CENTER);
			panel.setPreferredSize(new Dimension(200, 100));
			
		}
		
		public void initButtonBar(){
			helpButton = new JButton("Help");
			helpButton.setEnabled(false);
			helpButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					//TODO: Help function.
				}
			});
			buttonBar.add(helpButton);
			
			stepButton = new JButton("Step");
			stepButton.setEnabled(false);
			stepButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(!myParser.isDone()){
						myParser.step();
						fillParseTable();
					}
					if(myParser.isDone()){
						setDerivationComplete();
					}
				}
			});
			buttonBar.add(stepButton);
			
			StepToCompletion = new JButton("Step to Completion");
			StepToCompletion.setEnabled(false);
			StepToCompletion.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(!myParser.isDone()){
						while(!myParser.isDone()){
							myParser.step();
							fillParseTable();
						}
					}
					setDerivationComplete();
					
				}
			});
			buttonBar.add(StepToCompletion);
			
			showDerivation = new JButton("Show Derivation");
			showDerivation.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					switchToDerivationView();	
				}
			});
			showDerivation.setEnabled(false);
			buttonBar.add(showDerivation);
		}
		
		public void initInputFields(){
			
		}
		
		public void fillParseTable(){
			for(int i=0; i<parseTable.getColumnCount();i++){
				for(int j=0; j<parseTable.getColumnCount();j++){
					if(myParser.getNodeAtIndex(i, j)!= null){
						if(myParser.getNodeAtIndex(i, j).size()==0){
							parseTable.setValueAt(EMPTY_SET_SYMBOL, i, j);
						}else{
							parseTable.setValueAt(myParser.getNodeAtIndex(i, j), i, j);
						}
					}
				}
			}
		}
		
		public void setDerivationComplete(){
			StepToCompletion.setEnabled(false);
			stepButton.setEnabled(false);
			helpButton.setEnabled(false);
			showDerivation.setEnabled(true);
		}
		
		public void switchToDerivationView(){
			derivationTable.setText("");
			CardLayout cl = (CardLayout) graphicViewPanel.getLayout();
			cl.show(graphicViewPanel, "name_1303634671293069");
			if(myParser.isAccept()){
				ArrayList<Production> list = new ArrayList<Production>();
				for(int i=0;i<myParser.getDerivation().getResultArray().length;i++){
					list.add(myParser.getDerivation().getProduction(i));
				}
				derivationTable.setText(UtilFunctions.toDelimitedString(myParser.getDerivation().getResultArray(),"\n"));
			}else{
				derivationTable.setText("String is not accepted!");
			}
		}
		
		public void initGrammarTable(JSplitPane infoPane){
			scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new Dimension(200,200));
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			infoPane.setLeftComponent(scrollPane);
			
			SymbolString[][] array = new SymbolString[myParser.getGrammar().getProductionSet().size()][3];
			ArrayList<Production> list = new ArrayList<Production>(myParser.getGrammar().getProductionSet());
			for(int i=0;i<list.size();i++){
				array[i][0] = new SymbolString(list.get(i).getLHS());
				array[i][1] = new SymbolString(new Symbol("->"));
				array[i][2] = new SymbolString(list.get(i).getRHS());
			}
			grammarTable = new JTable();
			grammarTable.setModel(new DefaultTableModel( array,
				new String[] {
					"LHS", "->", "RHS"
				}
			));
			grammarTable.getColumnModel().getColumn(1).setPreferredWidth(32);
			grammarTable.getColumnModel().getColumn(2).setPreferredWidth(118);
			scrollPane.setViewportView(grammarTable);
		}
}
