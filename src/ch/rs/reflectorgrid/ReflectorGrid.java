/**
MIT License

Copyright (c) 2017 Ricardo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE
**/

package ch.rs.reflectorgrid;

import ch.rs.reflectorgrid.Transfergrid.Fieldtype;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * This class is used in conjunction with Transfergrid.java and enables a user
 * to generate a GridPane with labels and TextFields/ComboBoxes/TextAreas for
 * variables used in an Object. This also supports variables from inhereted
 * classes, aswell as private variables.
 *
 * In its current version, this class supports the following variable types:
 * <b>int, String, boolean, double, float</b>
 *
 * If your variable is not in here, the class will not be able to set it.
 *
 *
 * Please make sure you know the possibilities that @Transfergrid enables,
 * before letting the end user change variables he is not supposed to.
 *
 * This class uses the JavaFX GridPane and due to the way it is built, you only
 * need one class to generate a Grid. If the object changes to another object
 * you want to show the Variables of, you can just call turnObjectIntoGrid()
 * again. Due to the nature of JavaFX it will automaticly update the Grid on
 * your GUI.
 *
 * @author Ricardo S., RS
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

    //This is used to generate the Grid, formatted like:
    //Label | Field
    //Label | Field
    //or as a one column grid formated like:
    //Label
    //------
    //Field
    //-----
    //Label
    //etc.
    private boolean isSideBySide = true;

    //This variable is used to define the width limit of the Field. Default is
    //Set to 300.
    private double NODE_WIDTH_LIMIT = 300;

    public GridPane turnObjectIntoGrid(Object object) {
        gridObject = object;
        ArrayList<Label> labels = new ArrayList();
        ArrayList<Node> nodes = new ArrayList();
        ArrayList<Field> fields = new ArrayList();

        /**
         * This part checks for declared fields, including the ones in the
         * superclasses. Due to the nature of this function, unless a variable
         * is Tagged with @Transfergrid, it will not get used in the generation
         * of the grid, but will still be added to the field list.
         */
        fields.addAll(Arrays.asList(object.getClass().getDeclaredFields()));
        Class objClass = object.getClass().getSuperclass();
        while (hasSuperClass(objClass)) {
            fields.addAll(Arrays.asList(objClass.getDeclaredFields()));
            objClass = objClass.getSuperclass();
        }

        //Iterates trough all the found fields.
        for (Field field : fields) {

            //Gets the Transfergrid annotation of the variable.
            //If it does not have one, it turns null and gets interrupted in the next
            //if-statement
            Transfergrid annotation = field.getAnnotation(Transfergrid.class);

            //This makes sure that we dont start doing stuff for variables that are not supposed to be read.
            if (annotation != null) {
                labels.add(new Label(field.getName()));

                try {
                    field.setAccessible(true);
                    //First option is for ComboBox
                    if (hasOptions(annotation)) {
                        ComboBox valueNode = new ComboBox();
                        valueNode.getItems().addAll(annotation.options());
                        valueNode.setEditable(annotation.editable());
                        
                        if (valueNode.isEditable()) {
                            setValueChangerFunction(field, valueNode);
                        } else {
                            valueNode.setMouseTransparent(!valueNode.isEditable());
                            valueNode.setFocusTraversable(valueNode.isEditable());
                        }
                        
                        selectCurrentValue(field, valueNode);
                        
                        nodes.add(valueNode);

                        //second option for TextField
                    } else if (isTextField(annotation)) {
                        TextField valueNode = new TextField(getText(field));
                        valueNode.setEditable(annotation.editable());

                        if (valueNode.isEditable()) {
                            setValueChangerFunction(field, valueNode);
                        } else {
                            valueNode.setMouseTransparent(!valueNode.isEditable());
                            valueNode.setFocusTraversable(valueNode.isEditable());
                        }
                        
                        nodes.add(valueNode);

                        //last option for TextArea. This was made as else if in case
                        //of future additions
                    } else if (isTextArea(annotation)) {
                        TextArea valueNode = new TextArea(getText(field));
                        valueNode.setEditable(annotation.editable());

                        if (valueNode.isEditable()) {
                            setValueChangerFunction(field, valueNode);
                        } else {
                            valueNode.setMouseTransparent(!valueNode.isEditable());
                            valueNode.setFocusTraversable(valueNode.isEditable());
                        }
                        
                        nodes.add(valueNode);
                    }

                    //Sets the width of the last added Node.
                    //This mnakes sure we arent trying to set the width of the ComboBox...
                    if (nodes.get(nodes.size()-1).getClass() != ComboBox.class) {
                        setMaxWidth((TextInputControl) nodes.get(nodes.size() - 1));
                    }
                    field.setAccessible(false);

                } catch (Exception e) {
                    System.err.println("Error whilst constructing the getting"
                            + "the fields and annotations for the Grid!" + e.getMessage());
                    field.setAccessible(false);
                }
            }
        }

        //Generates the Grid and returns it.
        return generateGrid(labels, nodes);
    }

    private void setMaxWidth(TextInputControl field) {
        field.setMaxWidth(NODE_WIDTH_LIMIT);
    }

    private boolean isTextField(Transfergrid anot) {
        return ((anot).fieldtype() == Fieldtype.TEXTFIELD);
    }

    private boolean isTextArea(Transfergrid anot) {
        return ((anot).fieldtype() == Fieldtype.TEXTAREA);
    }

    private void selectCurrentValue(Field field, ComboBox combo){
        try{
            field.setAccessible(true);
            combo.getSelectionModel().select(field.get(gridObject));
            field.setAccessible(false);
        } catch (Exception e){
            System.err.println("Couldnt set the value of the ComboBox " + e.getMessage());
            field.setAccessible(false);
        }
        
    }

    private boolean hasOptions(Transfergrid anot) {
        return !((anot).options().length == 0);
    }

    private String getText(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (field.get(gridObject) == null) {
            return "";
        }
        return String.valueOf(field.get(gridObject));
    }

    private boolean hasSuperClass(Class object) {
        return (object.getSuperclass() != null);
    }

    private GridPane generateGrid(ArrayList<Label> labels, ArrayList<Node> nodes) {
        grid.getChildren().clear();
        int y = 0;
        int hpos = 0;
        for (Label lbl : labels) {
            GridPane.setHalignment(lbl, HPos.LEFT);
            lbl.setLayoutY(100);
            grid.add(lbl, 0, hpos);
            if (isSideBySide) {
                grid.add(nodes.get(y), 1, hpos);
            } else {
                hpos++;
                grid.add(nodes.get(y), 0, hpos);
            }
            hpos++;
            y++;
        }
        return grid;
    }

    private void setValueChangerFunction(Field field, TextField tempfield) {
        tempfield.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    field.setAccessible(true);
                    setObjectToField(field, tempfield);
                    field.setAccessible(false);
                }
            }

        });

    }

    private void setValueChangerFunction(Field field, ComboBox tempfield) {
        tempfield.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                field.setAccessible(true);
                setObjectToField(field, tempfield);
                field.setAccessible(false);
            }
        });

    }

    private void setValueChangerFunction(Field field, TextArea tempfield) {
        tempfield.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                field.setAccessible(true);
                setObjectToField(field, tempfield);
                field.setAccessible(false);
            }
        });
    }

    private void setObjectToField(Field field, TextField tempfield) {
        String text = tempfield.getText();
        try {
            switch (field.getType().getName()) {
                case "int":
                    field.set(gridObject, Integer.parseInt(text));
                    break;
                case "java.lang.String":
                    field.set(gridObject, text);
                    break;
                case "float":
                    field.set(gridObject, Float.parseFloat(text));
                    break;
                case "boolean":
                    field.set(gridObject, Boolean.parseBoolean(text));
                    break;
                case "double":
                    field.set(gridObject, Double.parseDouble(text));
                    break;
                default:
                    System.err.println("no case for " + field.getType().getName());
            }
            field.setAccessible(false);
        } catch (Exception e) {
            try {
                System.err.println("Couldn't cast the value to the desired field. " + e.getMessage());
                tempfield.setText(String.valueOf(field.get(gridObject)));
                field.setAccessible(false);
            } catch (Exception ee) {
                System.err.println("Couldn't reset the value of the field to the"
                        + " GUI TextField. " + e.getMessage());
            }

        }
    }

    private void setObjectToOption(Field field, String option) {
        try {
            field.set(gridObject, option);
        } catch (Exception e) {
            System.err.println("Couldn't set the option to the field." + e.getMessage());
        }
    }

    private void setObjectToField(Field field, ComboBox combo) {
        String text = combo.getSelectionModel().getSelectedItem().toString();
        try {
            setObjectToOption(field, text);
        } catch (Exception e) {
            System.err.println("Couldn't set the ComboBox to the field." + e.getMessage());
        }

    }

    private void setObjectToField(Field field, TextArea area) {
        try {
            field.set(gridObject, area.getText());
        } catch (Exception e) {
            System.err.println("Couldn't set the TextArea to the field." + e.getMessage());
        }

    }

    /**
     * Changes the generation method of the Grid to Side by Side. Example:
     * Label | Field
     * Label | Field
     */
    public void setSideBySide() {
        isSideBySide = true;
    }

        /**
     * Changes the generation method of the Grid to Above eachother. Example:
     * Label
     * Field
     * Label
     * Field
     * 
     */
    public void setAboveEach() {
        isSideBySide = false;
    }

    /**
     * Manually set your own limit to how wide TextInputs can get.
     * Standard is set to 300.
     * <b>This does not change the Width of an already generated Grid!
     * Call turnObjectIntoGrid() again to generate with the new Width!</b>
     * @param limit 
     */
    public void setNodeWidthLimit(double limit) {
        NODE_WIDTH_LIMIT = limit;
    }

}
