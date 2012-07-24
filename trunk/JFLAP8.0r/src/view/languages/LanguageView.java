package view.sets.languages;

import javax.swing.JComponent;

import view.formaldef.BasicFormalDefinitionView;
import model.language.Language;
import model.undo.UndoKeeper;

public class LanguageView extends BasicFormalDefinitionView<Language>{
	
	
	public LanguageView (Language lang) {
		this(lang, new UndoKeeper(), true);
	}
	
	public LanguageView (Language lang, UndoKeeper keeper, boolean editable) {
		super(lang, keeper, editable);
	}

	@Override
	public JComponent createCentralPanel(Language model, UndoKeeper keeper,
			boolean editable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Language Editor";
	}

}
