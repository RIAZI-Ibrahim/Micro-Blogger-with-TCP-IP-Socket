package frontend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReplyClient {
    public static void main(String[] args) throws IOException {
        connect("myself","how could you","ce00fa15-68fb-4923-b78f-5abe75de2c55");
    }

    public static String connect(String user, String post, String replyto) {

        String serverMsg = null;
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected");
            out.println("REPLY author:@"+user+" reply_to_id:"+replyto);
            out.println(post);
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