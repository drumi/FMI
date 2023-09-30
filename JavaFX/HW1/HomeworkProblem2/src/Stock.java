public class Stock {

    private static double DEFAULT_PREVIOUS_PRICE = 1.0d;
    private static double DEFAULT_CURRENT_PRICE = 1.0d;
    private static String DEFAULT_SYMBOL = "UNKNOWN";
    private static String DEFAULT_NAME = "UNNAMED";

    private String symbol;
    private String name;
    private double previousClosingPrice;
    private double currentPrice;

    public Stock(String symbol, String name) {
        setSymbol(symbol);
        setName(name);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPreviousClosingPrice() {
        return previousClosingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    private void setSymbol(String symbol) {
        if (symbol == null || symbol.isBlank())
            this.symbol = DEFAULT_SYMBOL;
        else
            this.symbol = symbol;
    }

    private void setName(String name) {
        if (name == null || name.isBlank())
            this.name = DEFAULT_NAME;
        else
            this.name = name;
    }

    public void setPreviousClosingPrice(double previousClosingPrice) {
        if (previousClosingPrice <= 0)
            this.previousClosingPrice = DEFAULT_PREVIOUS_PRICE;
        else
            this.previousClosingPrice = previousClosingPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        if (currentPrice <= 0)
            this.currentPrice = DEFAULT_CURRENT_PRICE;
        else
            this.currentPrice = currentPrice;
    }

    public double changePercent() {
        double change = currentPrice - previousClosingPrice;

        return 100 * change / previousClosingPrice;
    }

}
