package bg.fmi.news.gui;

import bg.fmi.news.exceptions.ApiException;
import bg.fmi.news.provider.NewsProvider;
import bg.fmi.news.response.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextGuiTest {

    @Mock
    NewsProvider provider;

    @Mock
    OutputStream out;

    List<Article> articles;

    @BeforeEach
    void setUp() {
        articles = List.of();
    }

    @Test
    void testRunFinishes() throws ApiException {
        when(provider.search(anyCollection(), eq(null), eq(null), eq(1))).thenReturn(List.of());
        InputStream in = new ByteArrayInputStream("how\nnull\nnull\ny\n".getBytes(StandardCharsets.UTF_8));
        TextGui gui = new TextGui(provider, in, out);
        assertTimeoutPreemptively(Duration.ofSeconds(1), gui::run, "Gui did not finish when expected to");
    }

}