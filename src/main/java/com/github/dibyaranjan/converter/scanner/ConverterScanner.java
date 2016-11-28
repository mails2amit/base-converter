package com.github.dibyaranjan.converter.scanner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.github.dibyaranjan.infra.converter.Converter;
import com.github.dibyaranjan.infra.converter.annotation.Convert;
import com.github.dibyaranjan.infra.converter.vo.SourceTargetValue;

/**
 * A custom scanner to scan a package and find the implementation marked with
 * <code>@Converter</code>
 * 
 * @author Dibya
 */
public class ConverterScanner {

	/**
	 * Find all the classes in the base-package, check if it is marked with
	 * <code>@Converter</code>. If the class is an implementation of a
	 * converter, get the source type and target type. Create SourceTargetValue
	 * object, create and instance of the method. Set the SourceTargetValue as
	 * key and instance as value to the map and return.
	 * 
	 * @param basePackage
	 *            package to scan
	 * @return A map with key as SourceTargetValue and value as an instance of
	 *         the converter
	 */
	public Map<SourceTargetValue, Converter> scanConvertersFromPackage(String basePackage) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		String searchPath = getPackagePath(basePackage);
		try {
			Resource[] resources = resourcePatternResolver.getResources(searchPath);
			Map<SourceTargetValue, Converter> converterRegistry = new HashMap<>();
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					registerConverter(metadataReader, converterRegistry);
				}
			}
			return converterRegistry;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getPackagePath(String basePackage) {
		return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
				+ "/**/*.class";
	}

	private void registerConverter(MetadataReader metadataReader, Map<SourceTargetValue, Converter> converterRegistry) {
		try {
			String className = metadataReader.getClassMetadata().getClassName();
			Class<?> clazz = Class.forName(className);
			Convert annotation = clazz.getAnnotation(Convert.class);
			if (annotation != null) {
				Converter newInstance = createNewInstance(clazz);
				registerConverter(converterRegistry, annotation, newInstance);
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private Converter createNewInstance(Class<?> clazz) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = clazz.getConstructor();
		Converter newInstance = (Converter) constructor.newInstance();
		return newInstance;
	}

	private void registerConverter(Map<SourceTargetValue, Converter> converterRegistry, Convert annotation,
			Converter newInstance) {
		Class<?> source = annotation.source();
		Class<?> target = annotation.target();

		SourceTargetValue stv = new SourceTargetValue(source, target);
		converterRegistry.put(stv, newInstance);
	}
}
