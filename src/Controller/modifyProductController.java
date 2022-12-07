package controller;
/**
 * Public modify product controller class
 */

/**
 * @author Tricia Smith
 */
import javafx.collections.FXCollections;
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
import model.Product;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.mainScreenController.modifyIndex;

public class modifyProductController implements Initializable {
    @FXML
  private TextField modProdIdTxt;

    @FXML
    private TextField modProdNameTxt;

    @FXML
    private TextField modProdInvTxt;

    @FXML
    private TextField modProdCostTxt;

    @FXML
    private TextField modProdMaxTxt;

    @FXML
    private TextField modProdMinTxt;

    @FXML
   private TextField prodSearchTxt;

    @FXML
    private TableView <Part> partTable;

    @FXML
    private TableColumn<Part, Integer> partId;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partInvLevel;

    @FXML
    private TableColumn<Part, Double> costPerUnit;

    @FXML
    private TableView<Part> asscPartTable;

    @FXML
   private TableColumn<Part, Integer> asscPartId;

    @FXML
    private TableColumn<Part, String> asscPartName;

    @FXML
    private TableColumn<Part, Integer> asscPartInvLevel;

    @FXML
    private TableColumn<Part, Double> asscCostPerUnit;

    @FXML
    public Button addProductBtn;

    @FXML
    public Button deleteProdBtn;

    @FXML
    public Button saveProdBtn;

    @FXML
    public Button cancelBtn;

    Product product;
    int selectedIndex = modifyIndex();

    Stage stage;
    Parent scene;

    public ObservableList<Part> asscPart = FXCollections.observableArrayList();

    /**
     * intializes and allows access to resource bundle
     * populates the tableviews on the modify product screen
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTable.setItems(Inventory.getAllParts());

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        costPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        asscPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        asscPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        asscPartInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        asscCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * add the part to be associated with the product
     * sets the added product into the associated parts table view
     * @param event
     */
    public void handleAddProduct(ActionEvent event) {
        Part part = partTable.getSelectionModel().getSelectedItem();
        asscPart.add(part);
        asscPartTable.setItems(asscPart);
    }

    /**
     * handles product modification and links associated parts to product on click of save button upon addition of associated part;
     * validates all product values prior to actually saving the product;
     * if an error is found will generate an error screen to show the user what may be the issue and where to correct.
     * returns user to main screen upon successful product update.
     * @param event
     * @throws IOException
     */
        public void handleSave (ActionEvent event) throws IOException {
            try {
                product.setId(Integer.parseInt(modProdIdTxt.getText()));
                product.setName(modProdNameTxt.getText());
                product.setPrice(Double.parseDouble(modProdCostTxt.getText()));
                product.setStock(Integer.parseInt(modProdInvTxt.getText()));
                product.setMin(Integer.parseInt(modProdMinTxt.getText()));
                product.setMax(Integer.parseInt(modProdMaxTxt.getText()));
                product.setAsscParts(product.getAsscParts());

                if (Integer.parseInt(modProdMinTxt.getText()) > Integer.parseInt(modProdMaxTxt.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Modify Product Error");
                    alert.setContentText("The minimum value cannot be greater than the maximum value.");
                    alert.showAndWait();
                } else if (Integer.parseInt(modProdInvTxt.getText()) > Integer.parseInt(modProdMaxTxt.getText()) || Integer.parseInt(modProdInvTxt.getText()) < Integer.parseInt(modProdMinTxt.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Modify Product Error");
                    alert.setContentText("The inventory value must be between the minimum and maximum values.");
                    alert.showAndWait();
                } else if (modProdNameTxt.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Modify Product Error");
                    alert.setContentText("The product must have a valid name.");
                    alert.showAndWait();
                } else {
                    Inventory.updateProduct(product.getId(), product);
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Modify Product Error");
                alert.setContentText("Price, Inv, Max, and Min values must contain number values to successfully modify the product.");
                alert.showAndWait();
            }
        }

    /**
     * generates screen asking user if they want to cancel product modification
     * upon confirmation redirects user back to main screen
     * @param event
     * @throws IOException
     */
    public void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Modifying Product?");
        alert.setHeaderText("This will cancel the modification of the product.");
        alert.setContentText("Do you wish to proceed with canceling modifying the product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * * performs search of parts based on the part ID or a name or partial name
     * occurs on key pressed;
     * if user searches for a part that can not be found will generate an error advising of such
     * @param event
     */
    public  void handleProdSearch(ActionEvent event) {
        String partSearch = prodSearchTxt.getText();
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
     * returns all parts back to parts tableview upon clearing of the search field
     * @param event
     */
    public  void prodSearch(KeyEvent event) {
        if (prodSearchTxt.getText().isEmpty()) {
            partTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * setProduct method populates the form fields with existing product data to modify
     * @param selectedProduct
     */
    public void setProduct(Product selectedProduct) {
        modProdIdTxt.setText(String.valueOf(selectedProduct.getId()));
        modProdNameTxt.setText(selectedProduct.getName());
        modProdInvTxt.setText((Integer.toString(selectedProduct.getStock())));
        modProdCostTxt.setText((Double.toString(selectedProduct.getPrice())));
        modProdMaxTxt.setText((Integer.toString(selectedProduct.getMax())));
        modProdMinTxt.setText((Integer.toString(selectedProduct.getMin())));

        product = selectedProduct;
        asscPart = product.getAsscParts();
        asscPartTable.setItems(asscPart);
    }

    /**
     *removes an associated part from the product
     *removes part from associated part table
     *asks for confirmation of part delete prior to removal
     * @param event
     */
    public void handleProdDelete(ActionEvent event) {
        Part delPrt = asscPartTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete the associated part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            asscPart.remove(delPrt);
            asscPartTable.setItems(asscPart);
        }
    }
}
