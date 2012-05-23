package file;

import javax.swing.JFileChooser;

import file.xml.XMLCodec;


public class XMLFileChooser extends JFileChooser {

	public XMLFileChooser(){
		super(System.getProperties().getProperty("user.dir"));
		this.setFileFilter(new XMLCodec());
	}
}
