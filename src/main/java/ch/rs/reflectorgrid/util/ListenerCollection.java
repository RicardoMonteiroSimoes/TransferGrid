/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Ricardo Daniel Monteiro Simoes
 * <p>
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
package ch.rs.reflectorgrid.util;

import ch.rs.reflectorgrid.util.typeconversion.EnumStringConverter;
import ch.rs.reflectorgrid.util.typeconversion.TypeConverterCollection;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * This class has a Collection of different Listeners. These can be used in a GUI to save the user Input into the
 * correct Field.
 * Created by Ricardo on 15.11.2017.
 * @author Ricardo Daniel Monteiro Simoes
 */
public class ListenerCollection {


    /**
     * This collection contains all needed TypeConverters.
     * In the case of Enums, it adds every new Enum-Type to the converterCollection.
     */
    private static TypeConverterCollection typeConverterCollection = new TypeConverterCollection();

    public static Consumer<String> getStringListener(Field field, Object handle, Object fieldObject) {
        Consumer<String> changeListener = string -> {
            Object value = typeConverterCollection.fromString(field.getType(), string);
            ReflectionHelper.setFieldValue(field, handle, value);
        };
        return changeListener;
    }


    public static Consumer<Enum> getEnumListener(Field field, Object handle, Object fieldObject) {
        Class<? extends Enum> clazz = (Class<? extends Enum>) field.getType();
        typeConverterCollection.addConverter(clazz, new EnumStringConverter(clazz));
        Consumer<Enum> changeListener = enumType -> {
            Object value = typeConverterCollection.fromObject(field.getType(), enumType);
            ReflectionHelper.setFieldValue(field, handle, value);
        };
        return changeListener;
    }


    public static Consumer<BigDecimal> getBigDecimalListener(Field field, Object handle, Object fieldObject) {
        Consumer<BigDecimal> changeListener = BigDecimal -> {
            Object value = typeConverterCollection.fromObject(field.getType(), BigDecimal);
            ReflectionHelper.setFieldValue(field, handle, value);
        };
        return changeListener;
    }

    public static Consumer<Object> getObjectListener(Field field, Object handle, Object fieldObject) {
        Consumer<Object> changeListener = object -> {
            Object value = typeConverterCollection.fromObject(field.getType(), object);
            ReflectionHelper.setFieldValue(field, handle, value);
        };

        return changeListener;
    }
}


