package universe.menu;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.JFLAPModel;



public class MainMenuButton extends JButton {

	private Class<? extends JFLAPModel> myClass;

	public MainMenuButton(Class<? extends JFLAPModel> clazz, String name) {
		super(name);
		myClass = clazz;
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFLAPModel model = ModelFactory.createModel(myClass, true);
				if (model == null ||
						JFLAPUniverse.createAndRegisterController(model) == null){
					JFLAPError.show("An error has occured in creating a new " + 
									MainMenuButton.this.getText() + ".", 
									"Error");
					return;
				}
				
				
				JFLAPUniverse.hideMainMenu();
			}
		});
	}

}
