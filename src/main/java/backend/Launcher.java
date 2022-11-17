package backend;

import backend.pojos.OpenWebSocketTask;
import backend.servers.AuthenticationServer;
import backend.servers.FlowServer;
import backend.servers.MainServer;
import backend.singltonDataHolder.DataHolder;
import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {

    public static WebSocket lastConn = null;

    public static void main(String[] args) throws InterruptedException {

        new Thread(AuthenticationServer::listen).start();
        new Thread(MainServer::listen).start();

        String host = "localhost";
        int port = 9191;
        ExecutorService service = Executors.newFixedThreadPool(2);
        Scanner sc = new Scanner(System.in);
        OpenWebSocketTask serverTask = new OpenWebSocketTask(host, port);
        service.execute(serverTask);

    }

    public static String getTimeString() {
        LocalDateTime time = LocalDateTime.now();
        return time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();

    }


}

