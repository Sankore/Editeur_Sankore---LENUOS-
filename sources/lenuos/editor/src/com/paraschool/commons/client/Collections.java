package com.paraschool.commons.client;

import java.util.ArrayList;
import java.util.List;

public final class Collections {

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		List<T> retval = new ArrayList<T>(list.size());
		for (T item : list) 
			if (!predicate.apply(item))
				retval.add(item);
		return retval;
	}

}
