package backend.pojos;

import backend.servers.FlowServer;
import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;

public class OpenWebSocketTask implements Runnable {
    FlowServer server = null;
    private String url;
    private int port;


    public OpenWebSocketTask(String url, int port) {
        this.url = url;
        this.port = port;
    }

    @Override
    public void run() {

        while (server == null) {
            try {
                server = new FlowServer(new InetSocketAddress(url, port));
            } catch (InterruptedException e) {
                System.out.print("\r waiting to open port ");
            }
        }
//        server.setConnectionLostTimeout(120);
        server.run();
    }

    public WebSocket getClient() {

        return server.getClient();
    }
}

