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
import java.util.regex.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import javafx.scene.shape.Rectangle;
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
    private AnchorPane contactsPane;
    @FXML
    private ScrollBar contactsScrollBar;
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
    @FXML
    private Pane clipPane;
    
    private int climit = 100;
    private ContactsManager contactsManager = new ContactsManager(climit);
    private Contact[] contacts = contactsManager.getContacts();
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
            showAlert("Couldn't find contacts matching "+sName, Alert.AlertType.ERROR);
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
            if(contacts != null){
                //Contact list to display on app
                for(int i=0; i<contactsManager.getContactsCount(); i++) 
                {
                    contactsListVBox
                            .getChildren()
                            .add(i, contactsUI.getContactItem(contacts[i], contactsListVBox));
                }
            }
            else if(contactsManager.getContactsCount() == 0)
            {
                contactsUI.getStateMessage("0 contacts found");
            } 
        } catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("Something went wrong while loading contacts!");
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
            else 
                contactsUI.getStateMessage("0 contacts found");
        }
        catch(NullPointerException e)
        {
            System.out.println("No contacts Found!");
            contactsUI.getStateMessage("No matches found");
        }
   }
    
    int findSubstring(String fullString, String subString){
        int m = fullString.length();
        int n = subString.length();
        
        for(int i=0; i<=m-n; i++){
            
            //Search for substring match
            int j;
            for(j=0; j<n; j++) 
                //Missmatch Found
                if(fullString.charAt(i+j) != subString.charAt(j))
                    break;
                
            //When inner loop finishes, we found match
            if(j == n) 
                return i; //Return substring starting index
        }
        
        return -1; //Substring does not match
    }
    
    Contact[] filterSearchContact(String input){
        Contact[] matches = new Contact[climit];
        int matchCount =0;
        
        if(contacts == null){
            return null;
        }
        
        for(int i=0; i<contactsManager.getContactsCount(); i++)
        {
            try {
                if(contacts[i].Name == null) 
                    break;
                else if(findSubstring(contacts[i].Name.toLowerCase(), input.toLowerCase()) > -1) {
                
                    System.out.println("match"+contacts[i].Name);
                    matches[matchCount] = contacts[i]; 
                    matchCount++;
                }
            }catch(NullPointerException e){
                System.out.println("No match");
                return null;
            }
        }
        return matches;
    }
    
    private void configureScrollAction(){
        //Clip Display Content With Rectangle
        Rectangle clipRect = new Rectangle();
        contactsListVBox.prefWidthProperty().bind(clipPane.widthProperty());
        clipRect.widthProperty().bind(clipPane.widthProperty());
        clipRect.heightProperty().bind(clipPane.heightProperty());
        clipPane.setClip(clipRect);
        
        //Calc Dimensions
        Platform.runLater(() -> {//Wait for scene graph to be laid out
            double clipViewH = clipRect.heightProperty().get();
            System.out.println(clipViewH);
            double contactsViewH = contactsListVBox.heightProperty().getValue();
            System.out.println(contactsViewH);
            double vsisibleAmount = contactsScrollBar.getVisibleAmount();
        
            double percentageIncrease = ((contactsViewH-clipViewH)/clipViewH)*100;
            System.out.println(percentageIncrease+"%");
            double newVisibleAmount =  percentageIncrease > 0? vsisibleAmount-percentageIncrease: vsisibleAmount;
        
            contactsScrollBar.visibleAmountProperty().set(newVisibleAmount);

            contactsScrollBar.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                    double scrollValue = newValue.doubleValue();
                    double oldScrollValue = oldValue.doubleValue();
                    double scrollDelta = scrollValue - oldScrollValue;

                    // Calculate the total scrollable distance
                    double totalScrollableDistance = contactsListVBox.getHeight() - clipViewH;

                    // Calculate the relative change in layoutY
                    double layoutDelta = (scrollDelta / contactsScrollBar.getMax()) * totalScrollableDistance;

                    // Update the layoutY value
                    double newLayoutY = contactsListVBox.getLayoutY() - layoutDelta;
                    contactsListVBox.setLayoutY(newLayoutY);
                }
            });
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        configureScrollAction();
        loadContacts();
        
        contactSearchBox.setOnKeyTyped(event -> {
            if(!(contactSearchBox.getText().trim()).matches("")){
                searchButton.setDisable(false);
                
                Contact[] clist = filterSearchContact(contactSearchBox.getText().trim());
                loadList(clist);
            } 
            else {
                loadContacts();
                searchButton.setDisable(true);
            }
        });
        
        
    }    
    
}
