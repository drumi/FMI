import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class StockTest extends Application {

    private static final String USER_TEMPLATE = "Please enter the stock's %s";
    private static final String UNPUT_TITLE = "User input";
    private static final String MESSAGE_TITLE = "Information";

    private static TextInputDialog dialog = new TextInputDialog();
    private static Alert alert = new Alert(Alert.AlertType.INFORMATION);

    private static String inputDialog(String infoMessage, String titleBar, String headerMessage) {
        dialog.getEditor().clear();

        dialog.setTitle(titleBar);
        dialog.setHeaderText(headerMessage);
        dialog.setContentText(infoMessage);

        return dialog.showAndWait().orElse(null);
    }

    private static void messageDialog(String infoMessage, String titleBar, String headerMessage) {
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);

        alert.showAndWait();
    }

    private String inputString(String displayName, String errorMessage, String headerMessage) {
        String input = inputDialog(displayName, UNPUT_TITLE, String.format(USER_TEMPLATE, displayName));

        while (input == null || input.isBlank()) {
            messageDialog(errorMessage, MESSAGE_TITLE, headerMessage);

            input = inputDialog(displayName, UNPUT_TITLE, String.format(USER_TEMPLATE, displayName));

        }

        return input;
    }

    private double inputPrice(String displayName, String errorMessage, String headerMessage) {
        String currentPriceInput = inputDialog(displayName, UNPUT_TITLE, String.format(USER_TEMPLATE, displayName));
        double currentPrice = Double.parseDouble(currentPriceInput);

        while (currentPrice <= 0.0d) {
            messageDialog(errorMessage, MESSAGE_TITLE, headerMessage);

            currentPriceInput = inputDialog(displayName, UNPUT_TITLE, String.format(USER_TEMPLATE, displayName));
            currentPrice = Double.parseDouble(currentPriceInput);
        }

        return currentPrice;
    }

    @Override
    public void start(Stage stage) throws Exception {
        String name = inputString("name", "Please enter a valid name", "Invalid input");
        String symbol = inputString("symbol", "Please enter a valid symbol", "Invalid input");
        double currentPrice = inputPrice("current price", "Please enter a valid price", "Invalid input");
        double previousPrice = inputPrice("previous price", "Please enter a valid price", "Invalid input");

        Stock stock = new Stock(symbol, name);
        stock.setCurrentPrice(currentPrice);
        stock.setPreviousClosingPrice(previousPrice);

        String stockData = String.format(
                "Stock name: %s%n" +
                "Stock symbol: %s%n" +
                "Stock current price: %.2f%n" +
                "Stock previous price: %.2f%n" +
                "Change in percentage: %.2f %%%n",
                stock.getName(),
                stock.getSymbol(),
                stock.getCurrentPrice(),
                stock.getPreviousClosingPrice(),
                stock.changePercent()
        );

        messageDialog(stockData, "Your stock", "Stock information");
    }

    public static void main(String[] args) {
        launch(args);
    }
}