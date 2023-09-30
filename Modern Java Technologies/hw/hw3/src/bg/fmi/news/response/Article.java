package bg.fmi.news.response;

public final class Article {

    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    private Article() {
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Article {\n" +
            "    source = " + source + "\n" +
            "    author = " + author + "\n" +
            "    title = " + title + "\n" +
            "    description = " + description + "\n" +
            "    url = " + url + "\n" +
            "    urlToImage = " + urlToImage + "\n" +
            "    publishedAt = " + publishedAt + "\n" +
            "    content = " + content + "\n" +
            '}';
    }
}
