package backend.handlers;

import backend.Utilities;

import java.io.PrintWriter;

public class Processor {

    public static void processRequest(String req, PrintWriter out) {
        switch (req.split("\\s+")[0]) {
            case "SUBS":
                SubsRetriever.handle(req, out);
                break;
            case "PUBLISH":
                Publisher.handle(req, out);
                break;
            case "RCV_IDS":
                IdsRetriever.handle(req, out);
                break;
            case "RCV_MSG":
                PostRetriever.handle(req, out);
                break;
            case "REPLY":
                Replier.handle(req, out);
                break;
            case "REPUBLISH":
                Republisher.handle(req, out);
                break;
            case "CONNECT":
                Connector.handle(req, out);
                break;
            case "SUBSCRIBE":
                Subscriber.handle(req, out);
                break;
            case "UNSUBSCRIBE":
                Unsubscriber.handle(req, out);
                break;
            default:
                Utilities.sendErrorResponse(out, "Bad request format");
        }
    }
}
