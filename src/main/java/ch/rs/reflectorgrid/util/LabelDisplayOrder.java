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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;

/**
 * Describes how the label will be displayed
 *
 * <b>ChangeLod 15.11.2017 by RDMS</b>
 *
 * Added new function for the insertion of a single Node
 * Changed the standard addNode function to accept 2 Nodes
 *
 * @author I-Al-Istannen, : https://github.com/I-Al-Istannen
 *
 */
public enum LabelDisplayOrder {
    /**
     * The labels are displayed on the left of the field.
     *
     * <br>
     * <pre>
     *  | Label | Field |
     *  | Label | Field |
     * </pre>
     */
    SIDE_BY_SIDE(2) {
        @Override
        public InsertionPosition addNode(InsertionPosition position, Node label, Node node,
                GridPane pane) {
            pane.add(label, position.column, position.row);
            pane.add(node, position.column + 1, position.row);
            return new InsertionPosition(position.row + 1, position.column);
        }

        @Override
        public InsertionPosition addNode(InsertionPosition position, Node node, GridPane pane) {
            pane.add(node, position.column, position.row);
            return new InsertionPosition(position.row + 1, position.column);
        }

        @Override
        public InsertionPosition addSeparator(InsertionPosition position, GridPane pane){
            pane.add(new Separator(), position.column, position.row);
            pane.add(new Separator(), position.column + 1, position.row);
            return new InsertionPosition(position.row + 1, position.column);
        }

    },
    /**
     * The labels are displayed above the field.
     *
     * <br>
     * <pre>
     *  | Label |
     *  | Field |
     *  | Label |
     *  | Field |
     * </pre>
     */
    ABOVE_FIELD(1) {
        @Override
        public InsertionPosition addNode(InsertionPosition position, Node label, Node node,
                GridPane pane) {
            pane.add(label, position.column, position.row);
            pane.add(node, position.column, position.row + 1);
            return new InsertionPosition(position.row + 2, position.column);
        }

        @Override
        public InsertionPosition addNode(InsertionPosition position, Node node, GridPane pane) {
            pane.add(node, position.column, position.row);
            return new InsertionPosition(position.row + 1, position.column);
        }

        @Override
        public InsertionPosition addSeparator(InsertionPosition position, GridPane pane){
            pane.add(new Separator(), position.column, position.row);
            return new InsertionPosition(position.row + 1, position.column);
        }
    };

    private int columns;

    LabelDisplayOrder(int columns) {
        this.columns = columns;
    }

    public int getColumnCount() {
        return columns;
    }

    /**
     * Adds a node with a label to a {@link GridPane}.
     *
     * @param position The position to add them at
     * @param label The {@link Label} to use
     * @param node The {@link Node} to add
     * @param pane The Pane to add it to
     * @return The new row and the new column
     */
    public abstract InsertionPosition addNode(InsertionPosition position, Node label, Node node,
            GridPane pane);

    /**
     * Adds a node to a {@link GridPane}.
     *
     * This node is supposed to be alone in one row, but might wary depending on implementation.
     *
     * @param position The position to add them at
     * @param node The {@link Node} to add
     * @param pane The Pane to add it to
     * @return The new row and the new column
     *
     */
    public abstract InsertionPosition addNode(InsertionPosition position, Node node, GridPane pane);

    /**
     * This function is used to input a JavaFX Separator in the next position.
     * @param position The position at which the separator will be inserted
     * @param pane the GridPane which is to receive the separator
     * @return InsertionPosition for the next Element
     */
    public abstract InsertionPosition addSeparator(InsertionPosition position, GridPane pane);

    public static class InsertionPosition {

        public final int row;
        public final int column;

        public InsertionPosition(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

}
