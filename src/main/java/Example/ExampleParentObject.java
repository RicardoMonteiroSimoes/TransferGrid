package Example;

import ch.rs.reflectorgrid.util.annotations.TransferGrid;

/**
 *
 * @author Ricardo
 */
public class ExampleParentObject {
    
    @TransferGrid(editable = false)
    protected String name;
    
    
    public ExampleParentObject(String name){
        this.name = name;
    }
    
}
