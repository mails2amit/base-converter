package com.github.dibyaranjan.infra.converter;

import java.util.Map;

import com.github.dibyaranjan.converter.scanner.ConverterScanner;
import com.github.dibyaranjan.infra.converter.vo.SourceTargetValue;

/**
 * An entry point to the converter abstraction. An object of this will be able
 * to identify the converter required to convert the requested objects and
 * delegates the call to appropriate converter. If the converter is a
 * NestedConverter, an instance of BaseConverter is injected.
 * 
 * @author Dibya
 */
public class BaseConverter implements Converter {
	private ConverterFactory converterFactory;

	/**
	 * Creates an instance of BaseConverter. Wires a ConverterFactory and
	 * ConverterRegistry. To create converter registry classes marked with
	 * Converter annotation should be scanned.
	 * 
	 * @param packageToScan
	 *            The base pakage for the converter
	 */
	public BaseConverter(String packageToScan) {
		ConverterScanner converterScanner = new ConverterScanner();
		Map<SourceTargetValue, Class<?>> converterRegistry = converterScanner.scanConvertersFromPackage(packageToScan);
		converterFactory = new ConverterFactory();
		converterFactory.setConverterRegistry(converterRegistry);
	}

	@Override
	public <T, S> T convert(T target, S source) {
		SourceTargetValue stv = new SourceTargetValue(source.getClass(), target.getClass());

		Converter converter = converterFactory.getConverter(stv);

		if (converter instanceof NestedConverter) {
			NestedConverter nestedConverter = (NestedConverter) converter;
			nestedConverter.setConverter(this);
		}

		return (T) converter.convert(target, source);
	}
}
