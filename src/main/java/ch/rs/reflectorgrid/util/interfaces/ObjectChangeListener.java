package ch.rs.reflectorgrid.util.interfaces;

import java.lang.reflect.Field;

@FunctionalInterface
public interface ObjectChangeListener {

    /**
     * This interface is intended to be used in objects that will get utilized in combination with TransferGrid.
     * You do NOT have to implement this interface.
     * This Interface allows the user the see, which variable was updated. Therefore, an optimized way of
     * calling functions can be achieved.
     *
     * @param field The field of the object that was updated
     */
    void onFieldValueChanged(Field field);

}
