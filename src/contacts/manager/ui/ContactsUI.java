/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contacts.manager.ui;

import contacts.manager.Contact;
import contacts.manager.ui.dialog.ContactCustomDialog;
import contacts.manager.ui.dialog.CustomDialog;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 *
 * @author Sphelele
 */
public class ContactsUI {
    private Contact contact;
    
    public void setContat(Contact contact){
        this.contact = contact;
    }
    //Button to be created each time a new contact is added.
    public Button getContactItem(Contact contact, Pane parent) {
        this.contact = contact;
        
        String buttonText = (contact.Name+"\n "+contact.Phone);
        Button contactItem = new Button(buttonText);
        contactItem.prefWidthProperty().bind(parent.widthProperty());
        contactItem.setStyle("-fx-background-color: #FFF; -fx-border-color: #D6D6D6;");
        contactItem.setAlignment(Pos.BASELINE_LEFT);
        contactItem.setTextFill(Paint.valueOf("#5e5e5e"));
        contactItem.setPrefHeight(100);

        // Change color on mouse enter
        contactItem.setOnMouseEntered(e -> {
        contactItem.setStyle("-fx-background-color: #e3e6e8; -fx-border-color: #D6D6D6;");
        
        });

        // Revert to original color on mouse exit
        contactItem.setOnMouseExited(e -> {
            contactItem.setStyle("-fx-background-color: #FFF; -fx-border-color: #D6D6D6;");
        });
    
        contactItem.setOnMouseClicked(e -> {
            ContactCustomDialog dialog = new ContactCustomDialog(contact);
            //CustomDialog dialog = new CustomDialog("FXMLContactDialog.fxml", "Contact");
            dialog.showDialog(parent, contactItem);
        });

        return contactItem;
    }
    
    public Label getTopLabel(String text){
        Label label = new Label("text");
        label.setAlignment(Pos.CENTER);
        label.setPrefHeight(17.0);
        return label;
    }
    
    public void displayContacts(Contact[] contacts){
    }
}
