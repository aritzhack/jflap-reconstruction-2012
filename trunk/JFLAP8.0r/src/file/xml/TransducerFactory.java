package file.xml;

import java.util.Map;
import java.util.TreeMap;

public class TransducerFactory {

	private static Map<Class, Transducer> myClassToTransducerMap;
	private static Map<String, Transducer> myTagToTransducerMap;
	
	static{
		myClassToTransducerMap = new TreeMap<Class, Transducer>();
		myTagToTransducerMap = new TreeMap<String, Transducer>();
	}
	
	private static <T> void addMapping(Class<T> clazz, 
										Transducer<T> trans, 
										String tag){
		myClassToTransducerMap.put(clazz, trans);
		myTagToTransducerMap.put(tag, trans);
		
	}
	
	public static <T> Transducer<T> getTransducerForModel(T object){
		return myClassToTransducerMap.get(object.getClass());
	}
	
	public static Transducer getTransducerForTag(String tag){
		return myTagToTransducerMap.get(tag);
	}
	
}
