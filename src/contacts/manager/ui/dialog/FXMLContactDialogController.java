/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package contacts.manager.ui.dialog;

import contacts.manager.Contact;
import contacts.manager.ContactsManager;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.font.TextLabel;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FXMLContactDialogController<T> implements Initializable {
    @FXML
    private Text nameInfo;
    @FXML
    private Text phoneInfo;
    @FXML 
    private Text emailInfo;
    @FXML
    private Text streetInfo;
    @FXML 
    private Text cityInfo;
    @FXML
    private Text stateInfo;
    @FXML
    private Text zipcodeInfo;
    @FXML
    private Button exitButton;
    
    private Stage stage;
    private Contact contact;
    private Pane parent;
    private Button triggerNode;
    
    @FXML
    private void handleExitWindowAction(ActionEvent Event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleEditContactAction(ActionEvent Event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEditContact.fxml"));
            Parent root = loader.load();
            FXMLEditContactController editContactController = loader.getController();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Contact");
            stage.show();
            
            
            editContactController.setCurrentFields(contact);
            editContactController.setContactsListPane(parent);
            editContactController.setParentStage((Stage) exitButton.getScene().getWindow());
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteContactAction(ActionEvent Event){
        //Confirm To User
        boolean proceed = deleteConfirmation(contact.Name);
        if(proceed){
            
            ContactsManager manager = new ContactsManager();
            manager.DeleteContactNode(contact);
            
            if(parent != null && stage != null){
                if(parent.getClass().getTypeName() == "javafx.scene.layout.VBox"){
                    parent = (VBox) parent;
                    ObservableList children = parent.getChildren();
                    //Search the vbox items list with regex
                    for (Object c : children){
                        Pattern pattern = Pattern.compile(contact.Phone);
                        Matcher matcher = pattern.matcher(c.toString());
                        if(matcher.find()){
                            parent.getChildren().remove(c);//Remove matched item
                        
                            //Close Contact Window
                            stage.close();
                
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Contact Deleted");
                            alert.show();
                            break;
                        }
                    }
                }
                else 
                showErrorAlert();
            } 
            else 
                showErrorAlert();
        }
    }
    
    
    private boolean deleteConfirmation(String itemName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Delete "+itemName+" From Contacts?");
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UNDECORATED);
        
        ButtonType Yes = ButtonType.YES;
        ButtonType No = ButtonType.NO;
        
        alert.getButtonTypes().setAll(Yes, No);
        
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES; //Wait for user to click yes
    }
    
    private void showErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Something Went Wrong");
        alert.show();
    }
    
    public void setDialogParent(Pane parent){
        this.parent = parent;
    }
    
    private void setTriggerNode(Button buttonNode){
        this.triggerNode = buttonNode;
    }
    
    public void setDialogContact(Contact contact){
        this.contact = contact;
        
        nameInfo.setText(contact.Name);
        phoneInfo.setText(contact.Phone);
        emailInfo.setText(contact.Email);
        streetInfo.setText(contact.Street);
        cityInfo.setText(contact.City);
        stateInfo.setText(contact.State);
        zipcodeInfo.setText(contact.ZipCode);
    }
    
    public void setDialogStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        exitButton.setOnMouseEntered(event ->{
            exitButton.setStyle("-fx-background-color: #FF0000; -fx-broder-color: #FF0000");
        });
        
        exitButton.setOnMouseExited(event ->{
            String cssfx = "-fx-background-color: #139cf7; -fx-border-color: #139cf7;";
            exitButton.setStyle(cssfx);
        });
    }        
}
