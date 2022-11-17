package frontend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RetrievePostClient {


    public static void main(String[] args) throws IOException {
        System.out.println(retrieve("9ac20240-6cb8-4489-91cc-41b75ef10f9c"));
    }

    public static String retrieve(String id) {

        String serverMsg = null;
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected");
            out.println("RCV_MSG msg_id:" + id);
            out.println("||---#end#---||");

            System.out.print("Server: ");
            StringBuilder sb = new StringBuilder();
            String line = "";
            while (!"||---#end#---||".equals(line = in.readLine())) sb.append(line).append("\n");
            serverMsg = sb.toString();
            System.out.println(serverMsg);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverMsg;
    }

}
