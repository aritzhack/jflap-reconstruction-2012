package oldnewstuff.view.util.thinscroller;

import javax.swing.JScrollPane;





public class ThinScrollBarScrollPane extends JScrollPane{


	
	public ThinScrollBarScrollPane(int bar_height, int vsbPolicy, int hsbPolicy){
		super(vsbPolicy, hsbPolicy);
		this.setHorizontalScrollBar(new ThinScrollBar(ScrollBar.HORIZONTAL, bar_height));
		this.setVerticalScrollBar(new ThinScrollBar(ScrollBar.VERTICAL, bar_height));

	}

	public ThinScrollBarScrollPane(int i) {
		this(i, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	
}
