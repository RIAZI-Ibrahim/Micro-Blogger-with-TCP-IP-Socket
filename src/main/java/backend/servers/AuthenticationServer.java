package backend.servers;

import backend.databaseservice.DbServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationServer {

    private static String name = null;
    private static String pass = null;

    public static void main(String[] args) {
        listen();
    }

    public static void listen() {
        int port = 2022;
        try (ServerSocket serverSocket = new ServerSocket(port, 100)){
            System.out.println("AuthenticationServer started listening on: localhost:" + port);
            System.out.println("-".repeat(80));
            while (true) {
                final Socket activeSocket = serverSocket.accept();
                new Thread(() -> receiveNewRequest(activeSocket)).start();
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    private static void receiveNewRequest(Socket socket) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String req = in.readLine();
            String msg;
            if (!parseReq(req)) msg = "ERROR! wrong format";
            else if (!DbServices.checkPass(name, pass)) msg = "ERROR! invalid credintials";
            else msg = "OK! token=YES:" + name;
            out.println(msg);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean parseReq(String req) {
        String regex = "LOGIN ([a-zA-Z]+\\w*):(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req);
        if (matcher.matches()) {
            name = matcher.group(1);
            pass = matcher.group(2);
            return true;
        }
        return false;
    }


}
