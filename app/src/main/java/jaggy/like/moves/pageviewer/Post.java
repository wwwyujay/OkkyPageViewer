package jaggy.like.moves.pageviewer;

public class Post {

    private String number; // Number of the post
    private String title, writer, createdAt, board, views, likes, comments;
    private String content;

    public Post() { }
    
    /**
     * Post constructor for use in {@link BoardFragment}
     *
     * @param number The post identifying number
     * @param title Title of the post
     * @param writer Writer of the post
     * @param createdAt When the post created
     * @param board Board which the post is belong to
     * @param views The number of views
     * @param likes The number of likes
     * @param comments The number of comments
     */
    public Post(String number, String title, String writer, String createdAt, String board, String views, String likes, String comments) {
        this.number = number;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
        this.board = board;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
    }
    
    /* Basic getters and setters */
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
