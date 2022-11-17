package backend.pojos;

public class Request {
    private String requestString;
    private String header;
    private String Type;
    private String author;
    private String tag;
    private long since_id;
    private int limit;
    private String post;

    public Request(String requestString) {
        this.requestString = requestString;
        header = "";
        Type = "";
        author = "";
        tag = "";
        since_id = 0L;
        limit = 0;
        post = "";

    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getSince_id() {
        return since_id;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestString='" + requestString + '\'' +
                ", header='" + header + '\'' +
                ", Type='" + Type + '\'' +
                ", author='" + author + '\'' +
                ", tag='" + tag + '\'' +
                ", since_id=" + since_id +
                ", limit=" + limit +
                ", post='" + post + '\'' +
                '}';
    }
}
