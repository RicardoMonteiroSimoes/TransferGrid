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

package ch.rs.reflectorgrid.util.statics;

import ch.rs.reflectorgrid.util.NumberSpinnerValueFactory;
import ch.rs.reflectorgrid.util.annotations.TransferGrid;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * This "Collection" contains different functions to create Nodes that are connected to a
 * {@link java.lang.reflect.Field} and contains the needed Listeners for changes in the node.
 * Created by Ricardo on 15.11.2017.
 */
public class ReflectionNodeCollection {

    public static ComboBox<String> createComboBox(TransferGrid annotation, Field field,
                                                   Object handle, Object masterObject) {

        ComboBox<String> comboBox = new ComboBox<>(
                FXCollections.observableArrayList(annotation.options())
        );

        comboBox.getSelectionModel().select(
                objectToString(ReflectionHelper.getFieldValue(field, handle))
        );
        comboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ov, newValue)
                        -> ListenerCollection.getStringListener(field, handle, masterObject).accept(newValue));

        return comboBox;
    }

    public static <T extends Enum<T>> ComboBox<T> createEnumComboBox(Class<T> clazz, Field field,
                                                                      Object handle, Object masterObject) {

        ComboBox<T> comboBox = new ComboBox<>(FXCollections.observableArrayList(clazz.getEnumConstants()));
        comboBox.getSelectionModel().select(
                (ReflectionHelper.getFieldValue(field, handle))
        );
        comboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ov, newValue)
                        -> ListenerCollection.getEnumListener(field, handle, masterObject).accept(newValue));

        return comboBox;
    }

    public static TextField createTextField(Field field, Object handle, Object masterObject) {
        TextField textField = new TextField(
                objectToString(ReflectionHelper.getFieldValue(field, handle))
        );

        textField.textProperty().addListener((obs, ov, newValue)
                -> ListenerCollection.getStringListener(field, handle, masterObject).accept(newValue));

        return textField;
    }

    public static TextArea createTextArea(Field field, Object handle, Object masterObject) {
        TextArea textArea = new TextArea(objectToString(ReflectionHelper.getFieldValue(field, handle)));

        textArea.textProperty().addListener((obs, ov, newValue)
                -> ListenerCollection.getStringListener(field, handle, masterObject).accept(newValue));

        return textArea;
    }

    /**
     * This Functions creates a Spinner with the {@link NumberSpinnerValueFactory} so that it can fit
     * any Numeric field.
     * @param field The field itself that gets connected to the spinner
     * @param handle the object in which the field is connected
     * @param masterObject the masterObject of the field
     * @return a Spinner with the {@link NumberSpinnerValueFactory} built in aswell as all needed Listeners.
     */
    public static Spinner createSpinner(Field field, Object handle, Object masterObject) {

        Spinner<BigDecimal> spinner = new Spinner<BigDecimal>();
        SpinnerValueFactory<BigDecimal> valueFactory
                = new NumberSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE,
                BigDecimal.valueOf(
                        ReflectionHelper.<Number>getFieldValue(field, handle).doubleValue()), field.getType());

        spinner.setValueFactory(valueFactory);
        spinner.valueProperty().addListener((obs, ov, newValue)
                -> ListenerCollection.getObjectListener(field, handle, masterObject).accept(newValue));
        return spinner;

    }

    private static String objectToString(Object object) {
        return object == null ? "" : object.toString();
    }

}
