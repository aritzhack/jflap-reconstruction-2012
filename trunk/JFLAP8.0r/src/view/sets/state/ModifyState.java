package view.sets.state;

import java.util.Set;

import errors.JFLAPException;
import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.SetsManager;
import model.sets.elements.Element;
import model.sets.elements.ElementsParser;
import model.undo.UndoKeeper;
import view.sets.edit.SetDefinitionPanel;
import view.sets.edit.SetsEditingPanel;

/**
 * State for modifying an existing set
 *
 *
 */
public class ModifyState extends State {

	private SetDefinitionPanel mySource;
	private AbstractSet myOriginalSet;
	private AbstractSet myModifiedSet;

	public ModifyState(SetDefinitionPanel source, AbstractSet set) {
		mySource = source;
		myOriginalSet = set;
	}

	@Override
	public AbstractSet finish(UndoKeeper keeper) throws Exception {
		SetsManager.ACTIVE_REGISTRY.remove(myOriginalSet);
		if (myOriginalSet.isFinite()) {

			String name = mySource.getSetName() == null ? CreateState.getAutomatedName() : mySource.getSetName();
			String description = mySource.getDescription();
			if (mySource.getElements() == null || mySource.getElements().trim().length() == 0) {
				throw new Exception("Set must contain at least one element!");
			}

			ElementsParser parser = new ElementsParser(mySource.getElements());
			try {
				Set<Element> elements = parser.parse();

				if (description == null)
					myModifiedSet = new FiniteSet(name, elements);
				myOriginalSet = new FiniteSet(name, description, elements);
			} catch (JFLAPException e) {

			} catch (Exception e) {

			}

		}
		return myOriginalSet;

	}


	@Override
	public SetsEditingPanel createEditingPanel(UndoKeeper keeper) {
		SetsEditingPanel editor = new SetsEditingPanel(keeper, true);
		editor.createFromExistingSet(myOriginalSet);
		return editor;
	}

	@Override
	public boolean undo() {
		SetsManager.ACTIVE_REGISTRY.remove(myModifiedSet);
		SetsManager.ACTIVE_REGISTRY.add(myOriginalSet);
		return true;
	}

	@Override
	public boolean redo() {
		SetsManager.ACTIVE_REGISTRY.add(myModifiedSet);
		SetsManager.ACTIVE_REGISTRY.remove(myModifiedSet);
		return true;
	}

}
