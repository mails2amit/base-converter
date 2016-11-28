package com.github.dibyaranjan.infra.converter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A custom annotation for marking the implementations of Converters. This will
 * hold the metadata for type of conversion an implementation supports.
 * 
 * @author Dibya
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert {
	Class<?> source();

	Class<?> target();
}
