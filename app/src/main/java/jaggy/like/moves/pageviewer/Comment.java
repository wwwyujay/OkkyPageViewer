package jaggy.like.moves.pageviewer;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Comment {
    
    private String writer;   // Writer of the comment
    private String replyTo; // Writer of the parent comment
    private String createdAt;   // When the comment was created
    private String detail;  // Content of the comment
    private int level;  // How deep is the comment (If the comment is a reply to another comment, then the level of the comments get 1 point higher.)

    public Comment(String writer, String createdAt, String detail, int level) {
        this.writer = writer;
        this.createdAt = createdAt;
        this.detail = detail;
        this.level = level;
    }

    public Comment(String writer, String replyTo, String createdAt, String detail) {
        this.writer = writer;
        this.replyTo = replyTo;
        this.createdAt = createdAt;
        this.detail = detail;
    }
    
    /**
     * Parse an Element object to a Comment object. The comments are separated by level.
     * The parsing process depends on the HTML structure of the original page.
     *
     * @param e An Element object to be parsed into Comment object.
     * @param level Level of the comment.
     * @return  A Comment object parsed from an element.
     */
    public static Comment parseElementToComment(Element e, int level) {
        Comment comment = null;
        if (e!=null) {
            String writer = e.getElementsByClass("writer").first().text();
            String createdAt = e.getElementsByClass("created-at").first().text();
            String content = e.getElementsByClass("comment-body").first().text();

            comment = new Comment(writer, createdAt, content, level);
        }
        return comment;
    }

    /**
     * Parse an Element object to a Comment object. The comments are differentiated by parent writer, not level.
     * The parsing process depends on the HTML structure of the original page.
     *
     * @param e Comment An Element object to be parsed into Comment object.
     * @param parent The writer of the parent comment.
     * @return A Comment object parsed from an element.
     */
    public static Comment parseElementToComment(Element e, String parent) {
        Comment comment = null;
        if (e!=null) {
            String writer = e.getElementsByClass("writer").first().text();
            String createdAt = e.getElementsByClass("created-at").first().text();
            String content = e.getElementsByClass("comment-body").first().text();

            comment = new Comment(writer, parent, createdAt, content);
        }
        return comment;
    }

    /**
     * Divide elements by comments and replies and store them into a Comment list. (Recursive Call)
     * Replies are same as a group of comments.
     *
     * @param list An ArrayList which will store Comment objects.
     * @param elements Original group of elements that has comment and replies.
     * @param level The level of comment. Increase 1 value per recursive call.
     */
    public static void divideElements(ArrayList<Comment> list, Elements elements, int level) {

        for (Element e : elements) {

            if (e.hasClass("replies")) {
                /* Divide replies into comments. Put replies as a parameter 'Elements' */
                divideElements(list, e.children(), level+1);
            }
            else {
                Comment c = Comment.parseElementToComment(e, level);
                list.add(c);
            }
        }
    }

    /**
     * Divide elements by comments and replies and store them into a Comment list. (Recursive Call)
     * Replies are same as a group of comments.
     *
     * @param list An ArrayList which will store Comment objects.
     * @param elements Original group of elements that has comment and replies.
     * @param parent The writer of the parent (upper) comment.
     */
    public static void divideElements(ArrayList<Comment> list, Elements elements, String parent) {

        for (Element e : elements) {

            if (e.hasClass("replies")) {
                /* Divide replies into comments. Put replies as a parameter 'Elements' */
                divideElements(list, e.children(), parent);
            }
            else {
                Comment c = Comment.parseElementToComment(e, parent);
                parent = c.getWriter();
                list.add(c);
            }
        }
    }

    /* Getters and Setters */
    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
