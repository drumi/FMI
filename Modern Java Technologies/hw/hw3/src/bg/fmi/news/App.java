package bg.fmi.news;

import bg.fmi.news.gui.TextGui;
import bg.fmi.news.provider.DefaultNewsProvider;

import java.net.http.HttpClient;

public final class App {

    public static void main(String[] args) {
        run();
    }

    static void run() {
        var httpClient = HttpClient.newHttpClient();
        var input = System.in;
        var output = System.out;
        var provider = new DefaultNewsProvider(httpClient);
        var gui = new TextGui(provider, input, output);

        gui.run();
    }
}
