package file;

import file.xml.XMLCodec;


public class XMLFileChooser extends BasicFileChooser {

	public XMLFileChooser(){
		super(XMLCodec.getJFFfileFilter());
	}
}
