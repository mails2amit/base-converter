package com.github.dibyaranjan.infra.converter;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * AbstractConverter class provides the skeletal implementation of Converter to
 * minimize the efforts required to implement the interface. The user of this
 * class has the control over the conversion of types by implementing the
 * abstract method.
 *
 * @author Dibya Ranjan
 */
public abstract class AbstractConverter implements Converter {
    protected Converter converter;

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    /**
     * Abstract method for the user of this class to provide concrete
     * implementation. The concrete implementation would provide the details of
     * source and target type.
     * 
     * @param sourceObject The sourceObject to get the data
     * @return converted object of type T
     */
    protected abstract <T, S> T doConvert(S sourceObject);

    @Override
    public <T, S> T convert(T target, S source) {
        if (target == null || source == null) {
            return target;
        }

        return doConvert(source);
    }

    protected Date parseDateFromSonarDefaultFormat(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }

        String tempDateString = dateString.replace('T', ' ');
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ssZ");

        DateTime parseDateTime = formatter.parseDateTime(tempDateString);

        return parseDateTime.toDate();
    }
}
