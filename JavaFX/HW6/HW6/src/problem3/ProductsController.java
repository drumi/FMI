package problem3;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import problem1.Category;
import problem1.Product;
import problem2.ListOfProducts;

public class ProductsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAddToList;

    @FXML
    private Button btnAveragePrice;

    @FXML
    private Button btnGroupByCategory;

    @FXML
    private Button btnShowProducts;

    @FXML
    private Button btnSortProducts;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblProductDetails;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextArea txaProducts;

    private ListOfProducts listOfProducts;

    @FXML
    void btnAddToListClicked(ActionEvent event) {
        var description = getDescriptionFromUser();
        var category = getCategoryFromUser();
        var price = getPriceFromUser();


        if (description.isPresent() && category.isPresent() && price.isPresent()) {
            listOfProducts.add(
                new Product(
                    description.get(),
                    category.get(),
                    price.get()
                )
            );
        }

    }

    @FXML
    void btnAveragePriceClicked(ActionEvent event) {
        txaProducts.setText(
            String.format("%.2f", listOfProducts.averagePrice())
        );
    }

    @FXML
    void btnGroupByCategoryClicked(ActionEvent event) {
        Map<Category, List<Product>> groups
            = listOfProducts.getProducts().stream()
                                          .collect(Collectors.groupingBy(
                                                Product::getCategory,
                                                TreeMap::new,
                                                Collectors.toList()
                                              )
                                          );

        var sb = new StringBuilder();
        for (var entry : groups.entrySet()) {
            sb.append(entry.getKey()).append(":\n");

            sb.append(productsToText(entry.getValue()));
            sb.append("\n\n");
        }

        txaProducts.setText(sb.toString());
    }

    @FXML
    void btnShowProductsClicked(ActionEvent event) {
        var price = getPriceFromUser();

        if (price.isPresent()) {
            var filteredProducts = listOfProducts.getProducts().stream()
                                                 .filter(p -> p.getPrice() > price.get())
                                                 .toList();
            displayProducts(filteredProducts);
        }

    }

    @FXML
    void btnSortProductsClicked(ActionEvent event) {
        var lst = listOfProducts.getProducts();
        lst.sort(Comparator.comparingDouble(Product::getPrice).reversed());
        displayProducts(lst);
    }

    @FXML
    void initialize() {
        assert btnAddToList != null : "fx:id=\"btnAddToList\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert btnAveragePrice != null : "fx:id=\"btnAveragePrice\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert btnGroupByCategory != null : "fx:id=\"btnGroupByCategory\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert btnShowProducts != null : "fx:id=\"btnShowProducts\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert btnSortProducts != null : "fx:id=\"btnSortProducts\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert lblCategory != null : "fx:id=\"lblCategory\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert lblDescription != null : "fx:id=\"lblDescription\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert lblPrice != null : "fx:id=\"lblPrice\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert lblProductDetails != null : "fx:id=\"lblProductDetails\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert txtCategory != null : "fx:id=\"txtCategory\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert txtDescription != null : "fx:id=\"txtDescription\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert txtPrice != null : "fx:id=\"txtPrice\" was not injected: check your FXML file 'sceneProducts.fxml'.";
        assert txaProducts != null : "fx:id=\"txaProducts\" was not injected: check your FXML file 'sceneProducts.fxml'.";

        listOfProducts = new ListOfProducts();
        listOfProducts.setup();
    }

    private void displayProducts(List<Product> productsList) {
        txaProducts.setText(productsToText(productsList));
    }

    private String  productsToText(List<Product> productsList) {
        return productsList.stream()
                           .map(Product::toString)
                           .collect(Collectors.joining("\n"));
    }

    private Optional<Double> getPriceFromUser() {
        double price = 0;
        try {
            price = Double.parseDouble(txtPrice.getText());

            if (price < 0) {
                txtPrice.setText("Price must be positive");
                return Optional.empty();
            }
        } catch (NumberFormatException ex) {
            txtPrice.setText("Invalid price");
            return Optional.empty();
        }

        return Optional.of(price);
    }

    private Optional<String> getDescriptionFromUser() {
        if (txtDescription.getText().isBlank()) {
            txtDescription.setText("Description cannot be empty");
            return Optional.empty();
        }

        return Optional.of(txtDescription.getText());
    }

    private Optional<Category> getCategoryFromUser() {
        boolean isInvalidCategory = Arrays.stream(Category.values())
                                          .noneMatch(cat -> cat.toString().equals(txtCategory.getText()));
        if (isInvalidCategory) {
            txtCategory.setText(
                "Category must be in (" +
                    Arrays.stream(Category.values())
                          .map(Category::toString)
                          .collect(Collectors.joining(","))
                    + ")"
            );

            return Optional.empty();
        }

        return Optional.of(Category.valueOf(txtCategory.getText()));
    }

}
