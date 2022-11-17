package backend.handlers;

import backend.databaseservice.DbServices;
import backend.Utilities;
import javafx.util.Pair;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Publisher {
    public static void handle(String req, PrintWriter out) {
        Pair<String, String> reqInfo = parsePublishReq(req);
        if (reqInfo != null) {
            String id = Utilities.getId();
            long timeStamp = Utilities.getTime();
            Utilities.updateFlow(reqInfo.getKey(),reqInfo.getValue(), id, timeStamp,"","");
            DbServices.addMessage(id, reqInfo.getValue(), timeStamp, reqInfo.getKey(),"","");
            Utilities.sendOkResponse(out);
        } else {
            Utilities.sendErrorResponse(out, "Bad request format");
        }

    }

    private static Pair<String, String> parsePublishReq(String req) {
        String userName = "";
        String post = "";
        String regex = "PUBLISH author:@(\\w+)\n(.*)\n";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req);
        if (matcher.matches()) {
            userName = matcher.group(1);
            post = matcher.group(2);
        }
        if (userName.isBlank() || post.isBlank()) return null;
        return new Pair<>(userName, post);
    }


}
