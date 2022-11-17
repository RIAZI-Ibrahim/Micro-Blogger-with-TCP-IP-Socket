package backend;

import backend.singltonDataHolder.DataHolder;
import org.java_websocket.WebSocket;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    public static String getId() {
        return UUID.randomUUID().toString();
    }

    public static long getTime() {
        return Instant.now().toEpochMilli();
    }

    public static void sendOkResponse(PrintWriter out) {
        out.println("OK");
        out.println("");
        out.println("||---#end#---||");
        out.flush();
    }

    public static void sendErrorResponse(PrintWriter out, String error) {
        out.println("ERROR");
        out.println(error);
        out.println("||---#end#---||");
        out.flush();
    }

    public static ArrayList<String> extractSubs(String cleintSubs) {
        ArrayList<String> list = new ArrayList<>();
        String post = "@Fahed #Algeria #France";
        String regex = "#(\\w+)|@(\\w+)";
        Matcher matcher = Pattern.compile(regex).matcher(cleintSubs);
        while (matcher.find()) {
            System.out.println(matcher.group());
            list.add(matcher.group());
        }
        return list;
    }

    public static void updateFlow(String userName, String post, String id, long timestamp, String author, String replyto) {

        List<String> subs = new ArrayList<>();
        subs.add("@" + userName);
        subs.addAll(Utilities.extractSubs(post));
        String metadata = joinMeta(userName, post, id, timestamp, author, replyto);
        List<String> subscribers;
        for (String sub : subs) {
            List<String> listOfSubscribers = DataHolder.getInstance().getSubscribers(sub);
            if (listOfSubscribers == null) continue;
            if (listOfSubscribers.size() < 1) continue;
            subscribers = DataHolder.getInstance().getSubscribers(sub);
            subscribers.forEach(s -> {
                WebSocket clientAddress = DataHolder.getInstance().getClientAddress(s);
                if (clientAddress != null)
                    clientAddress.send(metadata);
            });
        }
    }

    private static String joinMeta(String userName, String post, String id, long timestamp, String author, String replyto) {
        StringBuilder sb = new StringBuilder();
        sb.append("user: " + userName).append(", ");
        sb.append("content: " + post).append(", ");
        sb.append("id: " + id).append(", ");
        sb.append("time: " + timestamp).append(", ");
        sb.append("author: " + author).append(", ");
        sb.append("replyto: " + replyto).append(", ");
        sb.append("\b\b");

        return sb.toString();
    }
}
