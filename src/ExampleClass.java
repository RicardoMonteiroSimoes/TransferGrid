
import ch.rs.reflectorgrid.Transfergrid;

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
    
    @Transfergrid
    private int port;
    
    @Transfergrid(editable = false)
    private String serverIp = "127.0.0.1";
    
    @Transfergrid
    private String messageToSend;
}
