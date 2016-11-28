package com.github.dibyaranjan.infra.converter;

import java.util.Map;

import com.github.dibyaranjan.converter.scanner.ConverterScanner;
import com.github.dibyaranjan.infra.converter.vo.SourceTargetValue;

public class BaseConverter implements Converter {
	private ConverterFactory converterFactory;

	public BaseConverter(String packageToScan) {
		ConverterScanner converterScanner = new ConverterScanner();
		Map<SourceTargetValue, Converter> converterRegistry = converterScanner.scanConvertersFromPackage(packageToScan);
		converterFactory = new ConverterFactory();
		converterFactory.setConverterRegistry(converterRegistry);
	}

	@Override
	public <T, S> T convert(T target, S source) {
		SourceTargetValue stv = new SourceTargetValue(source.getClass(), target.getClass());

		Converter converter = converterFactory.getConverter(stv);

		if (converter instanceof AbstractConverter) {
			AbstractConverter abstractConverter = (AbstractConverter) converter;
			abstractConverter.setConverter(this);
		}

		return (T) converter.convert(target, source);
	}
}
