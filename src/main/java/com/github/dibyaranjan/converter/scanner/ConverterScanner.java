package com.github.dibyaranjan.converter.scanner;

import java.io.IOException;
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
	public Map<SourceTargetValue, Class<?>> scanConvertersFromPackage(String basePackage) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		String searchPath = getPackagePath(basePackage);
		try {
			Resource[] resources = resourcePatternResolver.getResources(searchPath);
			Map<SourceTargetValue, Class<?>> converterRegistry = new HashMap<>();
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

	private void registerConverter(MetadataReader metadataReader, Map<SourceTargetValue, Class<?>> converterRegistry) {
		try {
			String className = metadataReader.getClassMetadata().getClassName();
			Class<?> clazz = Class.forName(className);
			Convert annotation = clazz.getAnnotation(Convert.class);
			if (annotation != null) {
				registerConverter(converterRegistry, annotation, clazz);
			}
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private void registerConverter(Map<SourceTargetValue, Class<?>> converterRegistry, Convert annotation,
			Class<?> clazz) {
		Class<?> source = annotation.source();
		Class<?> target = annotation.target();

		SourceTargetValue stv = new SourceTargetValue(source, target);
		converterRegistry.put(stv, clazz);
	}
}
