package bg.fmi.news.uri;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultUriBuilderTest {

    @Test
    void testAddKeyword() throws URISyntaxException {
        var uri = new DefaultUriBuilder("https", "fmi.bg", "/news")
                                   .addKeyword("keyword", "value")
                                   .build();

        assertEquals(URI.create("https://fmi.bg/news?&keyword=value"), uri, "URI is not equivalent to the expected one");
    }

    @Test
    void testAddKeywords() throws URISyntaxException {
        var uri = new DefaultUriBuilder("https", "fmi.bg", "/news")
                           .addKeywords(Map.of(
                                "k1", "v1",
                                "k2", "v2"
                            ))
                           .build();

        var expected = URI.create("https://fmi.bg/news?&k1=v1&k2=v2");
        var equivalent = URI.create("https://fmi.bg/news?&k1=v1&k2=v2");

        assertTrue(uri.equals(expected) || uri.equals(equivalent), "URI is not equivalent to the expected one");
    }
}