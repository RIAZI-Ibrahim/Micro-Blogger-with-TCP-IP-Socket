package frontend.gui;

import backend.pojos.Post;
import frontend.client.*;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondaryController implements Initializable {

    private static WebSocketClient client = null;
    private ListProperty<Post> listProperty = new SimpleListProperty<>();
    private ListProperty<String> subProperty = new SimpleListProperty<>();
    private boolean toggleSwitchState = false;
    private String userName;


    @FXML
    private Label count;
    @FXML
    private TextArea textArea;
    @FXML
    private Text serverMsg;
    @FXML
    private ListView<Post> postsList;
    @FXML
    private ListView<String> subList;
    @FXML
    private TextField postRequestFiled;
    @FXML
    private TextField flowRequestFiled;
    @FXML
    private ToggleButton toggleBtn;
    @FXML
    private Button flowSubBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        //get username
        userName = ControllerDataHolder.getInstance().getUserName();


        //get subs
        Platform.runLater(()-> {
                    String[] subs = GetSubsClient.connect(userName).split(",");
                    for (String sub : subs){
                        if(sub.trim().equals("null")|| sub.isBlank())continue;
                        subProperty.add(sub);

                    }
            subList.refresh();

        });






        //prepare posts list and sublist
        postsList.itemsProperty().bind(listProperty);
        subList.itemsProperty().bind(subProperty);
        listProperty.set(FXCollections.observableArrayList());
        subProperty.set(FXCollections.observableArrayList());
        postsList.setCellFactory(param -> cellFactory());
        subList.setCellFactory(param -> subCellFactory());


        // bind text area with letters count
        textArea.lengthProperty().addListener(e -> {
            if (textArea.getLength() > 256) {
                textArea.setStyle("-fx-border-color: red");
                count.setStyle("-fx-text-fill: red");
            } else {
                textArea.setStyle("-fx-border-color:  #47477e");
                count.setStyle("-fx-text-fill: black");
            }
            count.setText(256 - textArea.getLength() + "");
        });

        //bind ListView with the window width property
        Platform.runLater(() -> {
            if (postsList.getScene() != null)
                (postsList.getScene().getWindow()).widthProperty().addListener((obs, oldVal, newVal) -> {
                    postsList.setCellFactory(param -> cellFactory());
                    postsList.refresh();
                    subList.setCellFactory(param -> subCellFactory());
                    subList.refresh();
                });
        });
    }

    public void publishPost(ActionEvent actionEvent) {
        String post = textArea.getText();
        if (textArea.getLength() > 256) return;
        String userName = ControllerDataHolder.getInstance().getUserName();
        String serverReply = PublishClient.connect(userName, post);
        serverMsg.setText(serverReply);

    }


    private ListCell<Post> cellFactory() {
        return new ListCell<>() {
            @Override
            public void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (!(empty || item == null)) {
                    Label user = new Label((item.getAuthor()));
                    user.getStyleClass().add("userName");
                    Label content = new Label(item.getContent());
                    content.getStyleClass().add("postContent");
                    Text date = new Text(new Date(item.getDate()).toString());
                    date.getStyleClass().add("postDate");
                    VBox postContainer = new VBox(user, content, date);
                    postContainer.getStyleClass().add("postContainer");
                    postContainer.setPrefWidth(postRequestFiled.getWidth() - 10);
                    postContainer.setMinWidth(postRequestFiled.getWidth() - 10);
                    content.setWrapText(true);
                    content.setTextAlignment(TextAlignment.JUSTIFY);
                    setGraphic(postContainer);
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
    }

    private ListCell<String> subCellFactory() {
        return new ListCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!(empty || item == null)) {
                    Label sub = new Label(item);
                    sub.setPrefWidth(subList.getWidth() - 10);

                    Label close = new Label("X");
                    close.setOnMouseClicked((e) -> {
                        System.out.println("ouch");
                        subProperty.remove(item);
                        UnsubscribeClient.connect(userName,item);
                    });
                    HBox subContainer = new HBox(sub, close);
                    subContainer.setPrefWidth(postRequestFiled.getWidth() - 10);
                    subContainer.setMinWidth(postRequestFiled.getWidth() - 10);
                    setGraphic(subContainer);
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
    }

    public void requestPost() {
        listProperty.clear();
        postsList.refresh();
        String content = postRequestFiled.getText();
        String user = "";
        String tag = "";
        int limit = 0;
        String regex = "@(?<user>\\w+)" +
                "|#(?<tag>\\w+)" +
                "|limit:(?<limit>\\d\\d*)";
        Matcher matcher = Pattern.compile(regex).matcher(content);
        while (matcher.find()) {
            if (matcher.group("user") != null)
                user = matcher.group("user");
            if (matcher.group("tag") != null)
                tag = matcher.group("tag");
            if (matcher.group("limit") != null)
                limit = Integer.parseInt(matcher.group("limit"));
        }

        String res = RetriveIdsClient.retrieve(user, tag, "", limit);
        String[] ids = res.split(", ");
        for (String id : ids) {
            String post = RetrievePostClient.retrieve(id);
            if (post == null || post.isBlank()) continue;
            listProperty.add(extractPost(post));
            postsList.refresh();

        }
    }

    private Post extractPost(String stringObj) {
        if (stringObj.trim().startsWith("ERROR")) return null;
        System.out.println("I AM HERE:\t" + stringObj);

        String regex = "(?s)msgid='(?<id>[0-9a-f-]+)" +
                "|content='(?<content>.+?)'" +
                "|date=(?<date>\\d+)" +
                "|author='(?<user>\\w+)" +
                "|originalAuthor='(?<author>\\w+)" +
                "|replyTo='(?<replyto>\\w+)";

        Matcher matcher = Pattern.compile(regex).matcher(stringObj);
        String id = "";
        String content = "";
        long date = 0;
        String user = "";
        String author = "";
        String replyto = "";
        while (matcher.find()) {
            if (matcher.group("id") != null)
                id = matcher.group("id");
            if (matcher.group("content") != null)
                content = matcher.group("content");
            if (matcher.group("date") != null)
                date = Long.parseLong(matcher.group("date"));
            if (matcher.group("user") != null)
                user = matcher.group("user");
            if (matcher.group("author") != null)
                author = matcher.group("author");
            if (matcher.group("replyto") != null)
                replyto = matcher.group("replyto");

            System.out.println("CONTENT: " + id);
        }

        return new Post(id, content, date, user, author, replyto);
    }

    public void flowRequest(ActionEvent e) {
        String tag = flowRequestFiled.getText();
        if(!tag.trim().matches("(#|@)\\w+")){
            serverMsg.setText("subscriptions must start with @ or # with no whitespace");
            return;
        }

        subProperty.add(tag);
        flowRequestFiled.clear();
        subList.refresh();

        System.out.println(SubscribeClient.connect(userName,tag));


//        Button btn =(Button) e.getSource();
//        String userName = ControllerDataHolder.getInstance().getUserName();
//        if(btn.getText().equals("SUBSCRIBE")){
//            SubscribeClient.connect(userName, flowRequestFiled.getText());
//        }
//        else {
//            UnsubscribeClient.connect(userName, flowRequestFiled2.getText());
//
//        }
    }


//    public void toggleFlow() {
//
//
//        if (toggleBtn.isSelected()) {
//            System.out.println("here");
//            openConnection();
//        } else {
//            stopConnection();
//        }
//
//
//    }

    public void toggleFlow() {
        boolean currentState = toggleBtn.isSelected();
        if (currentState != toggleSwitchState) {
            toggleSwitchState = currentState;
            if (currentState){
                openConnection();
                toggleBtn.setText("ONLINE");
            }
            else{
                stopConnection();
            }
        }
    }


    public void stopConnection() {

        client.close();

    }

    public void openConnection() {

        System.out.println(ConnectClient.connect(userName));

        Platform.runLater(() -> {
            client = null;
            try {
                client = new FlowClient(new URI("ws://localhost:9191"), listProperty, postsList,toggleBtn);
            } catch (URISyntaxException e) {
                toggleBtn.setText("OFFLINE");
                toggleBtn.setSelected(false);
                e.printStackTrace();
            }
            client.connect();
        });

    }

    public void clearList(ActionEvent actionEvent) {
        listProperty.clear();
    }

//
//    public void connectBtnState(boolean state){
//        if(state){
//            toggleBtn.setSelected(true);
//            toggleBtn.setText("ONLINE");
//        }else{
//            toggleBtn.setSelected(false);
//            toggleBtn.setText("OFFLINE");
//        }
//    }
}