/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package contacts.manager.ui.dialog;

import contacts.manager.Contact;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author admin
 */
public class CustomDialog {
    private String title;
    private String fxmlFilePath;
    private Stage stage;
    
    public CustomDialog(String title, String fxmlFilePath){
        this.fxmlFilePath = fxmlFilePath;
        this.title = title;
    }
    
    public void setDialogContext(){}
    
    public void showDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();
            
            //Get Accesss to Controller
            FXMLContactDialogController dialogController = loader.getController();
            
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
