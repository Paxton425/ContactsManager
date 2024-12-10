/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package contacts.manager.ui.dialog;

import contacts.manager.Contact;
import contacts.manager.ContactBuilder;
import contacts.manager.ContactsManager;
import contacts.manager.ui.ContactsUI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Sphelelel
 */
public class FXMLEditContactController implements Initializable {
    
    @FXML
    private TextField editNameField;
    @FXML
    private TextField editNumberField;
    @FXML
    private TextField editEmailField;
    @FXML
    private TextField editStreetField;
    @FXML
    private TextField editCityField;
    @FXML
    private TextField editStateField;
    @FXML
    private TextField editZipCodeField;
    @FXML 
    private Button exitButton;
    
    private Contact currContact;
    private Contact newContact;
    private Stage parentStage;
    private Pane contactsListPane;
    
    @FXML
    private void handleExitWindowAction(ActionEvent Event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleSaveContactEditAction(ActionEvent event){
        if(veriFyInputs()){
            if(!newContact.equals(null)){
                try{
                    ContactsManager cManager = new ContactsManager();
                    cManager.UpdateContact(newContact);
                    
                    //Update Contacts List UI
                    if(!contactsListPane.equals(null)){
                    ObservableList children = contactsListPane.getChildren();
                        //Search from the vbox for upadated contact item with regex
                        for (int i=0; i<children.size(); i++){
                            Pattern pattern = Pattern.compile(currContact.Phone);
                            Matcher matcher = pattern.matcher((children.get(i)).toString());
                            if(matcher.find()){
                                ContactsUI cui =  new ContactsUI();
                                Button newChild = (Button)cui.getContactItem(newContact, contactsListPane);
                                contactsListPane.getChildren().remove(children.get(i));
                                contactsListPane.getChildren().add(i , newChild);
                            
                                Stage stage = (Stage) exitButton.getScene().getWindow();
                                Stage pStage = parentStage;
                                stage.close();
                                pStage.close();
                    
                                ContactCustomDialog dialog = new ContactCustomDialog(newContact);
                                dialog.showDialog(contactsListPane, newChild);
                                showAlert("Changes Saved SuccessFully", Alert.AlertType.INFORMATION);
                            }
                        }
                    }
                    
                } catch(Exception e){
                    showAlert("Something Went Wrong While Saving Changes!", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        }
    }
    
    @FXML
    private void handleCancelEditAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String message, AlertType type){
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.show();
    }
    
    private boolean veriFyInputs(){
        String errorMessage = "";   //Validate Fields Inputs
        
        if(editNameField.getText().isEmpty() || editNumberField.getText().isEmpty())
            errorMessage = "You Must Enter all Compulsory Fields";
        else if (!isOnlyDigits(editNumberField.getText()))
            errorMessage = "Phone number must contain digits only";
        else if (!isOnlyDigits(editZipCodeField.getText()))
            errorMessage = "Zip code must contain digits only";
        else if(!editEmailField.getText().contains("@"))
            errorMessage = "You have entered an incorrect email format";
            
        if(!errorMessage.isEmpty()){
            showAlert(errorMessage, Alert.AlertType.ERROR);
            
            return false;
        }else{
            ContactBuilder cBuilder = new ContactBuilder();
            cBuilder.setId(currContact.Id);
            cBuilder.setName(editNameField.getText());
            cBuilder.setPhone(editNumberField.getText());
            cBuilder.setEmail(editEmailField.getText());
            cBuilder.setStreet(editStreetField.getText());
            cBuilder.setCity(editCityField.getText());
            cBuilder.setState(editStateField.getText());
            cBuilder.setZipCode(editZipCodeField.getText());
            this.newContact = cBuilder.getContact();
            
            return true;
         }
    }
    
    public void setParentStage(Stage stage){
        this.parentStage = stage;
    }
    
    public void setContactsListPane(Pane pane){
        if(pane.getClass().getTypeName() == "javafx.scene.layout.VBox"){
            this.contactsListPane = (VBox)pane;
        } else{
            showAlert("Error updating contact list(Pane type mismatch)", Alert.AlertType.ERROR);
        }
    }
    
    public void setCurrentFields(Contact contact){
        this.currContact = contact;
        
        //Populate Text Fields
        editNameField.setText(contact.Name);
        editNumberField.setText(contact.Phone);
        editEmailField.setText(contact.Email);
        editStreetField.setText(contact.Street);
        editCityField.setText(contact.City);
        editStateField.setText(contact.State);
        editZipCodeField.setText(contact.ZipCode);
    }
    
    private static boolean isOnlyDigits(String str) 
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
            
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        exitButton.setOnMouseEntered(event ->{
            exitButton.setStyle("-fx-background-color: #FF0000;");
            exitButton.textFillProperty().set(Paint.valueOf("#FFFFFF"));
        });
        exitButton.setOnMouseExited(event ->{
            exitButton.textFillProperty().set(Paint.valueOf("#000000"));
            String cssfx = ("-fx-background-color: #FFFFFF; -fx-border-color: #D6D6D6;");
            exitButton.setStyle(cssfx);
        });
    }    
    
}
