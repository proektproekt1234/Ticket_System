package TicketCenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class FormLoginController {

    @FXML
    private TextField textFieldUserName;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Button buttonOK, buttonCancel;

    @FXML
    void initialize() {

        // Логин
        buttonOK.setOnAction(event -> {
            this.loginMethod();
        });

        textFieldUserName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                if(textFieldUserName.getText().isEmpty()){
                    TVClass.showAlertError("Внимание!","Не сте въвели потребителско име.");
                    textFieldUserName.requestFocus();
                    return; // ИЗХОД
                }
                if(textFieldPassword.getText().isEmpty()){
                    TVClass.showAlertError("Внимание!","Не сте въвели парола.");
                    textFieldPassword.requestFocus();
                    return; // ИЗХОД
                }
                this.loginMethod();
            }
        });

        textFieldPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                if(textFieldUserName.getText().isEmpty()){
                    TVClass.showAlertError("Внимание!","Не сте въвели потребителско име.");
                    textFieldUserName.requestFocus();
                    return; // ИЗХОД
                }
                if(textFieldPassword.getText().isEmpty()){
                    TVClass.showAlertError("Внимание!","Не сте въвели парола.");
                    textFieldPassword.requestFocus();
                    return; // ИЗХОД
                }
                this.loginMethod();
            }
        });

        // Изход от формата
        buttonCancel.setOnAction(event -> {
            Stage thisStage = (Stage) buttonCancel.getScene().getWindow();
            thisStage.hide();
        });

    }

    private void loginMethod(){
        // Първо тестваме връзката към Oracle базата
        String ans = TVClass.testJdbcConnection(TVClass.hostName, TVClass.oraclePort, TVClass.oracleSID,
                TVClass.oracleUserName, TVClass.oraclePassWord);
        if(!ans.equals("ok")){
            TVClass.showAlertError("Внимание!","Проблем при тестване на връзката с описание: " + ans);
            return; // Изход
        }
        // Автентикация на потребителя
        if(!TVClass.sysLogin(textFieldUserName.getText(), textFieldPassword.getText()).equals("ok")) {
            return; // Изход
        }
        Stage thisStage = (Stage) buttonOK.getScene().getWindow();
        thisStage.hide();
    }

}
