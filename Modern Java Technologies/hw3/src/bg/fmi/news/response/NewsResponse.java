package bg.fmi.news.response;

import java.util.Collection;
import java.util.Collections;

public final class NewsResponse {

    private String status;
    private int totalResults;
    private Collection<Article> articles;

    private NewsResponse() {
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public Collection<Article> getArticles() {
        return Collections.unmodifiableCollection(articles);
    }

}
