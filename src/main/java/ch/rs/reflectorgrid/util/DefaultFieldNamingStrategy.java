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

package ch.rs.reflectorgrid.util;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Contains some basic {@link FieldNamingStrategy}s.
 * 
 * @author I-Al-Istannen, : https://github.com/I-Al-Istannen
 */
public enum DefaultFieldNamingStrategy implements FieldNamingStrategy {
  /**
   * {@link DefaultFieldNamingStrategy}.VERBATIM returns the name the Variable has
   */
  VERBATIM(Field::getName),

  /**
   * {@link DefaultFieldNamingStrategy}.SPLIT_TO_CAPITALIZED_WORDS returns the name split up and in CamelCase.
   * Example:
   * int receiverPort turns into Receiver Port
   */
  SPLIT_TO_CAPITALIZED_WORDS(field -> {
    StringBuilder output = new StringBuilder();

    boolean wordBoundary = true;

    for (char c : field.getName().toCharArray()) {
      if (Character.isUpperCase(c) || c == '_') {
        wordBoundary = true;
      }

      if (wordBoundary) {
        if (output.length() > 0) {
          output.append(" ");
        }
        output.append(Character.toUpperCase(c));
        wordBoundary = false;
      } else {
        output.append(Character.toLowerCase(c));
      }
    }

    return output.toString();
  });

  private Function<Field, String> transformationFunction;

  DefaultFieldNamingStrategy(Function<Field, String> transformationFunction) {
    this.transformationFunction = transformationFunction;
  }

  @Override
  public String toString(Field field) {
    return transformationFunction.apply(field);
  }
}
