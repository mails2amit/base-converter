package com.github.dibyaranjan.infra.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.github.dibyaranjan.infra.converter.vo.SourceTargetValue;

public class ConverterFactory {
	private Map<SourceTargetValue, Converter> converterRegistry;

	public void setConverterRegistry(Map<SourceTargetValue, Converter> converterRegistry) {
		this.converterRegistry = converterRegistry;
	}

	public Converter getConverter(SourceTargetValue stv) {
		Converter converter = converterRegistry.get(stv);
		if (converter == null) {
			throw new IllegalArgumentException("Converter not registered to convert " + stv.getTarget() + " from "
					+ stv.getSource());
		}

		return converter;
	}

	private Converter createNewInstance(Class<?> clazz) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = clazz.getConstructor();
		Converter newInstance = (Converter) constructor.newInstance();
		return newInstance;
	}

}
