package bg.fmi.news.uri;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

public interface UriBuilder {

    static UriBuilder of(String scheme, String authority, String endpoint) {
        Objects.requireNonNull(scheme, "scheme cannot be null");
        Objects.requireNonNull(authority, "authority cannot be null");
        Objects.requireNonNull(scheme, "endpoint cannot be null");

        return new DefaultUriBuilder(scheme, authority, endpoint);
    }

    UriBuilder addKeyword(String key, String value);

    UriBuilder addKeywords(Map<String, String> keywords);

    URI build() throws URISyntaxException;
}
