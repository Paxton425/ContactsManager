/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package contacts.manager;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class ContactsApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Image favicon = new Image(new File("C:\\Users\\admin\\Documents\\NetBeansProjects\\Contacts Manager\\src\\contacts\\manager\\images\\telephone-icon.png").toURI().toString());
        
        Scene scene = new Scene(root);
        
        stage.setTitle(STYLESHEET_MODENA);
        stage.setTitle("Contacts Manager");
        stage.getIcons().add(favicon);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
