package backend.servers;

import backend.databaseservice.DbServices;
import backend.singltonDataHolder.DataHolder;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class FlowServer extends WebSocketServer {
    public static WebSocket conn = null;
    private InetSocketAddress address = null;

    public FlowServer(InetSocketAddress address) throws InterruptedException {
        super(address);
        this.address = address;

    }

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 9191;
        FlowServer fs = new FlowServer(new InetSocketAddress(host, port));
        fs.start();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        this.conn = conn;
        conn.send("What is your name?");


        System.out.println("CONNECTED on Flow with\t" + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("قام الطرف الآخر بإغلاق الإتصال" + "\n" + reason);
        DataHolder.getInstance().removeConnectedClient(conn);
        String name = DataHolder.getInstance().getClientName(conn);
        String[] subs = DbServices.retrieveSubs(name).split(",");
        for (String sub : subs) {
            if (sub.isBlank() || sub == null) continue;
            DataHolder.getInstance().removeSubscription(name, sub);
        }


    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        if (message.trim().startsWith("name: ")) {
            String name = message.trim().split("\\s+")[1];
            DataHolder.getInstance().upateConnectedClient(name, conn);
            String[] subs = DbServices.retrieveSubs(name).split(",");
            for (String sub : subs) {
                if (sub.isBlank() || sub == null) continue;
                DataHolder.getInstance().addSubscription(name, sub);
            }

        }
    }

    public void sendMessage(String message) {
        System.out.println("POST to SEND:\t" + message);
        conn.send(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Problem in connecting to " + conn.getRemoteSocketAddress() + ":" + ex);
        DataHolder.getInstance().removeConnectedClient(conn);

    }


    @Override
    public void onStart() {
        System.out.println("Flow WebSocket Server started working on " + address);
        System.out.println("-".repeat(80));
    }

    public WebSocket getClient() {
        return conn;
    }

}