package ch.rs.reflectorgrid.typeconversion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.ByteStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.util.converter.ShortStringConverter;

/**
 * Contains all Type converters that will be used.
 *
 * @author I-Al-Istannen, : https://github.com/I-Al-Istannen
 */
public class TypeConverterCollection {

    private Map<Class<?>, StringConverter<?>> converterMap;

    public TypeConverterCollection() {
        converterMap = new HashMap<>();

        converterMap.put(Byte.class, new ByteStringConverter());
        converterMap.put(Byte.TYPE, new ByteStringConverter());
        converterMap.put(Short.class, new ShortStringConverter());
        converterMap.put(Short.TYPE, new ShortStringConverter());
        converterMap.put(Integer.class, new IntegerStringConverter());
        converterMap.put(Integer.TYPE, new IntegerStringConverter());
        converterMap.put(Long.class, new LongStringConverter());
        converterMap.put(Long.TYPE, new LongStringConverter());
        converterMap.put(Float.class, new FloatStringConverter());
        converterMap.put(Float.TYPE, new FloatStringConverter());
        converterMap.put(Double.class, new DoubleStringConverter());
        converterMap.put(Double.TYPE, new DoubleStringConverter());
        
        converterMap.put(Number.class, new NumberStringConverter());
        converterMap.put(BigDecimal.class, new BigDecimalStringConverter());
        converterMap.put(Boolean.class, new BooleanStringConverter());
    }

    public <T> void addConverter(Class<T> clazz, StringConverter<T> converter) {
        converterMap.put(clazz, converter);
    }

    public <T> String toString(Class<T> clazz, T object) {
        if (object == null) {
            return "";
        }

        if (object instanceof String) {
            return (String) object;
        }

        @SuppressWarnings("unchecked")
        StringConverter<T> stringConverter = (StringConverter<T>) converterMap.get(clazz);

        if (stringConverter == null) {
            throw new IllegalArgumentException("No converter registered for class " + clazz);
        }

        return stringConverter.toString(object);
    }

    public <T> T fromObject(Class<T> clazz, Object object) {
        if (object == null) {
            return null;
        }

        if (object.getClass().isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            T t = (T) object;
            return t;
        }
        
        @SuppressWarnings("unchecked")
        StringConverter<T> stringConverter = (StringConverter<T>) converterMap.get(clazz);

        if (stringConverter == null) {
            throw new IllegalArgumentException("No converter registered for class " + clazz);
        }

        return stringConverter.fromString(object.toString());
    }

    public <T> T fromString(Class<T> clazz, String string) {
        if (string == null) {
            return null;
        }

        if (String.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            T t = (T) string;
            return t;
        }

        @SuppressWarnings("unchecked")
        StringConverter<T> stringConverter = (StringConverter<T>) converterMap.get(clazz);

        if (stringConverter == null) {
            throw new IllegalArgumentException("No converter registered for class " + clazz);
        }

        return stringConverter.fromString(string);
    }
}
