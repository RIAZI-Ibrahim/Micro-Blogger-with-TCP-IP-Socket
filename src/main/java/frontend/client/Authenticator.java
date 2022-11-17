package frontend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Authenticator {

    public static String getToken(String username, String pass) {

        String response = null;
        try (Socket socket = new Socket("localhost", 2022);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("LOGIN "+username+":"+pass);
            out.flush();

            response = in.readLine();//رسالة السيرفر


        }catch (IOException e){e.printStackTrace();}
        return response;
    }

    public static void main(String[] args) {
        System.out.println(getToken("oqab","123"));
    }
}
