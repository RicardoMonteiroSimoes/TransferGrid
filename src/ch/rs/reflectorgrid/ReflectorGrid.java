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
package ch.rs.reflectorgrid;

import ch.rs.reflectorgrid.typeconversion.TypeConverterCollection;
import ch.rs.reflectorgrid.util.*;
import ch.rs.reflectorgrid.util.LabelDisplayOrder.InsertionPosition;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.lang.Number;
import javafx.scene.control.SpinnerValueFactory;

/**
 *
 * This class is used in conjunction with TransferGrid.java and enables a user
 * to generate a GridPane with labels and TextFields/ComboBoxes/TextAreas for
 * variables used in an Object. This also supports variables from inhereted
 * classes, aswell as private variables.
 *
 * In its current version, this class supports the following variable types:
 * <b>int, String, boolean, double, float</b>
 *
 * If your variable is not in here, the class will not be able to set it.
 *
 * Please make sure you know the possibilities that @TransferGrid enables,
 * before letting the end user change variables he is not supposed to.
 *
 * This class uses the JavaFX GridPane and due to the way it is built, you only
 * need one class to generate a Grid. If the object changes to another object
 * you want to show the Variables of, you can just call turnObjectIntoGrid()
 * again. Due to the nature of JavaFX it will automaticly update the Grid on
 * your GUI.
 *
 * @author Ricardo Daniel Monteiro Simoes
 * @author I-Al-Istannen, : https://github.com/I-Al-Istannen
 *
 */
public class ReflectorGrid {

    /**
     *
     * The GridPane that will get actualizied whenever you call
     * turnObjectIntoGrid. This way we update the grid according to the new
     * object.
     *
     */
    private GridPane grid = new GridPane();

    /**
     *
     * This object is the one we give with turnObjectIntoGrid(). It is needed to
     * set the field when using the TextFields etc.
     *
     */
    private Object gridObject = new Object();

    /**
     *
     * This is used to determine the format to which the grid is created.
     * {@link LabelDisplayOrder} for more information
     *
     */
    private LabelDisplayOrder displayOrder = LabelDisplayOrder.SIDE_BY_SIDE;

    /**
     * This variable is used to determine the max width of the editable fields.
     * Standard is set to 300.
     */
    private double NODE_WIDTH_LIMIT = 300;

    /**
     * This variable is used to set the way a Label is named. If it is set to
     * <b>VERBATIM</b>, the label will keep the same name as the variable. If it
     * is set to <b>SPLIT_TO_CAPITALIZED_WORDS</b>, it will work as in the
     * following example:
     *
     * portToSendTo -> Port To Send To
     *
     * {@link FieldNamingStrategy} for more information
     */
    private FieldNamingStrategy namingConvention = DefaultFieldNamingStrategy.SPLIT_TO_CAPITALIZED_WORDS;

    /**
     *
     */
    private TypeConverterCollection typeConverterCollection;

    /**
     * Sets some normal formatting for the grid.
     */
    public ReflectorGrid() {
        typeConverterCollection = new TypeConverterCollection();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
    }

    public GridPane transfromIntoGrid(Object object) {
        Objects.requireNonNull(object, "The received Object is null!");
        setGridObject(object);
        return generateGrid();
    }

    public void redoGrid() {
        generateGrid();
    }

    private void setGridObject(Object object) {
        gridObject = object;
    }

    private GridPane generateGrid() {
        clearGrid();

        List<Field> fields = ReflectionHelper.getAllFieldsInClassHierachy(
                gridObject.getClass(), this::shouldTransferToGrid
        );

        InsertionPosition insertionPosition = new InsertionPosition(0, 0);

        for (Field field : fields) {
            Pair<Label, Node> nodes = getNodePairForField(field, gridObject);
            insertionPosition = displayOrder
                    .addNode(insertionPosition, nodes.getKey(), nodes.getValue(), grid);
        }

        return grid;
    }

    private Pair<Label, Node> getNodePairForField(Field field, Object handle) {
        Label label = new Label(namingConvention.toString(field));
        Control node;

        TransferGrid annotation = field.getAnnotation(TransferGrid.class);

        if (annotation.options().length > 0) {
            node = createComboBox(annotation, field, handle);
        } else if (TypeHelper.isNumericType(field.getType())) {
            node = createSpinner(field, handle);
        } else {
            switch (annotation.fieldtype()) {
                case TEXT_FIELD:
                    node = createTextField(field, handle);
                    break;
                case TEXT_AREA:
                    node = createTextArea(field, handle);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown field type: " + annotation.fieldtype());
            }
        }

        adjustNodeProperties(annotation, node);

        return new Pair<>(label, node);

    }

    private void adjustNodeProperties(TransferGrid annotation, Control node) {
        setEditable(node, annotation.editable());
        node.setMouseTransparent(!annotation.editable());
        node.setFocusTraversable(annotation.editable());

        node.setMaxWidth(NODE_WIDTH_LIMIT);
    }

    private void setEditable(Control node, boolean editable) {
        if (node instanceof TextInputControl) {
            ((TextInputControl) node).setEditable(editable);
        } else if (node instanceof ComboBoxBase) {
            ((ComboBoxBase) node).setEditable(false);
        } else if (node instanceof Spinner) {
            ((Spinner) node).setEditable(editable);
        } else {
            throw new IllegalArgumentException("Can't make node uneditable: " + node);
        }
    }

    private Consumer<String> getStringListener(Field field, Object handle) {
        Consumer<String> changeListener = string -> {
            Object value = typeConverterCollection.fromString(field.getType(), string);
            ReflectionHelper.setFieldValue(field, handle, value);
        };

        return changeListener;
    }

    private Consumer<Object> getObjectListener(Field field, Object handle) {
        Consumer<Object> changeListener = object -> {
            Object value = typeConverterCollection.fromObject(field.getType(), object);
            ReflectionHelper.setFieldValue(field, handle, value);
        };

        return changeListener;
    }

//    private boolean isFieldNumber(Field field) {
//        System.out.println(field.getClass().isAssignableFrom(Number.class));
//        return field.getClass().isAssignableFrom(Number.class);
//    }
    private ComboBox<String> createComboBox(TransferGrid annotation, Field field,
            Object handle) {
        ComboBox<String> comboBox = new ComboBox<>(
                FXCollections.observableArrayList(annotation.options())
        );

        comboBox.getSelectionModel().select(
                objectToString(ReflectionHelper.getFieldValue(field, handle))
        );
        comboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ov, newValue)
                        -> getStringListener(field, handle).accept(newValue));

        return comboBox;
    }

    private TextField createTextField(Field field, Object handle) {
        TextField textField = new TextField(
                objectToString(ReflectionHelper.getFieldValue(field, handle))
        );

        textField.textProperty().addListener((obs, ov, newValue)
                -> getStringListener(field, handle).accept(newValue));

        return textField;
    }

    private TextArea createTextArea(Field field, Object handle) {
        TextArea textArea = new TextArea(objectToString(ReflectionHelper.getFieldValue(field, handle)));

        textArea.textProperty().addListener((obs, ov, newValue)
                -> getStringListener(field, handle).accept(newValue));

        return textArea;
    }

    private Spinner createSpinner(Field field, Object handle) {

        if (field.getType() == int.class) {
            Spinner<Integer> spinner = new Spinner<Integer>();
            SpinnerValueFactory<Integer> valueFactory
                    = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,
                            ReflectionHelper.getFieldValue(field, handle));

            spinner.setValueFactory(valueFactory);
            return spinner;
        }
        Spinner<Double> spinner = new Spinner<Double>();
        SpinnerValueFactory<Double> valueFactory
                = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE,
                        ReflectionHelper.getFieldValue(field, handle));

        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    private String objectToString(Object object) {
        return object == null ? "" : object.toString();
    }

    private void clearGrid() {
        grid.getChildren().clear();
    }

    private boolean shouldTransferToGrid(Field field) {
        return field.isAnnotationPresent(TransferGrid.class);
    }

    private void setMaxWidth(TextInputControl field) {
        field.setMaxWidth(NODE_WIDTH_LIMIT);
    }

    private void selectCurrentValue(Field field, ComboBox combo) {
        try {
            field.setAccessible(true);
            combo.getSelectionModel().select(field.get(gridObject));
            field.setAccessible(false);
        } catch (Exception e) {
            System.err.println("Couldnt set the value of the ComboBox " + e.getMessage());
            field.setAccessible(false);
        }

    }


    /**
     * Sets the format for the grid.
     *
     * {@link LabelDisplayOrder} for information about the different avaliable
     * formats.
     *
     * @param labelDisplayOrder
     */
    public void setLabelDisplayOrder(LabelDisplayOrder labelDisplayOrder) {
        displayOrder = labelDisplayOrder;
    }

    /**
     * Manually set your own limit to how wide TextInputs can get. Standard is
     * set to 300.
     * <b>This does not change the Width of an already generated Grid! You can
     * call redoGrid() to regenerate the Grid!</b>
     *
     * @param limit the max width to be use for the input fields.
     */
    public void setNodeWidthLimit(double limit) {
        NODE_WIDTH_LIMIT = limit;
    }

    /**
     *
     * @param fieldNamingStrategy The {@link FieldNamingStrategy} to use
     *
     * {@link FieldNamingStrategy} for information about the different avaliable
     * strategies.
     *
     */
    public void setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        namingConvention = fieldNamingStrategy;
    }

}
