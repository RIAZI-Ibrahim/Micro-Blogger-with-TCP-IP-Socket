package backend.handlers;

import backend.databaseservice.DbServices;
import backend.Utilities;
import backend.pojos.Post;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Republisher {
    public static void handle(String req, PrintWriter out) {
        String result = parseRepublishReq(req);
        if (result == null) {
            Utilities.sendErrorResponse(out, "bad request format");
            return;
        }

        String[] ar = result.split(", ");
        Post post = DbServices.retrievePost(ar[1]);
        String id = Utilities.getId();
        long timeStamp = Utilities.getTime();
        Utilities.updateFlow(ar[0],post.getContent(), id, timeStamp,post.getAuthor(),"");

        DbServices.addMessage(id, post.getContent(), timeStamp, ar[0],post.getAuthor(),"");
        Utilities.sendOkResponse(out);

    }

    private static String parseRepublishReq(String req) {
        //REPUBLISH author:@user msg_id:id
        String userName = "";
        String id = "";
        String idRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        String regex = "REPUBLISH\\s+author:@(\\w+)\\s+msg_id:(" + idRegex + ")\\s*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req);
        if (matcher.matches()) {
            userName = matcher.group(1);
            id = matcher.group(2);
        }
        if (userName.isBlank() || id.isBlank() ) return null;
        return String.join(", ", userName, id);

    }
}
