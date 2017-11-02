package ch.rs.reflectorgrid.util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Describes how the label will be displayed
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
    public InsertionPosition addNode(InsertionPosition position, Label label, Node node,
        GridPane pane) {
      pane.add(label, position.column, position.row);
      pane.add(node, position.column + 1, position.row);
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
    public InsertionPosition addNode(InsertionPosition position, Label label, Node node,
        GridPane pane) {
      pane.add(label, position.column, position.row);
      pane.add(node, position.column, position.row + 1);
      return new InsertionPosition(position.row + 2, position.column);
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
  public abstract InsertionPosition addNode(InsertionPosition position, Label label, Node node,
      GridPane pane);

  public static class InsertionPosition {

    public final int row;
    public final int column;

    public InsertionPosition(int row, int column) {
      this.row = row;
      this.column = column;
    }
  }
}
