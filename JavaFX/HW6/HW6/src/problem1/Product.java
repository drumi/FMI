package problem1;



public class Product {

    private static int id;

    private final int INV_NUMBER;
    private String invDescription;
    private Category category;
    private double price;

    public Product(int INV_NUMBER, String invDescription, Category category, double price) {
        if (INV_NUMBER < 0) {
            this.INV_NUMBER = id++;
        } else {
            this.INV_NUMBER = INV_NUMBER;
        }

        setInvDescription(invDescription);
        setCategory(category);
        setPrice(price);
    }

    public Product(String invDescription, Category category, double price) {
        this(-1, invDescription, category, price);
    }

    public Product() {
        this(-1, "No description", Category.A, 1.);
    }

    public Product(Product p) {
        this(p.INV_NUMBER, p.invDescription, p.category, p.price);
    }

    public int getInvNumber() {
        return INV_NUMBER;
    }

    public String getInvDescription() {
        return invDescription;
    }

    public void setInvDescription(String invDescription) {
        if (invDescription != null) {
            this.invDescription = invDescription;
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category != null) {
            this.category = category;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        }
    }

    @Override
    public String toString() {
        return String.format("Product( %s, %s, %s, %.2f )", INV_NUMBER, category, invDescription, price);
    }
}
