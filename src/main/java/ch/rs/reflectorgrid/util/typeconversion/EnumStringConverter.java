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
package ch.rs.reflectorgrid.util.typeconversion;

import javafx.util.StringConverter;

/**
 * This class represents an EnumStringConverter. This creates an EnumStringConverter for one Type of Enum;
 * for multiple you will need multiple EnumStringConverters.
 *
 * @author Ricardo Daniel Monteiro Simoes
 */
public class EnumStringConverter<T extends Enum<T>> extends StringConverter<T> {

    /**
     * This Variable saves the Class this Converter is supposed to use for later.
     */
    private Class<T> enumType;

    /**
     * Constructs the Converter and saves the Enum class for later usage.
     * @param enumType the enum class you want the converter to work for
     */
    public EnumStringConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public String toString(T object) {
        enumType.getClass().cast(object);
        return object.name();
    }

    @Override
    public T fromString(String string) {
        return Enum.valueOf(enumType, string);
    }

}
