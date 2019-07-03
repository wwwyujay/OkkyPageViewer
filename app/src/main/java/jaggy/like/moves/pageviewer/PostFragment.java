package jaggy.like.moves.pageviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PostFragment extends Fragment implements View.OnClickListener {

    String link;    // Link of the selected Post
    TextView category, title, writer, createdAt, content, commentCount;
    View showComments; // Works as a button to show a Comment dialog

    Post post;

    public PostFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Receive data from the activity (former fragment) */
        Bundle data = getArguments();
        if (data == null || data.isEmpty()) {
            getFragmentManager().popBackStack();    // Go back
        } else {
            link = data.getString("link");
        }

        /* Switch navigation button to back button */
        ((MainActivity)getActivity()).showBackButton(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /* When destroy this fragment, switch back button to navigation button */
        ((MainActivity)getActivity()).showBackButton(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* Inflate a root view and initialise the fragment */
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initFragment(view);

        /* Fetch a post with given url */
        new FetchPost(post).execute(Urls.BASE_URL_MOBILE, link);

        return view;
    }

    /**
     * Initialize layout: binding view references, setting up views, adapting listeners, etc.
     *
     * @param root Root view of the fragment.
     */
    private void initFragment(View root) {
        category = (TextView)root.findViewById(R.id.post_category);
        title = (TextView)root.findViewById(R.id.post_title);
        writer = (TextView)root.findViewById(R.id.post_writer);
        createdAt = (TextView)root.findViewById(R.id.post_created_at);
        commentCount = (TextView)root.findViewById(R.id.comments_count);

        showComments = root.findViewById(R.id.post_show_comments);
        showComments.setOnClickListener(this);

        content = (TextView)root.findViewById(R.id.post_detail);
        content.setMovementMethod(new LinkMovementMethod());
        content.setLinksClickable(true);
        content.setTextIsSelectable(true);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* Comment button */
            case R.id.post_show_comments:
                if (commentCount.getText().equals("[0]")) {
                    Toast.makeText(getContext(), "댓글이 없습니다.", Toast.LENGTH_SHORT).show();
                    break;    // Don't show the comments dialog if there is 0 comments
                }

                /* Prepare to show a FragmentDialog */
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);

                /* Create a CommentFragment, attach data, and show the dialog */
                CommentsFragment fragment = new CommentsFragment();
                Bundle data = new Bundle();
                data.putString("link", link);
                fragment.setArguments(data);
                fragment.show(transaction, "dialog");

                break;
            /* Refresh button */
            case R.id.toolbar_refresh:
                /* Rotate the refresh button image */
                Animation refreshAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_360);
                v.startAnimation(refreshAnimation);

                /* Reload current page */
                new FetchPost(post).execute(Urls.BASE_URL_MOBILE, link);
                break;
        }
    }

    /**
     * Fetch a Post from the given url.
     */
    private class FetchPost extends AsyncTask<Object, Void, Document> {

        Post post;
        private LoadingFragment fragment;

        protected FetchPost(Post post) {
            super();
            this.post=post;
        }

        @Override
        protected void onPreExecute() {

            /* Show loading dialog until fetching finished */
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);

            fragment = new LoadingFragment();
            fragment.setCancelable(false);
            fragment.show(transaction, "dialog");
        }

        @Override
        protected Document doInBackground(Object[] objects) {

            /* Compound url from the string parameters */
            String url = "";
            for (Object o: objects) {
                url += o.toString();
            }

            /* Return a document object after connected to given url */
            Document doc = null;
            try {
                doc = Jsoup.connect(url)
                        .userAgent(Urls.USER_AGENT) // added
                        .ignoreHttpErrors(true) // added
                        .followRedirects(true) // added
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
                /* Set comment count TextView */
                int count = document.getElementsByClass("commentEl").size();
                commentCount.setText("["+count+"]");

                /* Get a Post object from the document */
                post = Post.parseDocumentToPost(document);

                /* Update views */
                title.setText(post.getTitle());
                writer.setText(post.getWriter());
                createdAt.setText(post.getCreatedAt());

                if (post.getCategory() != null && !post.getCategory().isEmpty()) {
                    category.setText(post.getCategory());
                    category.setVisibility(View.VISIBLE);
                }

                /* Update content view (with a ImageGetter) */
                content.setText(Html.fromHtml(post.getContent(), new PicassoImageGetter(content), null));
            }

            /* Dismiss the process dialog */
            fragment.dismissAllowingStateLoss();
        }
    }
}
