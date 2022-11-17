package frontend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RetriveIdsClient {

    public static void main(String[] args) throws IOException {
        System.out.println(retrieve("", "", "", 3));
    }

    public static String retrieve(String user, String tag, String sinceId,int limit) {

        String serverMsg = null;
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected");
            out.println("RCV_IDS author:@"+user+" tag:#"+tag+" since_id:"+sinceId+" limit:"+limit);
            out.println("||---#end#---||");

            System.out.print("IdServer: ");
            StringBuilder sb = new StringBuilder();
            String line = "";
            while (!"||---#end#---||".equals(line = in.readLine())) sb.append(line).append("\n");
            serverMsg = sb.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverMsg;
    }

}