package controller;
/**
 * Add Part Controller Class
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


/** Declares controller class addPart and initializes it.
 *
 */
public class addPartController implements Initializable {

    @FXML
    private static TextField addPartIdTxt;

    @FXML
    private TextField addPartNameTxt;

    @FXML
    private TextField addPartInvTxt;

    @FXML
    private TextField addPartCostTxt;

    @FXML
    private TextField addPartMaxTxt;

    @FXML
    private TextField addPartMinTxt;

    @FXML
    private Label addPartLabel;

    @FXML
    private TextField machineIdTxt;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ToggleGroup addPartToggle;

    @FXML
    private RadioButton inHouse;

    @FXML
    private RadioButton Outsourced;


    Stage stage;
    Parent scene;

    static int id;

    /**allows access to resourceBundle
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /** This method handles the add part forms cancel button
     * and upon confirmation of cancel by user
     * will take the user back to the main screen view.
     *
     * @param event
     * @throws IOException
     */
    public void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Part");
        alert.setHeaderText("This will cancel adding a new part.");
        alert.setContentText("Do you wish to proceed with canceling adding a new part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /** handles the actions in response to the save button to add a new part
     * generates errors in case user enters invalid information in the add part form
     * returns user to main screen once validation and part save occur.
     *
     *
     * @param event
     * @throws IOException
     */

    /**
     * RUNTIME_ERROR
     * Here I ran into both a logic error and a run time errorl
     * I initially had attempted to set the value for the addPartIdTxt field here.
     * This caused a runtime error because the value was being reported as null and crashing the program.
     * The logic error lies in the fact that id is not set in this method so calling it from here would always lead to a null value.
     * upon removing id line, the code fuctioned correctly and i was able to successfully add a part.
     */
    public void handleAddPart(ActionEvent event) throws IOException {
        try {
        String name = addPartNameTxt.getText();
        int inventory = Integer.parseInt(addPartInvTxt.getText());
        double costPerUnit = Double.parseDouble(addPartCostTxt.getText());
        int max = Integer.parseInt(addPartMaxTxt.getText());
        int min = Integer.parseInt(addPartMinTxt.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Add Part Error");
                alert.setContentText("The minimum value cannot exceed the maximum value.");
                alert.showAndWait();
            }
            else if (inventory > max || inventory < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Add Part Error");
                alert.setContentText("The inventory amount must be within the minimum and maximum values.");
                alert.showAndWait();
            }
            else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Add Part Error");
                alert.setContentText("The new part must have a valid name.");
                alert.showAndWait();
            }
            else {

                if (inHouse.isSelected()) {
                    int machineId = Integer.parseInt(machineIdTxt.getText());
                    InHouse addInhousePart = new InHouse( ++id, name, costPerUnit, inventory, min, max, machineId);
                    Inventory.addPart(addInhousePart);
                }
                if (Outsourced.isSelected()) {
                    String companyName = machineIdTxt.getText();
                   Part OutsourcedPart= new Outsourced ( ++id, name, costPerUnit, inventory, min, max, companyName);
                    Inventory.addPart(OutsourcedPart);
                    }
                }
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/mainScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Add Part Error");
            alert.setContentText("Price, Inv, Max, Min, and Machine ID values must contain number values to successfully add the new part.");
            alert.showAndWait();
        }
    }

    /** handles the radio button selection for Outsourced
     * changes form label to Company name
     */

    @FXML
    public void handleOutsourced(ActionEvent event) {addPartLabel.setText("Company Name");
    }

    /**
     * handles the radio button selection for an inhouse part
     * changes the label to Machine ID
     * The inhouse radio button is set to selected by default in the corresponding fxml.
     * @param event
     */
    @FXML
    public void handleInhouse(ActionEvent event) {
        addPartLabel.setText("Machine ID");
    }

    }

