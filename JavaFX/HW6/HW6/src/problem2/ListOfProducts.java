package problem2;

import problem1.Category;
import problem1.Product;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;


// E extends  Product чупи setup метода

public class ListOfProducts {

    private ArrayList<Product> products;


    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void setProducts(ArrayList<Product> products) {
        if (products != null) {
            this.products = new ArrayList<>(products);

        }
    }

    public void add(Product p) {
        if (products == null) {
            products = new ArrayList<>();
        }

        if (p != null) {
            products.add(p);
        }
    }

    public String[] toArray() {
        return products.stream()
                       .map(Product::toString)
                       .toArray(String[]::new);
    }

    public void setup() {
        Random random = new Random();

        if (products == null) {
            products = new ArrayList<>(10);
        } else {
            products.clear();
        }

        for (int i = 0; i < 10; i++) {
            products.add(
                new Product(
                    "new product",
                    Category.values()[random.nextInt(0, 4)],
                    random.nextDouble(100)
                )
            );
        }
    }

    public double averagePrice() {
        var average = products.stream()
                              .mapToDouble(Product::getPrice)
                              .average();

        return average.isPresent() ? average.getAsDouble() : 0.;
    }

    @Override
    public String toString() {
        String result = products.stream()
                                .map(Product::toString)
                                .collect(Collectors.joining(","));

        return '[' + result + ']';
    }
}
