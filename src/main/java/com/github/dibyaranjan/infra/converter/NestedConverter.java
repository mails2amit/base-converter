package com.github.dibyaranjan.infra.converter;

/**
 * Contract for implementing a converter which has an instance of BaseConverter
 * to delegate converter calls with in the converter.
 * 
 * The implementation of the class must store the converter supplied to the
 * setConverter method.
 * 
 * @author Dibya
 */
public interface NestedConverter extends Converter {
	void setConverter(Converter baseConverter);
}
