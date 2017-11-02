
import ch.rs.reflectorgrid.TransferGrid;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */
public class ExampleClass {
    
    @TransferGrid
    private int port = 8080;
    
    @TransferGrid(editable = false)
    private String serverIp = "127.0.0.1";
    
    @TransferGrid
    private String messageToSend;
    
    @TransferGrid
    private double longVariable = 5063453;
    
}
