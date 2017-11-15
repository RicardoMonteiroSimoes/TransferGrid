package Example;

import ch.rs.reflectorgrid.TransferGrid;

/**
 *
 * @author Ricardo
 */
public class ExampleDataObject {

    @TransferGrid
    private int port = 1010;
    @TransferGrid
    private String ip = "192.168.0.1";
    @TransferGrid
    private String message = "Hello";

    public ExampleDataObject(int port, String ip, String message) {
        this.port = port;
        this.ip = ip;
        this.message = message;
    }

    public void setData(int port, String ip, String message) {
        this.port = port;
        this.ip = ip;
        this.message = message;
    }

    public int getPort() {
        return port;
    }

    public String getIP() {
        return ip;
    }

    public String getMessage() {
        return message;
    }

}
