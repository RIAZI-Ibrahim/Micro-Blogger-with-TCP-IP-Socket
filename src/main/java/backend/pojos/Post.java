package backend.pojos;

public class Post {
    private String msgid;
    private String content;
    private long date;
    private String author;
    private String originalAuthor;
    private String replyTo;


    public Post(String msgid, String content, long date, String user, String author, String replyTo) {
        this.msgid = msgid;
        this.content = content;
        this.date = date;
        this.author = user;
        this.originalAuthor = author;
        this.replyTo = replyTo;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public void setOriginalAuthor(String originalAuthor) {
        this.originalAuthor = originalAuthor;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Post{" +
                "msgid='" + msgid + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", author='" + author + '\'' +
                ", originalAuthor='" + originalAuthor + '\'' +
                ", replyTo='" + replyTo + '\'' +
                '}';
    }
}
