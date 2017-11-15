/**
 *
 * MIT License
 *
 * Copyright (c) 2017 Ricardo Daniel Monteiro Simoes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 *
 * */

package ch.rs.reflectorgrid.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Ricardo Daniel Monteiro Simoes
 */
public class TypeHelper {

    private static final Set<Class<?>> primitiveNumbers = Stream
            .of(int.class, long.class, float.class,
                    double.class, byte.class, short.class)
            .collect(Collectors.toSet());

    
    /**
     * @author Pshemo @ StackOverflow
     * https://stackoverflow.com/a/37656409/5471598
     * @param cls the class to check
     * @return true if the given class is of numeric nature
     */
    public static boolean isNumericType(Class<?> cls) {
        if (cls.isPrimitive()) {
            return primitiveNumbers.contains(cls);
        } else {
            return Number.class.isAssignableFrom(cls);
        }
    }

    /**
     * Checks to see if the given Class is of type Enum
     * @param type the class you want to check
     * @return true if the Class is OR extends Enum.class
     */
    public static boolean isEnum(Class<?> type) {
        return Enum.class.isAssignableFrom(type);
    }

    /**
     * Checks to see if the given Class is part of java.lang
     * @param type the class you want to check
     * @return true if the class is part of java.lang
     */
    public static boolean isJavaLang(Class<?> type) {
        return type.getName().startsWith("java.lang");
    }

}
