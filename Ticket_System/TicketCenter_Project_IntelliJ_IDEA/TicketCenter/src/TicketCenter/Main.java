package TicketCenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FormMain.fxml"));
        primaryStage.setTitle("Билетен център");
        primaryStage.setScene(new Scene(root, 602, 302));
        primaryStage.setResizable(false);
        primaryStage.show();
        if(!TVClass.getRegistrySettings()){
            TVClass.showAlertInformation("Внимание!", "Настройте първо връзката към Oracle базата!\n\n" +
                                            "Влезте в меню \"Система\"\\\"Връзка към Oracle базата\"");
        }; // зареждаме настройките на приложението
    }


    public static void main(String[] args) {
        launch(args);
    }
}
