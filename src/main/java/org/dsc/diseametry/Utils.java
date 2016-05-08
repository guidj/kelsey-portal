package org.dsc.diseametry;

import java.util.List;

public class Utils {
	
	public static String collectionToString(Object[] objects) {
		String s = "";
		
		for(Object o: objects) {
			s += o.toString();
		}
		
		return s;
	}
	
	public static String collectionToString(List<Object>[] objects) {
		String s = "";
		
		for(Object o: objects) {
			s += o.toString();
		}
		
		return s;
	}
}
