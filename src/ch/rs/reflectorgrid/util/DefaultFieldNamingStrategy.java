package ch.rs.reflectorgrid.util;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Contains some basic {@link FieldNamingStrategy}s.
 * 
 * @author I-Al-Istannen, : https://github.com/I-Al-Istannen
 */
public enum DefaultFieldNamingStrategy implements FieldNamingStrategy {
  VERBATIM(Field::getName),
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
