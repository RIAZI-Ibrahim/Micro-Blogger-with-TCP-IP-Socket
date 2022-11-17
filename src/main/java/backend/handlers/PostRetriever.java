package backend.handlers;

import backend.databaseservice.DbServices;
import backend.Utilities;
import backend.pojos.Post;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostRetriever {


    // هذه الدالة تعمل داخل ثريد مستقل كي تتعامل مع طلبات العميل
    public static void handle(String req, PrintWriter out) {
            String id = extractPostId(req);
            if(id == null){
                Utilities.sendErrorResponse(out,"Bad request format");
                return;
            }

            Post post = DbServices.retrievePost(id);
            if(post != null) {
                out.println("MSG");
                out.println(post);
                out.println("||---#end#---||");
                out.flush();
            }else{
                Utilities.sendErrorResponse(out,"Unknown message id");
            }
    }

private static String extractPostId(String req){
    String id = "";
    String idRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    String regex = "RCV_MSG msg_id:("+idRegex+")\n";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(req);
    if (matcher.matches()) {
        id = matcher.group(1);
    }
    if(id.isBlank()) return null;

        return id;
}

}