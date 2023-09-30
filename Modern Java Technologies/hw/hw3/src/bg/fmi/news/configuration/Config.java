package bg.fmi.news.configuration;

import bg.fmi.news.request.NewsRequestOptions;

public interface Config {

    static NewsRequestOptions options() {
        final String scheme = "https";
        final String authority = "newsapi.org";
        final String endpoint = "/v2/top-headlines";
        final String apiKey = "bb6f33987c8f4708bf099271a4147a47";

        final int pageSize = 50;

        return new NewsRequestOptions(scheme, authority, endpoint, pageSize, apiKey);
    }

}
