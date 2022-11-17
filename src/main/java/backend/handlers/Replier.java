package backend.handlers;

import backend.databaseservice.DbServices;
import backend.Utilities;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replier {
    public static void handle(String req, PrintWriter out) {
//
        String reqInfo = parseReplyReq(req);
        String[] ar = parseReplyReq(req).split(", ");
        if (reqInfo != null) {
            if (DbServices.checkPostExists(ar[1])) {
                String id = Utilities.getId();
                long timeStamp = Utilities.getTime();
                Utilities.updateFlow(ar[0],ar[2], id, timeStamp,"",ar[1]);
                DbServices.addMessage(id, ar[2], timeStamp, ar[0], "", ar[1]);
                Utilities.sendOkResponse(out);
            } else {
                Utilities.sendErrorResponse(out, "messge doesn't exist");
            }
        } else {
            Utilities.sendErrorResponse(out, "Bad request format");
        }

    }


    private static String parseReplyReq(String req) {
        String userName = "";
        String replyto = "";
        String post = "";
        String idRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        String regex = "REPLY\\s+author:@(\\w+)\\s+reply_to_id:(" + idRegex + ")\n(.*)\\s*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req);
        if (matcher.matches()) {
            userName = matcher.group(1);
            replyto = matcher.group(2);
            post = matcher.group(3);
        }
        if (userName.isBlank() || post.isBlank() || replyto.isBlank()) return null;
        return String.join(", ", userName, replyto, post);
    }


}
