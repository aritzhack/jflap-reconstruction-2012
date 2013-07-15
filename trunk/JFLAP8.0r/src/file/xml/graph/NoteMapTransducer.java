package file.xml.graph;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.BasicTransducer;
import file.xml.XMLHelper;

public class NoteMapTransducer extends BasicTransducer<Map<Point2D, String>> {

	private PointTransducer subTrans = new PointTransducer();

	@Override
	public Map<Point2D, String> fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, NOTE_TAG);
		Map<Point2D, String> map = new HashMap<Point2D, String>();

		for (int i = 0; i < list.size(); i++) {
			Element ele = list.get(i);
			Element p_ele = XMLHelper.getChildrenWithTag(ele, POINT_TAG).get(0);
			Element text_ele = XMLHelper.getChildrenWithTag(ele, VALUE_TAG).get(0);

			Point2D p = subTrans.fromStructureRoot(p_ele);
			String text = XMLHelper.containedText(text_ele);

			map.put(p, text);
		}
		return map;
	}

	@Override
	public Element toXMLTree(Document doc, Map<Point2D, String> structure) {
		Element root = XMLHelper.createElement(doc, getTag(), null, null);

		for (Point2D p : structure.keySet()) {
			String text = structure.get(p);

			Element sPoint = XMLHelper.createElement(doc, NOTE_TAG, null, null);
			sPoint.appendChild(XMLHelper.createElement(doc, VALUE_TAG, text,
					null));
			sPoint.appendChild(subTrans.toXMLTree(doc, p));

			root.appendChild(sPoint);
		}
		return root;
	}

	@Override
	public String getTag() {
		return NOTE_MAP_TAG;
	}

}
