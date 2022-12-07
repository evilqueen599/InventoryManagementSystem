package controller;
/**
 * Public modify part controller class
 */

/**
 * @author Tricia Smith
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * initializes the modifyPartController
 */
public class modifyPartController implements Initializable {

    @FXML
    private RadioButton inHouse;

    @FXML
    private RadioButton Outsourced;

    @FXML
    private ToggleGroup modifyPartToggle;

    @FXML
    private TextField modPartIdTxt;

    @FXML
    private TextField modPartNameTxt;

    @FXML
    private TextField modPartInvTxt;

    @FXML
    private TextField modCostTxt;

    @FXML
    private TextField modMaxTxt;

    @FXML
    private TextField modMinTxt;

    @FXML
    private TextField modMachineIdTxt;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label modPartLabel;

    Stage stage;
    Parent scene;

    /**
     * changes the form label to show machine id on Inhouse radio button selection
     * @param event
     */
    public void handleInhouse(ActionEvent event) {
        modPartLabel.setText("Machine ID");
    }

    /**
     * changes the form label to show company name on Outsourced radio button selection
     * @param event
     */
    public void handleOutsourced(ActionEvent event) {
        modPartLabel.setText("Company Name");
    }

    /**
     * This setPart method populates the form fields for the user prior to editing
     */

    /**
     * LOGIC_ERROR
     * Here I ran into a logic error
     * when the method setPart was called from the main screen it was populating the min and max fields in reverse order
     * adjusting the vales being casts here corrected it but i had to have max cast to min and vice versa
     * upon realizing that I changed the order of the values in the inhouse and outsourced model files
     * once that was done I restored the max and min fields back to their original form and the error was corrected.
     *
     */

    public void setPart (Part selectedPart) {

        modPartIdTxt.setText(String.valueOf(selectedPart.getId()));
        modPartNameTxt.setText(selectedPart.getName());
        modPartInvTxt.setText(String.valueOf(selectedPart.getStock()));
        modCostTxt.setText(String.valueOf(selectedPart.getPrice()));
        modMinTxt.setText(String.valueOf(selectedPart.getMin()));
        modMaxTxt.setText(String.valueOf(selectedPart.getMax()));




        if (selectedPart instanceof InHouse) {
            inHouse.setSelected(true);
            modPartLabel.setText("Machine ID");
            modMachineIdTxt.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }
        else {
            Outsourced.setSelected(true);
            modPartLabel.setText("Company Name");
            modMachineIdTxt.setText(String.valueOf(((model.Outsourced) selectedPart).getCompanyName()));
        }
    }

    /**
     * initializes the scene and allows access to resource bundle
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    /**
     * This provides the save process to allow modification of an existing part. It has form compliance methods to ensure form
     * completion and validity before allowing the modifications to be saved and the part updated.
     *
     * @param event
     * @throws IOException
     */
    public void handleSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modPartIdTxt.getText());
            String name = modPartNameTxt.getText();
            int stock = Integer.parseInt(modPartInvTxt.getText());
            double price = Double.parseDouble(modCostTxt.getText());
            int max = Integer.parseInt(modMaxTxt.getText());
            int min = Integer.parseInt(modMinTxt.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Modify Part Error");
                alert.setContentText("The min value cannot be greater than the max value.");
                alert.showAndWait();
            } else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Modify Part Error");
                alert.setContentText("The current inventory level must be between the min and max values.");
                alert.showAndWait();
            } else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Modify Part Error");
                alert.setContentText("The part must have a valid name.");
                alert.showAndWait();
            } else {
                if (inHouse.isSelected()) {
                    int machID = Integer.parseInt(modMachineIdTxt.getText());
                    Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machID));
                } else if (Outsourced.isSelected()) {
                    String compName = modMachineIdTxt.getText();
                    Inventory.updatePart(id, new Outsourced(id, name, price, stock, min, max, compName));
                }
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Modify Part Error");
            alert.setContentText("Price, Inv, Max, Min, and Machine Id fields must contain number values to successfully modify the part.");
            alert.showAndWait();
        }
    }

    /**
     * confirms user wants to cancel the part modification process and returns them to main screen upon receiving confirmation
     * @param event
     * @throws IOException
     */
    public void handleCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Modifying Part");
        alert.setHeaderText("This will cancel modifying the part.");
        alert.setContentText("Do you wish to proceed with canceling the modification of this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
}

