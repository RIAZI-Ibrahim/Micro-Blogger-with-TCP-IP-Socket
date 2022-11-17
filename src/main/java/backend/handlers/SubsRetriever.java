package backend.handlers;

import backend.databaseservice.DbServices;

import java.io.PrintWriter;

public class SubsRetriever {
    public static void handle(String req, PrintWriter out) {
        String userName = req.split("\\s+")[1];
        String response =  DbServices.retrieveSubs(userName);
        out.println(response);
        out.println("||---#end#---||");
        out.flush();

    }

}
