package backend.servers;

import backend.handlers.Processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {


    public static void main(String[] args) {
        listen();
    }

    public static void listen() {
        int port = 12345;

        try {
            // رقم مائة هو عدد الاتصالات التي ممكن ان تنتظر حتى نصنع لها ثريد خاص بها
            ServerSocket serverSocket = new ServerSocket(port, 100);
            System.out.println("MainServer started listening on: localhost:" + port);
            System.out.println("-".repeat(80));

            while (true) { // تكرار دائم, يستلم اتصال جديد ويضعه في ثريد مستقل

                final Socket activeSocket = serverSocket.accept(); // نستلم اتصال جديد
                new Thread(() -> receiveNewRequest(activeSocket)).start(); // نرسل الاتصال لثريد جديد
            }

        } catch (IOException e) {
            if ("Socket closed".equals(e.getMessage())) {
                System.out.println("Sever Stopped listening" + "\n----------------\n");
            } else {
                System.out.println(e.getCause() + "\n----------------\n");
            }
        }
    }

    // هذه الدالة تعمل داخل ثريد مستقل كي تتعامل مع طلبات العميل
    private static void receiveNewRequest(Socket socket) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String r;
            StringBuilder sb = new StringBuilder();
            while (!(r = in.readLine()).equals("||---#end#---||")) {
                sb.append(r).append("\n");
            }
            Processor.processRequest(sb.toString(),out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
