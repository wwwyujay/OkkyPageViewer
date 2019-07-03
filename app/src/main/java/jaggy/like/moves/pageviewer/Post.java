package jaggy.like.moves.pageviewer;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Post {

    private String number; // Number of the post
    private String title, writer, createdAt, category, board, views, likes, comments;
    private String content;

    public Post() { }

    /**
     * Post constructor for use in {@link PostFragment)
     *
     * @param title Title of the post
     * @param category Category of the post
     * @param writer Writer of the post
     * @param createdAt When the post created
     * @param content Contents of the post
     */
    public Post(String title, String category, String writer, String createdAt, String content) {
        this.title = title;
        this.category = category;
        this.writer = writer;
        this.createdAt = createdAt;
        this.content = content;
    }

    /**
     * Post constructor for use in {@link BoardFragment}
     *
     * @param number The post identifying number
     * @param title Title of the post
     * @param writer Writer of the post
     * @param createdAt When the post created
     * @param category Category of the post
     * @param board Board which the post is belong to
     * @param views The number of views
     * @param likes The number of likes
     * @param comments The number of comments
     */
    public Post(String number, String title, String writer, String createdAt, String category, String board, String views, String likes, String comments) {
        this.number = number;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
        this.category = category;
        this.board = board;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
    }

    /**
     * Create a Post object by extracting strings from the given Document.
     * The parsing process can be different depending on which site or page to parse.
     *
     * @param doc Document object to be parsed into a Post object
     * @return Post object
     */
    public static Post parseDocumentToPost(Document doc) {

        /* Parse the title part first */
        Element titlePart = doc.getElementsByClass("title").first();
        Post post = parseTitle(titlePart);  // Incomplete Post object

        /* Parse the main part of the post */
        String content = doc.getElementsByClass("content content-box post-content").first().toString();
        post.setContent(content);

        return post;
    }

    /**
     * Create a partial Post object by extracting strings from the given Element.
     * The parsing process can be different depending on which site or page to parse.
     * 
     * @param e An original Element object that will be parsed into a Post object
     * @return An incomplete Post object (main part is not parsed yet)
     */
    private static Post parseTitle(Element e) {

        /* When the given element is null */
        if (e == null) {
            return new Post("Title", "Category", "Writer", "Created-at", "Content");
        }

        /* Fields to store extracted strings */
        Element eCategory, eTitle, eWriter, eDate;
        String category = "", title = "", writer = "", createdAt = "";

        /* Extracting text */
        eCategory = e.getElementsByClass("category-text").first();
        eTitle = e.getElementsByTag("b").first();
        eWriter = e.getElementsByClass("text-right").first().getElementsByTag("b").first();
        eDate = e.getElementsByClass("text-right").first();

        /* Fill strings with extracted text */
        if (eCategory != null) category = eCategory.text();
        if (eTitle != null) title = eTitle.text().substring(category.length()).trim();
        if (eWriter != null) writer = eWriter.text();
        if (eDate != null) createdAt = eDate.text().substring(writer.length()+2).trim();

        return new Post(title, category, writer, createdAt, null);
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

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

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
