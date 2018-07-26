package ch.rs.reflectorgrid.util.interfaces;

import java.lang.reflect.Field;

@FunctionalInterface
public interface ChangeListener {

    /**
     * This interface is intended to be used in combination with TransferGrid.
     * You can implement this interface and add it trough
     * {@link ch.rs.reflectorgrid.ReflectorGrid}. It will be called every time there is an update,
     * and you will receive the object that was updated as well as the field that was updated.
     *
     * This is made specifically this way so that you can focus on only updating the necessary parts of your
     * Application.
     *
     * @param field The field of the object that was updated
     * @param object The object that was updated
     */
    void onObjectValueChanged(Field field, Object object);

}
