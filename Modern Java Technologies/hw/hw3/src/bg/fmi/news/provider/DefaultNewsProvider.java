package bg.fmi.news.provider;

import bg.fmi.news.configuration.Config;
import bg.fmi.news.exceptions.ApiException;
import bg.fmi.news.exceptions.ApiKeyDisabledException;
import bg.fmi.news.exceptions.ApiKeyExhaustedException;
import bg.fmi.news.exceptions.ApiKeyInvalidException;
import bg.fmi.news.exceptions.ApiKeyMissingException;
import bg.fmi.news.exceptions.ParameterInvalidException;
import bg.fmi.news.exceptions.ParametersMissingException;
import bg.fmi.news.exceptions.RateLimitedException;
import bg.fmi.news.exceptions.SourceDoesNotExistException;
import bg.fmi.news.exceptions.SourcesTooManyException;
import bg.fmi.news.request.NewsRequest;
import bg.fmi.news.response.Article;
import bg.fmi.news.response.ErrorResponse;
import bg.fmi.news.response.NewsResponse;
import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

public class DefaultNewsProvider implements NewsProvider {

    private static final int HTTP_OK = 200;

    private final HttpClient httpClient;

    public DefaultNewsProvider(HttpClient client) {
        this.httpClient = client;
    }

    @Override
    public Collection<Article> search(Collection<String> keywords, String category, String country, int pageNumber)
        throws ApiException {
        HttpRequest request = NewsRequest.builder(Config.options())
                                         .keywords(keywords)
                                         .category(category)
                                         .country(country)
                                         .pageNumber(pageNumber)
                                         .build()
                                         .toHttpRequest();

        var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        errorFilter(response);
        return handler(response);
    }

    @Override
    public int getMaxPageSize() {
        return Config.options().pageSize();
    }

    private void errorFilter(HttpResponse<String> response) throws ApiException {
        if (response.statusCode() != HTTP_OK) {
            Gson json = new Gson();
            var err = json.fromJson(response.body(), ErrorResponse.class);
            throwAsException(err);
        }
    }

    private void throwAsException(ErrorResponse err) throws ApiException {
        switch (err.getCode()) {
            case "apiKeyMissing" -> throw new ApiKeyMissingException(err.getMessage());
            case "apiKeyDisabled" -> throw new ApiKeyDisabledException(err.getMessage());
            case "apiKeyExhausted" -> throw new ApiKeyExhaustedException(err.getMessage());
            case "apiKeyInvalid" -> throw new ApiKeyInvalidException(err.getMessage());
            case "parameterInvalid" -> throw new ParameterInvalidException(err.getMessage());
            case "parametersMissing" -> throw new ParametersMissingException(err.getMessage());
            case "rateLimited" -> throw new RateLimitedException(err.getMessage());
            case "sourcesTooMany" -> throw new SourcesTooManyException(err.getMessage());
            case "sourceDoesNotExist" -> throw new SourceDoesNotExistException(err.getMessage());
            default -> throw new ApiException(err.getMessage());
        }
    }

    private Collection<Article> handler(HttpResponse<String> response) {
        Gson json = new Gson();
        var result = json.fromJson(response.body(), NewsResponse.class);
        return result.getArticles();
    }

}
