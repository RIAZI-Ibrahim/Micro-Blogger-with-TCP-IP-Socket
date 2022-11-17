package backend.handlers;

import backend.databaseservice.DbServices;
import backend.Utilities;
import backend.pojos.Request;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdsRetriever {

    // هذه الدالة تعمل داخل ثريد مستقل كي تتعامل مع طلبات العميل
    public static void handle(String req, PrintWriter out) {
        Request requestObject = new Request(req);
        if (validate(requestObject)) {
            String response = "MSG_IDS\n" + DbServices.retrieveIds(requestObject);
            out.println(response);
            out.println("||---#end#---||");
            out.flush();
        } else {
            Utilities.sendErrorResponse(out, "Bad request format");
        }

    }

    private static Boolean validate(Request req) {
        String idRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        String header = "";
        String user = "";
        String tag = "";
        String id = "";
        String limit = "";
        String regex = "^(?<header>RCV_IDS)|author:@(?<user>\\w+)|tag:#(?<tag>\\w+)|since_id:(?<id>" + idRegex + ")|limit:(?<limit>\\d\\d*)";
        Matcher matcher = Pattern.compile(regex).matcher(req.getRequestString());
        while (matcher.find()) {
            if (matcher.group("header") != null)
                header = matcher.group("header");
            if (matcher.group("user") != null)
                user = matcher.group("user");
            if (matcher.group("tag") != null)
                tag = matcher.group("tag");
            if (matcher.group("id") != null)
                id = matcher.group("id");
            if (matcher.group("limit") != null)
                limit = matcher.group("limit");
        }

        if (!header.isBlank() && !(id + user + tag + limit).isBlank()) {
            if (!user.isBlank())
                req.setAuthor(user);
            if (!id.isBlank())
                req.setSince_id(Long.parseLong(id));
            if (!limit.isBlank())
                req.setLimit(Integer.parseInt(limit));
            if (!tag.isBlank())
                req.setTag(tag);
            if (!header.isBlank())
                req.setHeader(header);
            return true;
        } else return false;

    }

}
