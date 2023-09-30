package bg.fmi.news.uri;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultUriBuilder implements UriBuilder {

    private final String scheme;
    private final String authority;
    private final String endpoint;

    private Map<String, String> keywords = new HashMap<>();

    DefaultUriBuilder(String scheme, String authority, String endpoint) {
        this.scheme = scheme;
        this.authority = authority;
        this.endpoint = endpoint;
    }

    @Override
    public DefaultUriBuilder addKeyword(String key, String value) {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(value, "value cannot be null");

        this.keywords.put(key, value);
        return this;
    }

    @Override
    public DefaultUriBuilder addKeywords(Map<String, String> keywords) {
        Objects.requireNonNull(keywords, "keywords cannot be null");

        this.keywords.putAll(keywords);
        return this;
    }

    @Override
    public URI build() throws URISyntaxException {
        return new URI(scheme, authority, endpoint, toQuery(), null);
    }

    private String toQuery() {
        StringBuilder builder = new StringBuilder();

        keywords.forEach((key, value) -> {
            builder.append('&')
                   .append(key)
                   .append('=')
                   .append(value);
        });

        return builder.toString();
    }

}
