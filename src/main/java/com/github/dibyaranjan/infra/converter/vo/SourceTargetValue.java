package com.github.dibyaranjan.infra.converter.vo;

/**
 * POJO class to hold mapping of converters.
 *
 * @author Dibya Ranjan
 */
public class SourceTargetValue {
    private Class<?> source;
    private Class<?> target;

    public SourceTargetValue(Class<?> source, Class<?> target) {
        this.source = source;
        this.target = target;
    }

    public Class<?> getSource() {
        return source;
    }

    public Class<?> getTarget() {
        return target;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        SourceTargetValue other = (SourceTargetValue) obj;
        if (source == null && other.source != null) {
            return false;
        }

        if (target == null && other.target != null) {
            return false;
        }

        if (source == other.source && target == other.target) {
            return true;
        }

        return false;

    }

    @Override
    public String toString() {
        return "SourceTargetValue [source=" + source + ", target=" + target + "]";
    }

}
