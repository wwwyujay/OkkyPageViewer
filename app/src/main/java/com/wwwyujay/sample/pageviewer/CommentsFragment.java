package com.wwwyujay.sample.pageviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class CommentsFragment extends DialogFragment implements View.OnClickListener {

    ArrayList<Comment> comments;
    ListView commentList;
    CommentAdapter adapter;

    ImageView spinner;
    Button mClose;
    RotateAnimation rotateAnimation;

    String link;

    public CommentsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Receive data from the activity */
        Bundle data = getArguments();
        if (data == null || data.isEmpty()) {
            getFragmentManager().popBackStack();    // Go back
        } else {
            link = data.getString("link");
        }

        /* Initialize a RotateAnimation object */
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        initFragment(view);

        /* Rotate the spinner image until the fetching comments finished */
        spinner.startAnimation(rotateAnimation);

        /* Fetch comments data */
        new FetchComments(comments, adapter).execute(Urls.BASE_URL, link);
        
        return view;
    }

    /**
     * Initialize layout: binding view references, setting up views, adapting listeners, etc.
     *
     * @param root Root view of the fragment.
     */
    private void initFragment(View root) {
        /* Create a Comment list and a ListView object */
        comments = new ArrayList<Comment>();
        commentList = (ListView)root.findViewById(R.id.comment_list);

        /* Bind an adapter with the ListView */
        adapter = new CommentAdapter(getContext(), R.layout.layout_comment_item, comments);
        commentList.setAdapter(adapter);

        /* Bind other views */
        spinner = (ImageView)root.findViewById(R.id.comment_spinner);
        mClose = (Button)root.findViewById(R.id.comment_close);
        mClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* Close button */
            case R.id.comment_close:
                spinner.clearAnimation();
                getFragmentManager().popBackStack();    // Get back (close this fragment)
                break;
        }
    }

    /**
     * Fetch a comment list from a given url.
     */
    private class FetchComments extends AsyncTask<Object, Void, Document> {

        ArrayList<Comment> list;
        CommentAdapter adapter;

        protected FetchComments(ArrayList<Comment> list, CommentAdapter adapter) {
            super();
            this.list=list;
            this.adapter = adapter;
        }

        @Override
        protected Document doInBackground(Object[] objects) {

            /* Compound url from the string parameters */
            String url = "";
            for (Object o: objects) {
                url += o.toString();   // board url + page number + query string etc.
            }

            /* Return a document object after connection to given url */
            Document doc = null;
            try {
                doc = Jsoup.connect(url)
                        .userAgent(Urls.USER_AGENT)
                        .ignoreHttpErrors(true)
                        .followRedirects(true)
                        .get();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return doc;
            }
        }

        @Override
        protected void onPostExecute(Document document) {

            if (document != null)
            {
                /* Select a list of comment elements from the document */
                Elements els = document.select("li.note-item div.panel-body");

                /* Create comment objects from extracted strings */
                for (Element e:els) {
                    String writer = e.select("a.nickname").text();
                    String createdAt = e.select("span.timeago").text();
                    String content = e.select("article.note-text").toString();

                    list.add(new Comment(writer, createdAt, content));
                }

                /* Update ListView */
                adapter.notifyDataSetChanged();
            }

            /* Hide the rotating image */
            spinner.clearAnimation();
            spinner.setVisibility(View.GONE);

            /* Set ListView visible */
            commentList.setVisibility(View.VISIBLE);
        }
    }
}
