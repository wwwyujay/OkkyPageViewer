package com.wwwyujay.sample.pageviewer;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private Context context;
    private int resource;
    private ArrayList<Comment> comments;
    
    public CommentAdapter(Context context, int resource, ArrayList<Comment> comments) {
        super(context, resource, comments);
        this.context = context;
        this.resource = resource;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommentHolder holder;

        /* Inflate Comment item view */
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new CommentHolder();
            holder.commentWriter = (TextView)convertView.findViewById(R.id.comment_writer);
            holder.commentCreatedAt = (TextView)convertView.findViewById(R.id.comment_created_at);
            holder.commentDetail = (TextView)convertView.findViewById(R.id.comment_detail);

            convertView.setTag(holder);
        }
        else
        {
            holder = (CommentHolder)convertView.getTag();
        }

        /* Bind control */
        Comment comment = comments.get(position);
        holder.commentWriter.setText(comment.getWriter());
        holder.commentCreatedAt.setText(comment.getCreatedAt().toString());
        //holder.commentDetail.setText(Html.fromHtml(comment.getDetail()));
        holder.commentDetail.setText(
                Html.fromHtml(
                        comment.getDetail(),
                        new PicassoImageGetter(holder.commentDetail),
                        null));

        return convertView;
    }

    static class CommentHolder {

        TextView commentWriter;
        TextView commentCreatedAt;
        TextView commentDetail;
    }
}
