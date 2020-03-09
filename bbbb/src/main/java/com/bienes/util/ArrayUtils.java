package com.bienes.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
	
	public static List<Integer> getSeleccionados(String[] checks ){
		
		List<Integer> ids = new ArrayList<Integer>();
		if(checks != null) {
			for (String id : checks) {
				ids.add(Integer.valueOf(id));
			}
		}
		return ids;
	}
}
