package ch.rs.reflectorgrid.util;

import java.lang.reflect.Field;

@FunctionalInterface
public interface UpdateInterface {
    void onFieldValueChanged(Field updatedField, Object value);
}
