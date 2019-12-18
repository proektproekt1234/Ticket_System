package TicketCenter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FormMainController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private MenuBar menuBarMain;
    @FXML
    private Menu menuAdmin, menuOrganiser, menuDistributor;
    @FXML
    private MenuItem menuItemLogin, menuItemOracleConn, menuItemExit, menuItemAdminUsers,
                    menuItemOrganiserEvents, menuItemDistributorEvents, menuItemAbout;
    @FXML
    private Label labelLoginInfo;
    @FXML
    private Button buttonLogin;

    @FXML
    void initialize() {

        // Изход от приложението
        menuItemExit.setOnAction(event -> {
            System.exit(0); // ИЗХОД ОТ ПРИЛОЖЕНИЕТО
        });

        // Информация за приложението
        menuItemAbout.setOnAction(event -> {
            TVClass.showAlertInformation("Информация за приложението","Курсова работа \"Билетен център\" \n\n" +
                    "Работа с JavaFX 8 и Oracle DataBase \n\n" +
                    "IntelliJ IDEA Community Edition 2019.2.4"); // ИЗХОД ОТ ПРИЛОЖЕНИЕТО
        });

        buttonLogin.setOnAction(event -> {
            this.sysLogin();
        });

        menuItemLogin.setOnAction(event -> {
            this.sysLogin();
        });

        menuItemOracleConn.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FormJdbc.fxml"));
            Parent content = null;
            try {
                content = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("JDBC връзка към Oracle базата");
            secondaryStage.setScene(new Scene(content, 400, 168));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setResizable(false);
            secondaryStage.show();
        });

        // меню за АДМИНИСТРАТОРИ
        menuItemAdminUsers.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FormUsers.fxml"));
            Parent content = null;
            try {
                content = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Администриране на потребители");
            secondaryStage.setScene(new Scene(content, 450, 340));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setResizable(false);
            secondaryStage.show();
        });

        // меню за ОРГАНИЗАТОРИ на събития
        menuItemOrganiserEvents.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FormEvents.fxml"));
            Parent content = null;
            try {
                content = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Администриране на събития");
            secondaryStage.setScene(new Scene(content, 534, 462));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setResizable(false);
            secondaryStage.show();
        });

        // меню за ДИСТРИБУТОРИ на билети
        menuItemDistributorEvents.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FormDistributors.fxml"));
            Parent content = null;
            try {
                content = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Продажба на билети");
            secondaryStage.setScene(new Scene(content, 528, 376));
            secondaryStage.initModality(Modality.APPLICATION_MODAL);
            secondaryStage.setResizable(false);
            secondaryStage.show();
        });

    }

    private void sysLogin() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FormLogin.fxml"));
        Parent content = null;
        try {
            content = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Вписване в системата");
        secondaryStage.setScene(new Scene(content, 246, 118));
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setResizable(false);
        secondaryStage.showAndWait();
        // ТУК ПРОВЕРЯВАМЕ ПРОМЕНЕНА ГЛОБАЛНА ПРОМЕНЛИВА ОТ FormLogin формата
        if(!TVClass.successfullyLoggedIn){
            return; // Изход, при неуспешно логване
        }
        labelLoginInfo.setText("Потребител  >>  " + TVClass.user_names);
        buttonLogin.setVisible(false);

        switch (TVClass.user_role){
            case "0":  // Администратор
                menuAdmin.setVisible(true);
                menuOrganiser.setVisible(false);
                menuDistributor.setVisible(false);
                break;
            case "1":  // Организатор
                menuOrganiser.setVisible(true);
                menuAdmin.setVisible(false);
                menuDistributor.setVisible(false);
                break;
            case "2":  // Дистрибутор (Касиер)
                menuDistributor.setVisible(true);
                menuOrganiser.setVisible(false);
                menuAdmin.setVisible(false);
                break;
        }

    };


}
