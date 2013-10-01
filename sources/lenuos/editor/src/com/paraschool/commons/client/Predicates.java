package com.paraschool.commons.client;

import java.util.Collection;

public final class Predicates {

	public static <T> Predicate<T> and(final Collection<? extends Predicate<T>> predicates) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T input) {
				for (Predicate<T> t : predicates) {
					if(!t.apply(input))
						return false;
				}
				return true;
			}
			
		};
	}
	
	public static <T> Predicate<T> or(final Collection<? extends Predicate<T>> predicates) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T input) {
				for (Predicate<T> t : predicates) {
					if(t.apply(input))
						return true;
				}
				return false;
			}
		};
	}
	
	public static <T> Predicate<T> not(final Predicate<T> predicate) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T input) {
				return !predicate.apply(input);
			}
			
		};
	}
	
	public static <T> Predicate<T> accept() {
		return new Predicate<T>() {
			@Override
			public boolean apply(T input) {
				return false;
			}
			
		};
	}
}
