/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contacts.manager.ui.dialog;

import contacts.manager.Contact;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author admin
 */
public class ContactCustomDialog {
    private Contact contact;
    private Stage dialogStage;
    
    public ContactCustomDialog(Contact contact){
        this.contact = contact;
    }
    
    public void closeDialog(){
        dialogStage.close();
    }
    
    public void showDialog(Pane parent, Button targertNode){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLContactDialog.fxml"));
            Parent root = loader.load();
            
            //Get Accesss to Controller
            FXMLContactDialogController dialogController = loader.getController();
            dialogController.setDialogContact(contact);
            dialogController.setDialogParent(parent);
            
            dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.setTitle("Contact");
            dialogStage.show();
            dialogController.setDialogStage(dialogStage);
            
        }catch(IOException ex){
            ex.printStackTrace();
        } catch(NullPointerException ex){
            System.out.println("Null Contact");
        }
    }
}
