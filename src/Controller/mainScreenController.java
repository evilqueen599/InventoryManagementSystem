package controller;
/**
 * Public mainScreen class
 */

/**
 * @author Tricia Smith
 */
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import static model.Inventory.*;

/**
 * initializes mainScreenController class
 */
public class mainScreenController implements Initializable {

    Stage stage;

    @FXML
    private TextField searchPart;

    @FXML
    public TableView<Part> partTable;

    @FXML
    public TableColumn<Part, Integer> partId;

    @FXML
    public TableColumn<Part, String> partName;

    @FXML
    public TableColumn<Part, Integer> partInvLevel;

    @FXML
    public TableColumn<Part, Double> costPerUnit;

    @FXML
    public Button addPartBtn;

    @FXML
    public Button modifyPartBtn;

    @FXML
    public TextField searchProduct;

    @FXML
    public TableView<Product> productTable;

    @FXML
    public TableColumn<Product, Integer> productId;

    @FXML
    public TableColumn<Product, String> productName;

    @FXML
    public TableColumn<Product, Integer> productInvLevel;

    @FXML
    public TableColumn<Product, Double> productCostPerUnit;

    @FXML
    public Button addProductBtn;

    @FXML
    public Button modifyProductBtn;

    @FXML
    public Button deletePartBtn;

    @FXML
    public Button deleteProductBtn;

    @FXML
    public Button ExitBtn;

    private static int index;

    public static int modifyIndex() {
        return index;
    }

    Scene scene;

    /**
     * initializes
     * allows access to resource bundle
     * populates table views with existing parts and products
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productTable.setItems(Inventory.getAllProducts());

        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setItems(Inventory.getAllParts());

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        costPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * gracefully closes the application
     */
    @FXML
    void handleExit() {
        System.exit(0);
    }

    /**
     * allows a user to search a part by its ID, name, or partial name
     * generates an error if an existing part is not found by ID or name
     * @param event
     */
    @FXML
    public void handlePartSearch(ActionEvent event) {
        String partSearch = searchPart.getText();
        try {
            ObservableList<Part> searchPart = Inventory.lookupPart(partSearch);
            if (searchPart.size() == 0) {
                int partId = Integer.parseInt(partSearch);
                Part search = Inventory.lookupPart(partId);
                searchPart.add(search);
                if (search == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Part Search Error");
                    alert.setContentText("No part found with that ID.");
                    alert.showAndWait();
                }
            }
            partTable.setItems(searchPart);
        }
        catch (NumberFormatException e) {
            partTable.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Part Search Error");
            alert.setContentText("No part found with that name.");
            alert.showAndWait();
        }
    }

    /**
     * upon clearing of part search text field restores the full part list to view
     * @param keyEvent
     */
    @FXML
    public void partSearchEmpty(KeyEvent keyEvent) {
        if (searchPart.getText().isEmpty()) {
            partTable.setItems(Inventory.getAllParts());
        }

    }

    /**
     * sends the user to the add part page upon click of add part button
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleAddPart(ActionEvent event) throws IOException {
        Stage stage = (Stage) addPartBtn.getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/addPart.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * if no part selected to modify generates error that user must select part to proceed
     * if part is selected will take user to the modify part screen
     * calls setPart method to cast the selection from the main screen and send the data to modify part screen
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    void handleModifyPart(ActionEvent actionEvent) throws IOException {
        if (partTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Modify Part Warning");
            alert.setContentText("You must first select the part you wish to modify.");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader =  new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/modifyPart.fxml"));
            fxmlLoader.load();

            modifyPartController modifyPartController = fxmlLoader.getController();
            modifyPartController.setPart(partTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }
    }

    /**
     * deletes the selected part from the part list
     * if no item selected will generate error to tell user to select a part
     * upon verifying part is selected will ask the user to confirm they wish to delete the part
     * upon receiving confirmation part is removed
     * @param event
     */
    @FXML
    public void handlePartDelete(ActionEvent event) {
        if (partTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Part Deletion Error");
            alert.setContentText("There were no selected items to delete.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Delete this part?");
            alert.setContentText("Are you sure you want to delete : " + partTable.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Part part;
                part = partTable.getSelectionModel().getSelectedItem();
                deletePart(part);
            }
        }
    }

    /**
     * upon selection the add product button sends user to the add product screen.
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleAddProduct(ActionEvent event) throws IOException {

        Stage stage = (Stage) addProductBtn.getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/addProduct.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * if no product selected to modify generates error that user must select product to proceed
     * if product is selected will take user to the modify product screen
     * calls setProduct method to cast the selection from the main screen and send the data to modify product screen
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleModifyProduct(ActionEvent event) throws IOException {
        if (productTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Modify Product Error");
            alert.setContentText("You must first select the product you wish to modify.");
            alert.showAndWait();
        }
        else {
            FXMLLoader fxmlLoader =  new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/modifyProduct.fxml"));
            fxmlLoader.load();

            modifyProductController modifyProductController = fxmlLoader.getController();
            modifyProductController.setProduct(productTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }
    }

    /**
     * Upon attempting to delete a product will request confirmation that the user wants to remove the product
     * upon confirmation will delete product unless there are current parts associated with the product
     * if parts are associated with the product will generate an error asking the user to remove the part association prior to removal
     * if no product is selected to delete will generate an error advising user no product was selected.
     * @param event
     *
     */

    /**
     *FUTURE_ENHANCEMENT
     * For a future addition of a feature to the program I would recommend a redirect option when giving the user and associated part error when attempting to delete a product.
     * This would allow the user to go straight to the modify product remove the associated parts, then proceed with the product deletion if needed.
     */
    @FXML
    public void handleProductDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Product Delete");
        alert.setHeaderText("Delete this product?");
        alert.setContentText("Are you sure you want to delete : " + productTable.getSelectionModel().getSelectedItem().getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {
                Product product = productTable.getSelectionModel().getSelectedItem();
                if (product.getAsscParts().isEmpty()) {
                    deleteProduct(product);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Product Delete Error");
                    alert.setHeaderText("You cannot delete a product that has a current part associated with it.");
                alert.setContentText("Please remove associated parts before continuing with product deletion.");
                    alert.showAndWait();
                }
            } catch (UnsupportedOperationException e) {
                alert.setTitle("Product Delete Error");
                alert.setHeaderText("The Product has not been deleted!");
                alert.setContentText("There was no product selected for deletion.");
                alert.showAndWait();
            }

        }
    }

    /**
     *allows a user to search a part by its ID or name
     * this is currently case-sensitive so a future enhancement could be to set it to not be case-sensitive.
     *generates an error if an existing part is not found by ID or name
     * @param event
     */
    @FXML
    public void handleProductSearch(ActionEvent event) {
        String find = searchProduct.getText();
        try {
            ObservableList<Product> searchProduct = Inventory.lookupProduct(find);
            if (searchProduct.size() == 0) {
                int ProductId = Integer.parseInt(find);
                Product searchProd = (Product) Inventory.lookupProduct(ProductId);
                searchProduct.add(searchProd);
                if (searchProd == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Search Error");
                    alert.setContentText(" No product found by that ID.");
                    alert.showAndWait();
                }
            }
            productTable.setItems(searchProduct);
        }
        catch (NumberFormatException e) {
            productTable.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Search Error");
            alert.setContentText("No product found by that name.");
            alert.showAndWait();
        }
        }

    /**
     * upon clearing of product search text field will restore all products to the product tableview
     * @param keyEvent
     */
    public void searchProdEmpty(KeyEvent keyEvent) {
        if (searchProduct.getText().isEmpty()) {
            productTable.setItems(Inventory.getAllProducts());
        }
    }
}