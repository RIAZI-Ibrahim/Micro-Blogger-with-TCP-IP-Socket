package backend.handlers;

import backend.singltonDataHolder.DataHolder;
import backend.databaseservice.DbServices;
import backend.Utilities;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subscriber {
    public static void handle(String req, PrintWriter out) {
        String reqInfo = parseSubReq(req);
        if (reqInfo == null) {
            Utilities.sendErrorResponse(out, "Bad request format");
            return;
        }


        String[] ar = reqInfo.split("\\s+");
        String clientName = ar[0];
        String sub = ar[1];
        DataHolder.getInstance().addSubscription(clientName, sub);
        DbServices.addSub(clientName,sub);
        Utilities.sendOkResponse(out);
    }

    private static String parseSubReq(String req) {
        String sub = "";
        String regex = "SUBSCRIBE\\s+client:@(\\w+)\\s+(author:|tag:)(@|#)(\\w+)\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req);
        if (matcher.matches()) {
            sub =(matcher.group(1))+" "+ matcher.group(3) + matcher.group(4);
        }
        if(sub.isBlank()) return null;
        return sub;
    }
}
