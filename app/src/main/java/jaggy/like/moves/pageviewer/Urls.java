package jaggy.like.moves.pageviewer;

/**
 * Urls of boards from the original site
 * used in {@link BoardFragment}, {@link PostFragment}, {@link CommentsFragment}
 */
public final class Urls {

    public static final String BASE_URL = "https://okky.kr";

    public static final String PAGE_FIRST = "1";
    public static final String PAGE_LAST = "?last=true";

    /* User agent */
    public static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";    // User agent

    /* Board urls */
    public static final String TECH_ALL = "/articles/tech?offset=";    /* Tech */
    public static final String TECH_NEWS = "/articles/news?offset=";   /* IT News & 정보 */
    public static final String TECH_TIPS = "/articles/tips?offset=";   /* Tips & 강좌 */

    public static final String COMMUNITY_ALL = "/articles/community?offset=";  /* 커뮤니티 */
    public static final String COMMUNITY_NOTICE = "/articles/notice?offset=";  /* 공지사항 */
    public static final String COMMUNITY_LIFE = "/articles/life?offset=";  /* 사는얘기 */
    public static final String COMMUNITY_FORUM = "/articles/forum?offset=";    /* 포럼 */
    public static final String COMMUNITY_EVENT = "/articles/event?offset=";    /* IT 행사 */
    public static final String COMMUNITY_GATHERING = "/articles/gathering?offset=";    /* 정기모임 */
    public static final String COMMUNITY_PROMOTE = "/articles/promote?offset=";    /* 학원/홍보 */

    public static final String COLUMNS = "/articles/columns?offset=";  /* 칼럼 */

}
