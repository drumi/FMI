package bg.fmi.news.request;

public record NewsRequestOptions(String scheme, String authority, String endpoint, int pageSize, String apiKey) {
}
