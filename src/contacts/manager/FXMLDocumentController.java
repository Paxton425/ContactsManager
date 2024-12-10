/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package contacts.manager;

import contacts.manager.ui.ContactsUI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.*;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.xml.sax.SAXException;

/**
 *noContactsLabel
    * @author Sphelele
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField contactSearchBox;
    @FXML 
    private ScrollPane contactsScrollPane;
    @FXML
    private Pane contactsPane;
    @FXML 
    private AnchorPane rootAchorPane;
    @FXML
    private SplitPane mainSplitPain;
    @FXML
    private VBox sideBarVBox;
    @FXML
    private Pane seccondParentPane;
    @FXML
    private VBox contactsListVBox;
    @FXML
    private Button searchButton;
    
    private ContactsManager contactsManager = new ContactsManager(100);
    private ContactsUI contactsUI = new ContactsUI();
    
    @FXML
    private void handleSearchAction(ActionEvent event) {
        String sName = contactSearchBox.getText();
        try{
            Contact sResultContact = contactsManager.searchContact(sName);
            
            if(sResultContact != null){
                contactsListVBox.getChildren().clear();
                contactsListVBox.getChildren()
                            .add(0, contactsUI
                            .getContactItem(sResultContact, contactsListVBox));
            }
            else showAlert("Couldn't find contacts matching "+sName, Alert.AlertType.ERROR);
        }
        catch(NullPointerException e)
        {
            System.out.println("No contacts Found!");
            showAlert("Couldn't fin contacts matching "+sName, Alert.AlertType.ERROR);
        }
    }
    
    private void trackWidth(double with){
        System.out.println(with);
    }
    
    private void showLabelMessage(String message){
        try{
            Label label = contactsUI.getTopLabel(message);
            contactsListVBox.getChildren().add(0, label);
        }catch(IndexOutOfBoundsException e){
            System.out.println("LABEL OUT OF BOUNDS!");
        }
    }
    
    @FXML
    private void handleAddContactAction(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddContactFXML.fxml"));
            Parent root = loader.load();
            
            //Access the controller to perfom futher actions
            AddContactFXMLController addContactController = loader.getController();
            
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(new Scene(root));
            stage.setTitle("Add Contact");
            stage.show();
            
            addContactController.setOnSaveButtonClick(e -> {
                // Perform actions in the main window when the Save button is clicked
                Contact contact = addContactController.getContact();
                
                if(contactsManager.getContactsCount() == 0)
                    contactsListVBox.getChildren().clear();
                addNewContact(contact);
                stage.close(); //Close the window
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setContentText("Contact Saved!");
                alert.setWidth(100);
                alert.show();
            });
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void addNewContact(Contact contact){
        try {
            int index = (contactsManager.getContactsCount() > 0)? (contactsManager.getContactsCount()-1) : 0;
            contactsManager.AddContact(
                    contact.Name, 
                    contact.Phone, 
                    contact.Email, 
                    contact.Street,
                    contact.City, 
                    contact.State,
                    contact.ZipCode);
            contactsListVBox
                    .getChildren()
                    .add(index, contactsUI.getContactItem(contact, contactsListVBox));
        } catch (SAXException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadContacts(){
        try
        {
            if(contactsManager.getContactsCount() > 0){
                Contact[] contactsList = contactsManager.getContacts();
                
                //Contact list to display on app
                for(int i=0; i<contactsManager.getContactsCount(); i++) 
                {
                    contactsListVBox
                            .getChildren()
                            .add(i, contactsUI.getContactItem(contactsList[i], contactsListVBox));
                }
            }
            else if(contactsManager.getContactsCount() == 0)
            {
                showLabelMessage("No Contacts");
            } 
        } catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("Something went wrong while starting app!");
            e.printStackTrace();
        }
    }
    
    private void showAlert(String message, AlertType type){
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.show();
    }
    
    void loadList(Contact[] contacts){
       try{
            if(contacts != null){
                contactsListVBox.getChildren().clear();
                for(int i=0; i<contacts.length; i++)
                    contactsListVBox.getChildren()
                            .add(i, contactsUI
                            .getContactItem(contacts[i], contactsListVBox));
            }
        }
        catch(NullPointerException e)
        {
            System.out.println("No contacts Found!");
            showAlert("Loading Contacts Went wrong ", Alert.AlertType.ERROR);
        }
   }
    Contact[] filterSearchContact(String name){
        Contact[] contactsList = contactsManager.getContacts();
        
        int matchCount =0;
        Contact[] matches = new Contact[limit];
        for(int i=0; i<contacts.length; i++)
        {
            if(contacts[i].Name.toLowerCase().matches(name.toLowerCase())) {
                matches[matchCount] = contacts[i]; 
                matchCount++;
            }
        }
        return matches;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Binding pane sizes
        contactsPane.prefHeightProperty().bind(contactsScrollPane.heightProperty());
        contactsPane.prefWidthProperty().bind(contactsScrollPane.widthProperty());
        contactsListVBox.prefWidthProperty().bind(contactsScrollPane.widthProperty());
        contactsListVBox.prefHeightProperty().bind(contactsScrollPane.heightProperty());
        
        trackWidth(rootAchorPane.widthProperty().getValue());
        loadContacts();
        
        contactSearchBox.setOnKeyTyped(event -> {
            if((contactSearchBox.getText().trim()).matches("")){
                searchButton.setDisable(true);
                
                Contact[] clist = contactsManager.filterSearchContact(contactSearchBox.getText());
                loadList(clist);
            }
            else 
                searchButton.setDisable(false);
        });
        
        
    }    
    
}
