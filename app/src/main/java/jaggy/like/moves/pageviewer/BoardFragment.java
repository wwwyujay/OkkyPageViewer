package jaggy.like.moves.pageviewer;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BoardFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private int itemId;         // Id of the selected menu item
    private String boardName;   // Current board name (same as menu title)
    private String boardUrl;    // Current board url
    
    private ScrollView scrollView;
    private LinearLayout layoutBoard;
    private TextView toolbarTitle;
    private ImageView toolbarRefresh;

    private Integer lastPage, currentPage = 1;
    private EditText inputPage;
    private ImageView goToFirst, goToLast, goPrevious, goNext;  // page transition button

    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Receive data from the activity (navigation drawer) */
        Bundle data = getArguments();
        if (data != null && !data.isEmpty()) {
            itemId = data.getInt("itemId");
            boardName = data.getString("boardName");

        } else {
            itemId = R.id.board_latest;
            boardName = getString(R.string.board_latest);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* Inflate a root view and initialise the fragment */
        ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_board, container, false);
        initFragment(view);
        
        /* Change fragment attributes by item id */
        switch (itemId) {
            case R.id.board_notice: // 공지사항
                boardUrl = Urls.BOARD_NOTICE;
                break;
            case R.id.board_dding:  // 띵언게시판
                boardUrl = Urls.BOARD_DDING;
                break;
            case R.id.board_womstory:   // 웜역사관
                boardUrl = Urls.BOARD_WOMSTORY;
                new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, Urls.BOARD_WOMSTORY);
                break;
            case R.id.board_latest: // 최신글
                boardUrl = Urls.BOARD_LATEST;
                break;
            case R.id.board_freeboard:  // 자유게시판
                boardUrl = Urls.BOARD_FREEBOARD;
                break;
            case R.id.board_womsplain:  // 웜스플레인
                boardUrl = Urls.BOARD_WOMSPLAIN;
                break;
            case R.id.board_hospital:   // 재활병원
                boardUrl = Urls.BOARD_HOSPITAL;
                break;
            case R.id.board_website:    // 사이트건의
                boardUrl = Urls.BOARD_WEBSITE;
                break;
            case R.id.board_deathnote:  // 데스노트
                boardUrl = Urls.BOARD_DEATHNOTE;
                break;
            case R.id.board_mapo:   // 마포대교밑
                boardUrl = Urls.BOARD_MAPO;
                break;
            case R.id.best_wonyum:  // 워념글
                boardUrl = Urls.BEST_WONYUM;
                break;
            case R.id.best_dailybest:   // 일간베스트
                boardUrl = Urls.BEST_DAILYBEST;
                break;
            case R.id.best_weeklybest:  // 주간베스트
                boardUrl = Urls.BEST_WEEKLYBEST;
                break;
            case R.id.best_monthlybest: // 월간베스트
                boardUrl = Urls.BEST_MONTHLYBEST;
                break;
            case R.id.board_project:    // 프로젝트
                boardUrl = Urls.BOARD_PROJECT;
                break;
            case R.id.board_global: // Global
                boardUrl = Urls.BOARD_GLOBAL;
                break;
            default:
                break;
        }

        /* Load a Board page with selected url */
        new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, currentPage); // Load board page with selected url

        /* Set up page related views and fields */
        setUpPage();
        inputPage.clearFocus();
        
        return view;
    }

    /**
     * Initialize layout: binding view references, setting up views, adapting listeners, etc.
     *
     * @param root Root view of the fragment.
     */
    private void initFragment(@NonNull View root) {

        /* Binding view references */
        scrollView = (ScrollView)root.findViewById(R.id.board_scrollview);
        layoutBoard = (LinearLayout)root.findViewById(R.id.layout_board);
        toolbarTitle = (TextView)getActivity().findViewById(R.id.toolbar_title);
        toolbarRefresh = (ImageView)getActivity().findViewById(R.id.toolbar_refresh);

        inputPage = (EditText)root.findViewById(R.id.input_page);
        goToFirst = (ImageView)root.findViewById(R.id.button_first_page);
        goToLast = (ImageView)root.findViewById(R.id.button_last_page);
        goPrevious = (ImageView)root.findViewById(R.id.button_previous_page);
        goNext = (ImageView)root.findViewById(R.id.button_next_page);

        /* Change toolbar title */
        toolbarTitle.setText(boardName);

        /* Attach listeners */
        toolbarRefresh.setOnClickListener(this);
        inputPage.setOnKeyListener(this);
        goToFirst.setOnClickListener(this);
        goToLast.setOnClickListener(this);
        goPrevious.setOnClickListener(this);
        goNext.setOnClickListener(this);
    }

    /**
     * Setting up page related views and fields.
     */
    private void setUpPage() {
        /* Set input-page field (edit text) by current page */
        inputPage.setText(currentPage.toString());

        /* Fetch last page number */
        new Thread() {
            public void run() {
                try  {
                    String url = Jsoup.connect(Urls.BASE_URL + boardUrl + Urls.PAGE_LAST).execute().url().toString();
                    lastPage = Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
                } catch (Exception e) {
                    e.printStackTrace();
                    lastPage = 1;
                }
            }
        }.start();
    }

    /**
     * Fetch a board (post list items) from a given url.
     */
    private class FetchBoard extends AsyncTask<Object, Void, Document> {

        private Context context;
        private ViewGroup parent;
        private LoadingFragment fragment;

        private String link, page, number, board, category, title, comments, writer, createdAt, views, likes;

        public FetchBoard(Context context, ViewGroup parent) {
            this.context = context;
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            /* Show loading dialog (process dialog) */
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
        protected Document doInBackground(Object... objects) {

            /* Compound url from the string parameters */
            String url = "";
            for (Object o: objects) {
                url += o.toString();   // board url + page number + query string etc.
            }

            /* Return a document object after connected to given url */
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

            /* Check if document is null */
            if (document == null) {
                return;
            }

            /* Select elements from the  document. It varies depending on which site you will parse. */
            Elements elements = document.select("table.basic tbody tr");

            /* Check if selected elements are null. */
            if (elements == null || elements.isEmpty()) {
                return;
            }

            /* Remove existing post items before adding new views. */
            if (parent.getChildCount() > 2) {
                parent.removeAllViews();
            }

            /* Bind each table data with a post item view */
            for (Element e: elements) {

                /* The number of children in an element. */
                int numOfData = e.select("td").size();

                /* Ignore the notice items */
                if (e.hasClass("notice")) {
                    continue;
                }

                /* Extract strings from the element */
                link = e.select("td.title a").attr("href");
                number = e.select("td.number").text();
                board = e.select("td.repository").text();
                category = e.select("td.title a span.category-text").text();
                title = e.select("td.title a").first().ownText();
                comments = e.select("td.title span.comments-count").text();
                writer = e.select("td").get(numOfData-4).text();
                createdAt = e.select("td").get(numOfData-3).text();
                views = e.select("td").get(numOfData-2).text();
                likes = e.select("td").get(numOfData-1).text();

                /* Create a new view model and a view object with extracted strings */
                Post post = new Post(number, title, writer, createdAt, category, board, views, likes, comments);
                PostItemView view = new PostItemView(context, post);    // create (inflate) a post item view
                view.setLink(link);

                /* Add this view to the parent layout */
                parent.addView(view);
            }

            /* Update input-page field (EditText) */
            page = document.getElementById("posts-page").val();
            currentPage = Integer.parseInt(page);
            inputPage.setText(currentPage.toString());
            inputPage.clearFocus();

            /* Scroll to the top */
            scrollView.smoothScrollTo(0, 0);

            /* Dismiss the process dialog */
            fragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
                
        switch (v.getId()) {
            /* Refresh button: Reload current page */
            case R.id.toolbar_refresh:
                /* Rotate the button image */
                Animation refreshAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_360);
                v.startAnimation(refreshAnimation);
                
                /* Reload current page */
                new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, currentPage);
                break;

            /* First page arrow: Go to the first page */
            case R.id.button_first_page:
                new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, Urls.PAGE_FIRST);
                break;

            /* Last page arrow: Go to the last page */
            case R.id.button_last_page:
                new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, Urls.PAGE_LAST);
                break;

             /* Left arrow: Go to the previous page */
            case R.id.button_previous_page:
                if (currentPage <= 1) { // When it's first page
                    Toast.makeText(getContext(), "첫 페이지입니다.", Toast.LENGTH_LONG).show();
                    new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, Urls.PAGE_FIRST);
                } else {
                    new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, --currentPage);
                }
                break;

            /* Right arrow: Go to the next page */
            case R.id.button_next_page:
                if (currentPage >= lastPage) {  // When it's last page
                    Toast.makeText(getContext(), "마지막 페이지입니다.", Toast.LENGTH_LONG).show();
                    new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, Urls.PAGE_LAST);
                } else {
                    new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, ++currentPage);
                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()) {
            case R.id.input_page:
                /* When user pressed 'enter' key */
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { // when user press 'enter' key

                    /* Clear focus */
                    v.clearFocus();

                    /* Move to the page as user input */
                    int page = Integer.parseInt(((EditText) v).getText().toString());
                    if (page >= lastPage) {
                        new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, Urls.PAGE_LAST);
                    } else if (page < 1) {
                        new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, Urls.PAGE_FIRST);
                    } else {
                        new FetchBoard(getContext(), layoutBoard).execute(Urls.BASE_URL, boardUrl, page);
                    }
                    return true;
                }
                break;
        }
        return false;
    }
}
