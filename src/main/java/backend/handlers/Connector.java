package backend.handlers;

import backend.singltonDataHolder.DataHolder;
import backend.Utilities;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connector {

    // هذه الدالة تعمل داخل ثريد مستقل كي تتعامل مع طلبات العميل
    public static void handle(String req, PrintWriter out) {
        String userName = parseConnectReq(req);
        if (userName == null) {
            Utilities.sendErrorResponse(out, "bad request format");
            return;
        }

        if (!DataHolder.getInstance().clientExists(userName)) {
            DataHolder.getInstance().addConnectedClient(userName);
            Utilities.sendOkResponse(out);
        } else {
            Utilities.sendErrorResponse(out, "Client is already connected");
        }
    }

    private static String parseConnectReq(String req) {
        String userName = "";
        String regex = "CONNECT\\s+user:@(\\w+)\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req);
        if (matcher.matches()) {
            userName = matcher.group(1);
        }
        if (userName.isBlank()) return null;
        return userName;
    }
}
