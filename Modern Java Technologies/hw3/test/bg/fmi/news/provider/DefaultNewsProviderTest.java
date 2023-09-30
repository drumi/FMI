package bg.fmi.news.provider;

import bg.fmi.news.exceptions.ApiException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class DefaultNewsProviderTest {

    static final int HTTP_OK = 200;
    static final int HTTP_ERROR = 401;

    static String httpOkResponseBody;
    static String httpErrorResponseBody;

    @BeforeAll
    static void setUp() {
        httpOkResponseBody = "{\"status\":\"ok\",\"totalResults\":10,\"articles\":[{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"Stowaway found in South Africa plane wheel at Amsterdam airport\",\"description\":\"A man is discovered alive in the wheel section of a plane that landed in Amsterdam from South Africa.\",\"url\":\"http://www.bbc.co.uk/news/world-europe-60104010\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/82DD/production/_107710533_gettyimages-847669542.jpg\",\"publishedAt\":\"2022-01-23T14:37:24.3060064Z\",\"content\":\"Image source, Getty Images\\r\\nDutch police say they have found a stowaway alive in the wheel section of a plane that landed at Amsterdam's Schiphol airport from South Africa.\\r\\nFlights from Johannesburg… [+966 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"'Such is life': NZ PM calls off wedding\",\"description\":\"New Zealand's Prime Minister Jacinda Ardern has cancelled her wedding after announcing new Covid restrictions.\",\"url\":\"http://www.bbc.co.uk/news/world-asia-60103015\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/C87D/production/_122952315_p0bjvgww.jpg\",\"publishedAt\":\"2022-01-23T13:22:22.0642378Z\",\"content\":\"New Zealand's Prime Minister Jacinda Ardern has cancelled her wedding after announcing new Covid restrictions.\\r\\nThe entire country is set to be placed under the highest level of Covid restrictions af… [+216 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"Nusrat Ghani: Ex-minister told she was fired for her 'Muslimness'\",\"description\":\"Nusrat Ghani says she was told her faith made colleagues uncomfortable - but the chief whip denies her claim.\",\"url\":\"http://www.bbc.co.uk/news/uk-politics-60100525\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/7409/production/_122950792_mediaitem122950769.jpg\",\"publishedAt\":\"2022-01-23T04:22:21.4978992Z\",\"content\":\"Image source, UK Parliament\\r\\nA Muslim MP says her faith was raised by a government whip as a reason why she was sacked as a minister in 2020. \\r\\nAccording to the Sunday Times, Tory Nusrat Ghani says w… [+3739 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"IS prison break in Syria sparks days of clashes\",\"description\":\"More than 70 people have been killed in the fighting between militants and Kurdish-led forces.\",\"url\":\"http://www.bbc.co.uk/news/world-middle-east-60100364\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/40DD/production/_122950661_gettyimages-1237907398.jpg\",\"publishedAt\":\"2022-01-23T03:07:17.1194432Z\",\"content\":\"Image caption, Fighting has continued in Hasaka since Thursday\\r\\nIntense fighting is taking place in north-eastern Syria after Islamic State (IS) fighters tried to break inmates out of a Kurdish-run p… [+2914 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"Overweight armadillo twins on post-Christmas diet\",\"description\":\"Sussex-based armadillos Patsy and Eddie have to shift some pounds after their festive feasting.\",\"url\":\"http://www.bbc.co.uk/news/uk-60083200\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/10170/production/_122940956_p0bjnqmt.jpg\",\"publishedAt\":\"2022-01-23T01:37:23.1809659Z\",\"content\":\"If you're trying to lose some weight after eating too much over Christmas, then you're not alone. \\r\\nArmadillo twins Patsy and Eddie have been put on a strict diet by the zookeepers at their home in D… [+187 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"Sedef Kabas: Turkish journalist jailed for reciting proverb\",\"description\":\"Sedef Kabas denies using the proverb to insult President Recep Tayyip Erdogan.\",\"url\":\"http://www.bbc.co.uk/news/world-europe-60099931\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/18A1/production/_122950360_gettyimages-491522210.jpg\",\"publishedAt\":\"2022-01-23T01:07:19.3822231Z\",\"content\":\"Image caption, Sedef Kabas is accused of insulting President Recep Tayyip Erdogan\\r\\nA Turkish court has detained well-known journalist Sedef Kabas for allegedly insulting the country's president.\\r\\nMs … [+1665 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"German navy chief resigns over Ukraine comments\",\"description\":\"Kay-Achim Schönbach said the idea that Russia wanted to invade Ukraine was nonsense.\",\"url\":\"http://www.bbc.co.uk/news/world-europe-60099924\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/8D09/production/_122950163_51751687457_84e21669ed_o.jpg\",\"publishedAt\":\"2022-01-22T23:52:22.5156853Z\",\"content\":\"Image caption, Kay-Achim Schönbach (R) said President Putin wanted respect\\r\\nThe head of the German navy has resigned over controversial comments he made over Ukraine. \\r\\nKay-Achim Schönbach said the i… [+2665 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"Russia-Ukraine tensions: UK warns of plot to install pro-Moscow ally\",\"description\":\"The Foreign Office takes the unusual step of naming a former Ukrainian MP as a potential Kremlin candidate.\",\"url\":\"http://www.bbc.co.uk/news/uk-60095459\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/12D44/production/_122942177_gettyimages-1237834830.jpg\",\"publishedAt\":\"2022-01-22T22:52:22.2088597Z\",\"content\":\"Image source, Getty Images\\r\\nThe UK has accused President Putin of plotting to install a pro-Moscow figure to lead Ukraine's government. \\r\\nThe Foreign Office took the unusual step of naming former Ukr… [+5058 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"'Surreal' January wildfire shuts California highway\",\"description\":\"An unseasonal blaze rages in the Monterey County, forcing evacuations and the closure of Highway 1.\",\"url\":\"http://www.bbc.co.uk/news/world-us-canada-60092300\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/94A6/production/_122945083_californiafire.png\",\"publishedAt\":\"2022-01-22T21:52:22.8558703Z\",\"content\":\"Image source, The Mercury News via Getty Images\\r\\nImage caption, Flames were seen moving towards the famous Bixby Creek Bridge on Saturday morning\\r\\nAn unseasonal wildfire is raging in California's Mon… [+1259 chars]\"},{\"source\":{\"id\":\"bbc-news\",\"name\":\"BBC News\"},\"author\":\"BBC News\",\"title\":\"Jean-Jacques Savin: French adventurer dies crossing Atlantic Ocean\",\"description\":\"Jean-Jacques Savin, who was 75, had previously made the voyage in a large barrel.\",\"url\":\"http://www.bbc.co.uk/news/world-europe-60097918\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/17EAC/production/_122946979_savinlargegetty2.jpg\",\"publishedAt\":\"2022-01-22T19:22:21.1117071Z\",\"content\":\"Image source, Getty Images\\r\\nImage caption, The boat used by Jean-Jacques Savin was found overturned\\r\\nA 75-year-old Frenchman who was trying to row across the Atlantic Ocean has been found dead at sea… [+1743 chars]\"}]}";
        httpErrorResponseBody = "{\"status\":\"error\",\"code\":\"apiKeyInvalid\",\"message\":\"Your API key is invalid or incorrect. Check your key, or go to https://newsapi.org to create a free API key.\"}";
    }

    @Mock
    HttpClient client;

    @Mock
    HttpResponse<String> response;

    @Mock
    CompletableFuture<HttpResponse<String>> completableFuture;

    @Test
    void testSearch() throws ApiException {
        when(response.statusCode()).thenReturn(HTTP_OK);
        when(response.body()).thenReturn(httpOkResponseBody);

        when(completableFuture.join()).thenReturn(response);

        when(client.<String>sendAsync(any(), any())).thenReturn(completableFuture);

        DefaultNewsProvider provider = new DefaultNewsProvider(client);

        var articles = provider.search(List.of("how"), null, null, 1);

        assertEquals(10, articles.size(), "The query returned different size from the expected one");
    }

    @Test
    void testSearchThrowsOnInvalidRequest() {

        when(response.statusCode()).thenReturn(HTTP_ERROR);
        when(response.body()).thenReturn(httpErrorResponseBody);

        when(completableFuture.join()).thenReturn(response);

        when(client.<String>sendAsync(any(), any())).thenReturn(completableFuture);

        DefaultNewsProvider provider = new DefaultNewsProvider(client);

        assertThrows(ApiException.class, () -> provider.search(List.of("how"), null, null, 1), "Method should throw when request returns errors");
    }

}