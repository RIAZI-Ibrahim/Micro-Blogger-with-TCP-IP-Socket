package frontend.client;

import backend.pojos.Post;
import frontend.gui.ControllerDataHolder;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlowClient extends WebSocketClient {
    private ListProperty<Post> flowProperty = null;
    private ListView<Post> flowList = null;
    private ToggleButton toggleBtn = null;

    //main for testing only
    public static void main(String[] args) throws URISyntaxException {

    }
    public FlowClient(URI serverURI, ListProperty<Post> listProperty, ListView<Post> listView, ToggleButton toggleBtn) {
        super(serverURI);
        this.flowProperty = listProperty;
        this.flowList = listView;
        this.toggleBtn = toggleBtn;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        notifyAll();

    }

    public void sendMessage(String message) {
        System.out.println("SENDING:\t" + message);
        send(message);
    }

    @Override
    public void onMessage(String message) {
        if(message.trim().equals("What is your name?")){
            sendMessage("name: "+ControllerDataHolder.getInstance().getUserName());
        }else {
            String[] msg = message.split(", ");
            if (msg.length < 4) return;
            Post post = parseResponst(message);
            Platform.runLater(() -> {
                flowProperty.add(post);
                flowList.refresh();
            });
        }

    }



    @Override
    public void onClose(int code, String reason, boolean remote) {
        Platform.runLater(() -> {
            toggleBtn.setText("OFFLINE");
            toggleBtn.setSelected(false);
        });
        System.out.println("تم انهاء الاتصال " + "\n" + code + "\n" + reason + "\n");
    }

    @Override
    public void onError(Exception ex) {

        if (!ex.getMessage().contains("current thread is not owner"))
        System.out.println("حدث خطأ في الاتصال:" + ex + "\n");
    }

    private static Post parseResponst(String res) {
        String user = "";
        String content = "";
        String id = "";
        long time = 0;
        String author = "";
        String replyto = "";

        String idRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

        String regex = "user:\\s+(\\w+)|content:\\s+(.+?),(?=\\s+id:)|id:\\s+("+idRegex+")|time:\\s+(\\d+)|author:\\s+(\\w*)|replyto:\\s+(\\w*)";

        Matcher matcher = Pattern.compile(regex).matcher(res);
        while (matcher.find()) {
            if (matcher.group(1) != null)
                user = matcher.group(1);
            if (matcher.group(2) != null)
                content = matcher.group(2);
            if (matcher.group(3) != null)
                id = matcher.group(3);
            if (matcher.group(4) != null)
                time = Long.parseLong(matcher.group(4));
            if (matcher.group(5) != null)
                author = matcher.group(5);
            if (matcher.group(6) != null)
                replyto = matcher.group(6);
        }

        return new Post(id, content, time, user, author, replyto);

    }
}
