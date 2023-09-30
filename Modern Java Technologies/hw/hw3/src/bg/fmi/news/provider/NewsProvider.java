package bg.fmi.news.provider;

import bg.fmi.news.exceptions.ApiException;
import bg.fmi.news.response.Article;

import java.util.Collection;

public interface NewsProvider {

    /**
     * Searches for news articles by keywords and/or category and/or country
     *
     * @param keywords the keywords to be searched in news articles
     * @param category the category to be searched, or null
     * @param country the 2-letter ISO 3166-1 code of the country, or null
     * @param pageNumber the page number to return
     *
     * @throws ApiException if there is a problem with the api
     *
     * @return {@link Collection} of {@link Article} that were found at the page number
     */
    Collection<Article> search(Collection<String> keywords, String category, String country, int pageNumber)
        throws ApiException;


    /**
     * @return maximum page size
     */
    int getMaxPageSize();
}
