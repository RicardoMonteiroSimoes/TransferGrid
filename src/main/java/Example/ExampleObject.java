package Example;

import ch.rs.reflectorgrid.util.annotations.TransferGrid;
import ch.rs.reflectorgrid.util.annotations.TransferGridChangeListener;
import ch.rs.reflectorgrid.util.interfaces.ListenerInterface;

import java.lang.reflect.Field;

/**
 *
 */
public class ExampleObject extends ExampleParentObject implements ListenerInterface {

    @TransferGrid
    private String functionName = "testFunction";

    @TransferGrid
    private int number = 20;

    @TransferGrid(options = {"you", "are", "nice"})
    private String choiceForYou = "you";

    @TransferGrid(fieldtype = TransferGrid.Fieldtype.TEXT_AREA)
    private String textArea;

    @TransferGrid(tooltip = "Defines the runtime of this application")
    private ExampleEnum option = ExampleEnum.FULL;
    
    @TransferGrid
    private ExampleDataObject data;

    @TransferGrid(editable = false)
    private String uneditableForYou = "blocked";

    @TransferGridChangeListener
    private void updateObject(Field field){
        System.out.println(name + " had field " + field.getName() + " updated!");
    }


    public ExampleObject() {
        super("ExampleName");
        data = new ExampleDataObject(8090, "127.0.0.1", "hello\n");
    }

    @Override
    public void onFieldValueChanged(Field field, Object object) {
        System.out.println("Interface updated");
    }
}
