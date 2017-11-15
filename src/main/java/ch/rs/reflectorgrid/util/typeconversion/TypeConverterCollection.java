/**
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package ch.rs.reflectorgrid.util.typeconversion;

import javafx.util.StringConverter;
import javafx.util.converter.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all Type converters that will be used.
 * Handles the conversion from String to Objects
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

    /**
     * Adds a Converter to the HashMap. This will then be used to convert Types.
     * @param clazz The Class of the Type you want to add
     * @param converter The corresponding Converter for said Class
     * @param <T> The Type of the class
     */
    public <T> void addConverter(Class<T> clazz, StringConverter<T> converter) {
        //Addition by RDMS to check if a converter for said class is already in the map
        if(converterMap.containsKey(clazz)){
            return;
        }
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
