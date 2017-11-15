package Example;

import ch.rs.reflectorgrid.TransferGrid;

/**
 *
 * @author Ricardo
 */
public class ExampleParentObject {
    
    @TransferGrid(editable = false)
    private String name;
    
    
    public ExampleParentObject(String name){
        this.name = name;
    }
    
}
