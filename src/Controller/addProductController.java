package controller;
/**
 * Public add product controller class
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
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * initializes add product controller class
 */
public class addProductController implements Initializable {
    @FXML
    private TextField prodIdTxt;

    @FXML
    private TextField prodNameTxt;

    @FXML
    private TextField prodInvTxt;

    @FXML
    private TextField prodCostTxt;

    @FXML
    private TextField prodMaxTxt;

    @FXML
    private TextField prodMinTxt;

    @FXML
    private TextField prodSearchTxt;

    @FXML
    private TableView<Part> partTable;

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
    private Button addProductBtn;

    @FXML
    private Button deleteProdBtn;

    @FXML
    private Button saveProdBtn;

    @FXML
    private Button cancelBtn;

    Stage stage;

    Parent scene;

    private static int prdId =1000;

    private Product newProduct = new Product(0, "", 0.00, 0, 0, 0, null);

    ObservableList<Part> asscPartList = FXCollections.observableArrayList();

    /**
     * initializes
     * allows access to resource bundle
     * populates the parts and associated parts tables on the add product screen
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

        prdId = getPrdId();
        prodIdTxt.setText(String.valueOf(prdId));
    }

    /**
     * gets the product ID, auto incremented starts at 1000 to differentiate from part id
     * @return
     */
    public static int getPrdId() {
        prdId++;
        return prdId;
    }

    /**
     * performs search of parts based on the part ID or a name or partial name
     * occurs on key pressed;
     * if user searches for a part that can not be found will generate an error advising of such
     * @param event
     */
    public void handleProdSearch(ActionEvent event) {
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
     * returns all part list back to parts tableview
     * @param keyEvent
     */
    @FXML
    public void prodSearch (KeyEvent keyEvent) {
        if (prodSearchTxt.getText().isEmpty()) {
            partTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * add the part to be associated with the product
     * sets the added product into the associated parts table view
     * @param event
     * @throws IOException
     */
    public void handleAddProduct(ActionEvent event) throws IOException {
        Part part = partTable.getSelectionModel().getSelectedItem();
        asscPartList.add(part);
        asscPartTable.setItems(asscPartList);
    }

    /**
     * removes an associated part from the product
     * removes part from associated part table
     * asks for confirmation of part delete prior to removal
     * @param event
     */
    public void handleProdDelete(ActionEvent event) {
        Part delPrt = asscPartTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete the associated part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            asscPartList.remove(delPrt);
            asscPartTable.setItems(asscPartList);
        }

        }

    /**
     * handles new product creation and links associated parts to product on click of save button;
     * validates all product values prior to actually saving the product;
     * if an error is found it will generate an error screen to show the user what may be the issue and where to correct.
     * returns user to main screen upon successful product creation.
     * @param event
     */
    public void handleSave(ActionEvent event) {
        try {
            newProduct.setId(Integer.parseInt(prodIdTxt.getText()));
            newProduct.setName(prodNameTxt.getText());
            newProduct.setPrice(Double.parseDouble(prodCostTxt.getText()));
            newProduct.setStock(Integer.parseInt(prodInvTxt.getText()));
            newProduct.setMin(Integer.parseInt(prodMinTxt.getText()));
            newProduct.setMax(Integer.parseInt(prodMaxTxt.getText()));
            newProduct.setAsscParts(asscPartList);

            if (Integer.parseInt(prodMinTxt.getText()) > Integer.parseInt(prodMaxTxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Save Product Error");
                alert.setContentText("The minimum value cannot exceed the maximum value.");
                alert.showAndWait();
            } else if (Integer.parseInt(prodInvTxt.getText()) > Integer.parseInt(prodMaxTxt.getText()) || Integer.parseInt(prodInvTxt.getText()) < Integer.parseInt(prodMinTxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Save Product Error");
                alert.setContentText("The inventory values should be between the min and max values.");
                alert.showAndWait();
            } else if (prodNameTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Save Product Error");
                alert.setContentText("The product requires a name to be saved.");
                alert.showAndWait();
            } else {
                Inventory.addProduct(newProduct);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException | IOException exception) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Save Product Error");
            alert.setContentText("Price, Inv, Max, and Min values must contain number values to successfully add the new product.");
            alert.showAndWait();
        }
    }

    /**
     * upon clicking the cancel button on the add product form
     * program asks the user to confirm they do not want to add a new product
     * upon confirmation returns the user to the main screen
     * @param event
     * @throws IOException
     */
    public void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Product");
        alert.setHeaderText("This will cancel adding a new product.");
        alert.setContentText("Do you wish to proceed with canceling adding a new product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

}
