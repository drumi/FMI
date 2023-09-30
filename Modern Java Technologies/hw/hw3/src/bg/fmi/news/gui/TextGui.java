package bg.fmi.news.gui;

import bg.fmi.news.exceptions.ApiException;
import bg.fmi.news.provider.NewsProvider;
import bg.fmi.news.response.Article;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class TextGui implements Runnable {

    private final NewsProvider newsProvider;
    private final Scanner scanner;
    private final PrintWriter writer;

    public TextGui(NewsProvider newsProvider, InputStream input, OutputStream output) {
        this.newsProvider = newsProvider;
        this.scanner = new Scanner(input);
        this.writer = new PrintWriter(output, true);
    }

    @Override
    public void run() {
        State state = new Start(newsProvider, scanner, writer);

        while (state.isRunning()) {
            state = state.handle();
        }
    }

    private static final class StateData {

        private String[] keywords = new String[0];
        private String category = null;
        private String country = null;
        private Collection<Article> articles = Collections.emptyList();
        private int pageNumber = 1;

        public StateData() {
        }

        public StateData(String[] keywords, String category, String country,
                          Collection<Article> articles, int pageNumber) {
            this.keywords = keywords;
            this.category = category;
            this.country = country;
            this.articles = articles;
            this.pageNumber = pageNumber;
        }

        public String[] getKeywords() {
            return keywords;
        }

        public void setKeywords(String[] keywords) {
            this.keywords = keywords;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Collection<Article> getArticles() {
            return articles;
        }

        public void setArticles(Collection<Article> articles) {
            this.articles = articles;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }
    }

    private interface State {

        State handle();

        default boolean isRunning() {
            return true;
        }
    }

    private static class Start implements State {

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public Start(NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            return new WaitingKeywords(new StateData(), newsProvider, scanner, writer);
        }
    }

    private static class QuitPrompt implements State {

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public QuitPrompt(NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            writer.println("Do you want to quit? (y/n):");
            String in = scanner.nextLine();

            if ("y".equals(in)) {
                return new End();
            } else {
                return new Start(newsProvider, scanner, writer);
            }
        }
    }

    private static class End implements State {

        @Override
        public State handle() {
            return this;
        }

        @Override
        public boolean isRunning() {
            return false;
        }
    }

    private static class WaitingKeywords implements State {

        private final StateData data;

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public WaitingKeywords(StateData data, NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.data = data;
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            writer.println("Enter search keywords separated by spaces:");

            String userInput = scanner.nextLine();
            String[] keywords = userInput.split(" ");

            data.setKeywords(keywords);

            return new WaitingCategory(data, newsProvider, scanner, writer);
        }
    }

    private static class WaitingCategory implements State {

        private final StateData data;

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public WaitingCategory(StateData data, NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.data = data;
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            writer.println("Enter category or null:");

            String category = scanner.nextLine();
            category = category.equals("null") ? null : category;

            data.setCategory(category);

            return new WaitingCountry(data, newsProvider, scanner, writer);
        }
    }

    private static class WaitingCountry implements State {

        private final StateData data;

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public WaitingCountry(StateData data, NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.data = data;
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            writer.println("Enter country or null:");

            String country = scanner.nextLine();
            country = country.equals("null") ? null : country;

            data.setCountry(country);

            return new DispatchingRequest(data, newsProvider, scanner, writer);
        }
    }

    private static class DispatchingRequest implements State {

        private final StateData data;

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public DispatchingRequest(StateData data, NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.data = data;
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            try {
                var articles = newsProvider.search(Arrays.asList(data.getKeywords()), data.getCategory(),
                    data.getCountry(), data.getPageNumber());

                data.setArticles(articles);

                return new PrintingArticles(data, newsProvider, scanner, writer);
            } catch (ApiException e) {
                writer.println(e.getMessage());
                return new QuitPrompt(newsProvider, scanner, writer);
            }
        }
    }

    private static class PrintingArticles implements State {

        private final StateData data;

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public PrintingArticles(StateData data, NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.data = data;
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            var articles = data.getArticles();

            if (articles.size() == 0) {
                writer.println("No results found");
                return new QuitPrompt(newsProvider, scanner, writer);
            }

            writer.println(articles);

            if (articles.size() == newsProvider.getMaxPageSize()) {
                return new NextPageConfirmation(data, newsProvider, scanner, writer);
            } else {
                return new QuitPrompt(newsProvider, scanner, writer);
            }
        }
    }

    private static class NextPageConfirmation implements State {

        private final StateData data;

        private final NewsProvider newsProvider;
        private final Scanner scanner;
        private final PrintWriter writer;

        public NextPageConfirmation(StateData data, NewsProvider newsProvider, Scanner scanner, PrintWriter writer) {
            this.data = data;
            this.newsProvider = newsProvider;
            this.scanner = scanner;
            this.writer = writer;
        }

        @Override
        public State handle() {
            writer.println("Next page? (y/n):");
            String in = scanner.nextLine();

            if ("y".equals(in)) {
                data.setPageNumber(data.getPageNumber() + 1);
                return new DispatchingRequest(data, newsProvider, scanner, writer);
            } else {
                return new QuitPrompt(newsProvider, scanner, writer);
            }
        }
    }

}
