package frontend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RepublishClient {
    public static void main(String[] args) throws IOException {
        connect("me","6d7a229d-9d6f-430b-9f21-1350ec3c73d1");
    }

    public static String connect(String user, String id) {

        String serverMsg = null;
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected");
            out.println("REPUBLISH author:@"+user+" msg_id:"+id);
            out.println("||---#end#---||");

            System.out.print("Server: ");
            StringBuilder sb = new StringBuilder();
            String line = "";
            while (!"||---#end#---||".equals(line = in.readLine())) sb.append(line).append("\n");
            serverMsg = sb.toString();
            System.out.println(serverMsg);


        }catch (IOException e){e.printStackTrace();}
        return serverMsg;
    }
}