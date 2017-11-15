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
package ch.rs.reflectorgrid;

import ch.rs.reflectorgrid.util.*;
import ch.rs.reflectorgrid.util.LabelDisplayOrder.InsertionPosition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is used in conjunction with TransferGrid.java and enables a user
 * to generate a GridPane with labels and TextFields/ComboBoxes/TextAreas for
 * variables used in an Object. This also supports variables from inhereted
 * classes, aswell as private variables.
 *
 * Please make sure you know the possibilities that @TransferGrid enables,
 * before letting the end user change variables he is not supposed to.
 * <p>
 * This class uses the JavaFX GridPane and due to the way it is built, you only
 * need one class to generate a Grid. If the object changes to another object
 * you want to show the Variables of, you can just call turnObjectIntoGrid()
 * again. Due to the nature of JavaFX it will automaticly update the Grid on
 * your GUI.
 *
 * @author Ricardo Daniel Monteiro Simoes
 * <b>Autho of some nifty little tricks:</b> I-Al-Istannen, : https://github.com/I-Al-Istannen
 */
public class ReflectorGrid {

    /**
     * The GridPane that will get actualizied whenever you call
     * turnObjectIntoGrid. This way we update the grid according to the new
     * object.
     */
    private GridPane grid = new GridPane();

    /**
     * This object is the one we give with turnObjectIntoGrid(). It is needed to
     * set the field when using the TextFields etc.
     */
    private Object gridObject = new Object();

    /**
     * This is used to determine the format to which the grid is created.
     * {@link LabelDisplayOrder} for more information
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
     * <p>
     * portToSendTo -> Port To Send To
     * <p>
     * {@link FieldNamingStrategy} for more information
     */
    private FieldNamingStrategy namingConvention = DefaultFieldNamingStrategy.SPLIT_TO_CAPITALIZED_WORDS;

    /**
     * Sets some normal formatting for the grid.
     */
    public ReflectorGrid() {
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
    }

    public ReflectorGrid(ReflectorGrid refGrid) {
        grid.setHgap(refGrid.getHgap());
        grid.setVgap(refGrid.getVgap());
        grid.setPadding(refGrid.getPadding());
        this.NODE_WIDTH_LIMIT = refGrid.getNodeWidth();
        this.displayOrder = refGrid.getDisplayOrder();
        this.namingConvention = refGrid.getNamingConvention();
    }

    public GridPane transfromIntoGrid(Object object) {
        Objects.requireNonNull(object, "The received Object is null!");
        setGridObject(object);
        return generateGrid();
    }


    /**
     * This Method can be used externally to regenerate the Grid.
     * This is supposed to be used when you do Layoutchanges after generating the Grid.
     */
    public void redoGrid() {
        generateGrid();
    }

    private void setGridObject(Object object) {
        gridObject = object;
    }

    /**
     * This function starts all necessary functions to generate the grid and returns it.
     *
     * @return The finished Grid for the given Object.
     */
    private GridPane generateGrid() {
        clearGrid();

        InsertionPosition insertionPosition = new InsertionPosition(0, 0);

        for (Field field : gridObject.getClass().getDeclaredFields()) {
            insertionPosition = handleField(insertionPosition, field, gridObject);
        }

        return grid;
    }

    /**
     * This Function handles the use case, when an Object contains an Object in itself.
     * If annoted correctly, it will create a new TransferGrid for said "sub-object" and then take its Nodes and add them
     * to this GridPane. It also adds in a "separator" with the Name of the object in the parent object.
     *
     * @param insertionPosition
     * @param field
     * @param subObject
     * @return
     */
    private InsertionPosition handleSubClassField(InsertionPosition insertionPosition, Field field, Object subObject) {
        insertionPosition = displayOrder.addNode(insertionPosition, new Label(namingConvention.toString(field) + ":"), new Separator(), grid);
        ReflectorGrid tempRefGrid = new ReflectorGrid(this);
        Object object = ReflectionHelper.getFieldValue(field, subObject);
        insertionPosition=  addGridElements(insertionPosition, tempRefGrid.transfromIntoGrid(object));
        return displayOrder.addNode(insertionPosition, new Separator(), new Separator(), grid);
    }

    /**
     * This function takes the Nodes of a subObject and moves them to the main GridPane.
     * Due to the nature of JavaFX we need to cast the subObject GridPane to a List.
     * @param position The starting position for the positioning of the Nodes in
     *                 the main GridPane
     * @param gridToAdd The GridPane of the subObject
     * @return returns the position for the next node in the GridPane
     */
    protected InsertionPosition addGridElements(InsertionPosition position, GridPane gridToAdd) {
        Label label = null;
        for (Node node : new ArrayList<>(gridToAdd.getChildren())) {
            if (label == null) {
                label = (Label) node;
            } else {
                position = displayOrder.addNode(position, label, node, grid);
                label = null;
            }
        }
        return position;
    }

    /**
     * This function handles a single Field. It looks if the field is a normal field or a subObject-
     * @param insertionPosition the position in which the Nodes for this field get inserted
     * @param field the Field itself
     * @param object the object to which the field belongs. This change was needed in case the Field
     *               belongs to a subObject rather than the object itself.
     * @return position for the next Node in the Grid
     */
    private InsertionPosition handleField(InsertionPosition insertionPosition, Field field, Object object) {
        if (TypeHelper.isNumericType(field.getType()) || TypeHelper.isJavaLang(field.getType()) || TypeHelper.isEnum(field.getType())) {
            Pair<Label, Node> nodes = getNodePairForField(field, object);
            return insertionPosition = displayOrder
                    .addNode(insertionPosition, nodes.getKey(), nodes.getValue(), grid);
        }
        return insertionPosition = handleSubClassField(insertionPosition, field, object);
    }

    /**
     * Creates a Pair of a Label aswell as an InputField for normal decalred Fields.
     * @param field the field itself
     * @param handle the object it belongs to
     * @return a Pair<> consisting of a Label with the Fieldname aswell as an InputField
     */
    private Pair<Label, Node> getNodePairForField(Field field, Object handle) {
        Label label = new Label(namingConvention.toString(field));
        Control node;
        System.out.println("doing " + field.getName());
        TransferGrid annotation = field.getAnnotation(TransferGrid.class);

        if (annotation.options().length > 0) {
            node = ReflectionNodeCollection.createComboBox(annotation, field, handle, gridObject);
        } else if (TypeHelper.isNumericType(field.getType())) {
            node = ReflectionNodeCollection.createSpinner(field, handle, gridObject);
        } else if (TypeHelper.isEnum(field.getType())) {
            node = ReflectionNodeCollection.createEnumComboBox((Class<? extends Enum>) field.getType(), field, handle, gridObject);
        } else {
            switch (annotation.fieldtype()) {
                case TEXT_FIELD:
                    node = ReflectionNodeCollection.createTextField(field, handle, gridObject);
                    break;
                case TEXT_AREA:
                    node = ReflectionNodeCollection.createTextArea(field, handle, gridObject);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown field type: " + annotation.fieldtype());
            }
        }

        adjustNodeProperties(annotation, node);

        return new Pair<>(label, node);

    }

    /**
     * This method is used internally to set if a field is editable or not.
     * @param annotation the annotation of said field; is needed to determine if it can be edited or not
     * @param node the node which is supposed to be set according to the annotation
     */
    private void adjustNodeProperties(TransferGrid annotation, Control node) {
        setEditable(node, annotation.editable());
        node.setMouseTransparent(!annotation.editable());
        node.setFocusTraversable(annotation.editable());

        node.setMaxWidth(NODE_WIDTH_LIMIT);
    }

    /**
     * This function handles the different .setEditable functions depending on the type of inputField
     * @param node the inputField itself
     * @param editable if it is editable or not
     */
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

    private void clearGrid() {
        grid.getChildren().clear();

    }

    /**
     * Checks to see if given Field has the correct annotation
     * @param field the field itself
     * @return true if it contains the annotation <b>@TransferGrid</b>
     */
    private boolean shouldTransferToGrid(Field field) {
        return field.isAnnotationPresent(TransferGrid.class);
    }

    private void setMaxWidth(TextInputControl field) {
        field.setMaxWidth(NODE_WIDTH_LIMIT);
    }

    /**
     * This method sets the value to show in the GUI for the ComboBox.
     * @param field the field with the value ot show
     * @param combo the comboBox to set
     */
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
     * <p>
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
     * @param fieldNamingStrategy The {@link FieldNamingStrategy} to use
     *                            <p>
     *                            {@link FieldNamingStrategy} for information about the different avaliable
     *                            strategies.
     */
    public void setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        namingConvention = fieldNamingStrategy;
    }

    private double getNodeWidth() {
        return NODE_WIDTH_LIMIT;
    }

    private LabelDisplayOrder getDisplayOrder() {
        return displayOrder;
    }

    private FieldNamingStrategy getNamingConvention() {
        return namingConvention;
    }

    private double getHgap() {
        return grid.getHgap();
    }

    private double getVgap() {
        return grid.getVgap();
    }

    private Insets getPadding() {
        return grid.getPadding();
    }

}
