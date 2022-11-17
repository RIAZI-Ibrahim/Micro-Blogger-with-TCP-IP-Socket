package frontend.client;

import backend.Utilities;
import backend.databaseservice.DbServices;
import backend.singltonDataHolder.DataHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UnsubscribeClient {
    public static void main(String[] args) throws IOException {
        connect("userNew", "#tagNew");
    }

    public static String connect(String user,String tag) {

        String sub = "";
        if(tag.startsWith("@")){
            sub = "author:"+tag;
        }else if(tag.startsWith("#")){
            sub = "tag:"+tag;
        }else{
            return "Wrong tag. It must starts with # or @";
        }
        String serverMsg = null;
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected");
            out.println("UNSUBSCRIBE client:@"+user+" "+sub);
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
