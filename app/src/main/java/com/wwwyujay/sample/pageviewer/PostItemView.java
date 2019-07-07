package com.wwwyujay.sample.pageviewer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostItemView extends LinearLayout implements View.OnClickListener {

    private Context context;
    private TextView board, category, title, writer, createdAt, views, likes, comments;
    private String link, number; // *link: url to the specific post page, *number: post identifier (not used)

    public PostItemView(Context context, Post post) {
        super(context);
        this.context = context;
        initialize();
        setPost(post);
    }

    private void initialize() {
        /* Inflate the root view */
        inflate(getContext(), R.layout.layout_post_item, this);

        /* Bind view references */
        board = (TextView)findViewById(R.id.post_list_board);
        title = (TextView)findViewById(R.id.post_list_title);
        writer = (TextView)findViewById(R.id.post_list_writer);
        createdAt = (TextView)findViewById(R.id.post_list_created_at);
        views = (TextView)findViewById(R.id.post_list_count_views);
        likes = (TextView)findViewById(R.id.post_list_count_likes);
        comments = (TextView)findViewById(R.id.post_list_count_comments);

        setOnClickListener(this);
    }

    /*
    * Replace existing BoardFragment to PostFragment
    * */
    @Override
    public void onClick(View v) {
        /* Prepare to create and replace a fragment */
        FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        PostFragment postFragment = new PostFragment();

        /* Attach data to the fragment */
        Bundle data = new Bundle();
        data.putString("link", getLink());  // Url of the post
        data.putString("number", getNumber());  // Identifier of the post (not used)
        postFragment.setArguments(data);

        /* Commit replacement */
        transaction.replace(R.id.fragment_placeholder, postFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* Set values of the view at once with a Post object */
    public void setPost(Post post) {
        number = post.getNumber(); // not used
        setBoard(post.getBoard());
        setTitle(post.getTitle());
        setWriter(post.getWriter());
        setCreatedAt(post.getCreatedAt());
        setViews(post.getViews());
        setLikes(post.getLikes());
        setComments(post.getComments());
    }

    /**
     * Getters and setters
     * Getters: get values from the views
     * Setters: set values of the views
     * */
    public String getNumber() { return number; }

    public String getBoard() {
        return board.getText().toString();
    }

    public void setBoard(String board) {
        if (board.isEmpty() || board == null) {
            this.board.setVisibility(View.GONE);
        } else {
            this.board.setText(board);
        }
    }

    public String getCategory() {
        return category.getText().toString();
    }

    public void setCategory(String category) {
        if (category.isEmpty() || category == null) {
            this.category.setVisibility(View.GONE);

        } else {
            this.category.setText(category);    // when the post has board (board: O)
        }
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getWriter() {
        return writer.getText().toString();
    }

    public void setWriter(String writer) {
        this.writer.setText(writer);
    }

    public String getCreatedAt() {
        return createdAt.getText().toString();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.setText(createdAt);
    }

    public String getViews() {
        return views.getText().toString();
    }

    public void setViews(String views) {
        this.views.setText(views);
    }

    public String getLikes() {
        return likes.getText().toString();
    }

    public void setLikes(String likes) {
        this.likes.setText(likes);
    }

    public String getComments() {
        return comments.getText().toString();
    }

    public void setComments(String comments) {
        this.comments.setText(comments);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
