package ch.rs.reflectorgrid.util;

import java.lang.reflect.Field;

/**
 * Defines how a {@link Field} name is mapped to a label in the GUI.
 * 
 * @author I-Al-Istannen, :-> https://github.com/I-Al-Istannen
 */
public interface FieldNamingStrategy {

  /**
   * @param field The {@link Field} to transform
   * @return The name of the field, according to this strategy
   */
  String toString(Field field);
}
