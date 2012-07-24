package view.sets.state;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import errors.JFLAPException;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.elements.Element;
import model.sets.elements.ElementsParser;
import model.undo.UndoKeeper;
import view.sets.edit.SetDefinitionPanel;
import view.sets.edit.SetsEditingPanel;

/**
 * State for creating a new set entirely
 * 
 *
 */
public class CreateState extends State {

	private SetDefinitionPanel mySource;

	public CreateState(Component source) {
		mySource = (SetDefinitionPanel) source;
	}

	@Override
	public SetsEditingPanel createEditingPanel(UndoKeeper keeper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSet finish(UndoKeeper keeper) throws Exception {

		String name = mySource.getName() == null ? getAutomatedName() : mySource.getName();
		String description = mySource.getDescription();
		if (mySource.getElements() == null || mySource.getElements().trim().length() == 0) {
			throw new Exception("Set must contain at least one element!");
		}
		
		ElementsParser parser = new ElementsParser(mySource.getElements());
		try {
			Set<Element> elements = parser.parse();
			
			if (description == null)
				return new CustomFiniteSet(name, elements);
			return new CustomFiniteSet(name, description, elements);
		} catch (JFLAPException e) {
			
		} catch (Exception e) {
		
		}
		return null;
	}


	private String getAutomatedName() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		return System.getProperty("user.name") + format.format(cal.getTime());
	}

}
