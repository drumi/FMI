package bg.fmi.news.request;

import bg.fmi.news.uri.UriBuilder;

import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Collection;

public final class NewsRequest {

    private final Collection<String> keywords;
    private final String category;
    private final String country;
    private final int pageNumber;

    private final NewsRequestOptions options;

    private NewsRequest(Builder builder) {
        this.options = builder.options;
        this.category = builder.category;
        this.country = builder.country;
        this.pageNumber = builder.pageNumber;
        this.keywords = builder.keywords;
    }

    public Collection<String> getKeywords() {
        return keywords;
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public NewsRequestOptions getOptions() {
        return options;
    }

    public HttpRequest toHttpRequest() {
        var uriBuilder = UriBuilder.of(options.scheme(), options.authority(), options.endpoint());

        uriBuilder.addKeyword("apiKey", options.apiKey())
                  .addKeyword("pageSize", Integer.toString(options.pageSize()))
                  .addKeyword("q", String.join("+", keywords))
                  .addKeyword("page", Integer.toString(pageNumber));

        if (country != null) {
            uriBuilder.addKeyword("country", country);
        }

        if (country != null) {
            uriBuilder.addKeyword("category", category);
        }

        try {
            return HttpRequest.newBuilder()
                              .uri(uriBuilder.build())
                              .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Uri builder is not working correctly", e);
        }
    }

    public static Builder builder(NewsRequestOptions options) {
        return new Builder(options);
    }

    public static final class Builder {

        private Collection<String> keywords;
        private String category;
        private String country;
        private int pageNumber;

        private final NewsRequestOptions options;

        public Builder(NewsRequestOptions options) {
            this.options = options;
        }

        public Builder keywords(Collection<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder pageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public NewsRequest build() {
            return new NewsRequest(this);
        }
    }
}
