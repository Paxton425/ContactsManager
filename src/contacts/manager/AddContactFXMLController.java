/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package contacts.manager;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class AddContactFXMLController implements Initializable {
    
    @FXML
    private TextField addNameField;
    @FXML
    private TextField addNumberField;
    @FXML
    private TextField addEmailField;
    @FXML 
    private TextField addStreetField;
    @FXML
    private TextField addCityField;
    @FXML
    private TextField addStateField;
    @FXML
    private TextField addZipCodeField;
    @FXML
    private Button ExitButton;

    // Define an event handler for the Save button click
    private EventHandler<ActionEvent> onSaveButtonClick;
    private Contact contact = null;
    
    // Set the event handler for the Save button click
    public void setOnSaveButtonClick(EventHandler<ActionEvent> eventHandler) {
        onSaveButtonClick = eventHandler;
    }
    
    @FXML
    private void handleExitWindowAction(ActionEvent event){
        System.out.println("Exiting");
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleSaveContactAction(ActionEvent event){
        
        String errorMessage = "";
        if (onSaveButtonClick != null) {
            
            //Validate Fields Inputs
            if(addNameField.getText().isEmpty() || addNumberField.getText().isEmpty())
                errorMessage = "You Must Enter all Compulsory Fields";
            else if (!isOnlyDigits(addNumberField.getText()))
                errorMessage = "Phone number must contain digits only";
            else if (!isOnlyDigits(addZipCodeField.getText()))
                errorMessage = "Zip code must contain digits only";
            else if(!addEmailField.getText().contains("@"))
                errorMessage = "You have entered an incorrect email format";
            
            if(!errorMessage.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setContentText(errorMessage);
                alert.show();
            }
            else {
                ContactBuilder contactBuilder = new ContactBuilder();
                contactBuilder.setName(addNameField.getText().trim());
                contactBuilder.setPhone(addNumberField.getText().trim());
                contactBuilder.setEmail(addEmailField.getText().trim().toLowerCase());
                contactBuilder.setStreet(addStreetField.getText().trim());
                contactBuilder.setCity(addCityField.getText().trim());
                contactBuilder.setState(addStateField.getText().trim());
                contactBuilder.setZipCode(addZipCodeField.getText().trim());
                contact = contactBuilder.getContact();
                System.out.println(contact);
                
                onSaveButtonClick.handle(event);// Trigger the event
            }
        }
    }
    
    public static boolean isOnlyDigits(String str) 
    { 
        // Traverse the string from start to end 
        for (char c : str.toCharArray()) { 
            // Check if character is not a digit between 0-9 then return false
            if (Character.isDigit(c) || c == '-' || c == '+') continue;
            else {
                System.out.println(Character.isDigit(c));
                return false;
            } 
        } 
          // If we reach here, that means all characters were digits.
        return true; 
    } 
    
    public Contact getContact() throws NullPointerException{
        return contact;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ExitButton.setOnMouseEntered(event ->{
            ExitButton.setStyle("-fx-background-color: #FF0000;");
            ExitButton.textFillProperty().set(Paint.valueOf("#FFFFFF"));
        });
        ExitButton.setOnMouseExited(event ->{
            
            ExitButton.textFillProperty().set(Paint.valueOf("#000000"));
            String cssfx = ("-fx-background-color: #FFFFFF; -fx-border-color: #D6D6D6;");
            ExitButton.setStyle(cssfx);;
        });
    }     
}
