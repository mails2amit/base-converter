package com.github.dibyaranjan.infra.converter;

/**
 * 
 * "Converters" are the classes which changes "type". A typical converter will
 * take the source, get the data from the object manipulate if required and set
 * it to the target.
 * 
 * @author Dibya Ranjan
 */
public interface Converter {
	/**
	 * The implementation of this method should convert an object of type S to
	 * an object of type T and return the converted object.
	 * 
	 * @param target
	 *            target object to set data
	 * @param source
	 *            source object to get data from
	 * @return target object
	 */
	public <T, S> T convert(T target, S source);
}
