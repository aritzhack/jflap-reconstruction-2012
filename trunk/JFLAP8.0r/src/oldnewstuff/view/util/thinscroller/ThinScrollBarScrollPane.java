package oldnewstuff.view.util.thinscroller;

import javax.swing.JScrollPane;





public class ThinScrollBarScrollPane extends JScrollPane{


	
	public ThinScrollBarScrollPane(int bar_height, int vsbPolicy, int hsbPolicy){
		super();
		this.setHorizontalScrollBar(new ThinScrollBar(ScrollBar.HORIZONTAL, bar_height));
		this.setHorizontalScrollBar(new ThinScrollBar(ScrollBar.HORIZONTAL, bar_height));

	}

	
}
