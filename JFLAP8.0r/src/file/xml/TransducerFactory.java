package file.xml;

import java.util.Map;
import java.util.TreeMap;

public class TransducerFactory {

	private static Map<Class, Transducer> myClassToTransducerMap;
	
	static{
		myClassToTransducerMap = new TreeMap<Class, Transducer>();
	}
	
	public static <T> Transducer<T> getTransducerForModel(T object){
		return myClassToTransducerMap.get(object.getClass());
	}
	
	public static Transducer getTransducerForTag(String tag){
		for (Transducer trans: myClassToTransducerMap.values())
		{
			if (trans.getType().equals(tag))
				return trans;
		}
		return null;
	}
	
}
