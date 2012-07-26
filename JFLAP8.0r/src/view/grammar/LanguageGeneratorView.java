package view.grammar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import model.algorithms.AlgorithmException;
import model.grammar.Grammar;
import model.languages.LanguageGenerator;
import model.symbols.SymbolString;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiableList;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.MagnifiableToolbar;
import view.formaldef.BasicFormalDefinitionView;
import view.grammar.productions.ProductionTable;

public class LanguageGeneratorView extends BasicFormalDefinitionView<Grammar> {

	private LanguageGenerator myGenerator;
	private MagnifiableList myList;

	public LanguageGeneratorView(Grammar g) {
		super(g, new UndoKeeper(), false);
		myGenerator = LanguageGenerator.createGenerator(g);
	}

	@Override
	public JComponent createCentralPanel(Grammar model, UndoKeeper keeper,
			boolean editable) {
		Component prodView = new ProductionTable(getDefinition(), getKeeper(),
				false);
		Component langView = new MagnifiableScrollPane(
				myList = new MagnifiableList(new DefaultListModel(),
						JFLAPPreferences.getDefaultTextSize()));
		MagnifiableSplitPane split = new MagnifiableSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, prodView, langView);

		LanguageInputtingPanel inputPanel = new LanguageInputtingPanel();

		MagnifiablePanel panel = new MagnifiablePanel(new BorderLayout());
		panel.add(inputPanel, BorderLayout.NORTH);
		panel.add(split, BorderLayout.CENTER);

		return panel;
	}

	@Override
	public String getName() {
		return "Language Generator";
	}

	private class LanguageInputtingPanel extends MagnifiableToolbar {

		private MagnifiableLabel myLabel;
		private MagnifiableTextField myTextField;
		private MagnifiableButton myLengthButton;
		private MagnifiableButton myGenerateButton;

		public LanguageInputtingPanel() {
			setFloatable(false);
			int size = JFLAPPreferences.getDefaultTextSize();

			myLabel = new MagnifiableLabel("Generate: ", size);
			myTextField = new MagnifiableTextField(size);
			myGenerateButton = new MagnifiableButton("strings", size);
			myLengthButton = new MagnifiableButton("length strings", size);

			this.add(myLabel);
			this.add(myTextField);
			this.add(myGenerateButton);
			this.add(myLengthButton);

			myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setupInteractions();
		}

		private void setupInteractions() {
			myGenerateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setList(myGenerator.getStrings((getNumberToGenerate())));
				}

			});

			myLengthButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setList(myGenerator
							.getStringsOfLength((getNumberToGenerate())));
				}
			});

		}

		private int getNumberToGenerate() {
			String input = myTextField.getText();
			try {
				int numToGen = Integer.parseInt(input);
				return numToGen;
			} catch (Exception e) {
				throw new AlgorithmException(
						"The entered string is not a numeric value!");
			}
		}

	}

	private void setList(List<SymbolString> generatedStrings) {
		SymbolString[] array = generatedStrings.toArray(new SymbolString[0]);
		DefaultListModel model = (DefaultListModel) myList.getModel();
		model.clear();
		for (SymbolString string : generatedStrings) {
			model.addElement(string);
		}
		repaint();
	}

}
