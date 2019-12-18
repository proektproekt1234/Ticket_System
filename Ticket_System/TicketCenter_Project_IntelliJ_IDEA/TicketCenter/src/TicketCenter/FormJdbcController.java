package TicketCenter;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormJdbcController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField textFieldHostName, textFieldPort, textFieldOracleSID;
    @FXML
    private TextField textFieldUserName;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Button buttonSave, buttonExit, buttonDefaultSettings, buttonTestConnection;

    @FXML
    void initialize() {

        if (TVClass.getRegistrySettings()){
            // т.е. имаме съхранени настройки в Registry базата
            textFieldHostName.setText(TVClass.hostName);
            textFieldPort.setText(TVClass.oraclePort);
            textFieldOracleSID.setText(TVClass.oracleSID);
            textFieldUserName.setText(TVClass.oracleUserName);
            textFieldPassword.setText(TVClass.oraclePassWord);
        }
        else {
            textFieldHostName.setText("localhost");
            textFieldPort.setText("1521");
            textFieldOracleSID.setText("xe");
            textFieldUserName.setText("todor");
            textFieldPassword.setText("vasilev");
        }

        // Бутон за сетване на настройките по подразбиране
        buttonDefaultSettings.setOnAction(event -> {
            textFieldHostName.setText("localhost");
            textFieldPort.setText("1521");
            textFieldOracleSID.setText("xe");
            textFieldUserName.setText("todor");
            textFieldPassword.setText("vasilev");
        });

        // Тестване JDBC връзката към Oracle базата
        buttonTestConnection.setOnAction(event -> {
            String ans = TVClass.testJdbcConnection(textFieldHostName.getText(), textFieldPort.getText(),
                        textFieldOracleSID.getText(), textFieldUserName.getText(), textFieldPassword.getText());
            if(ans.equals("ok")){
                TVClass.showAlertInformation("Билетен център","Успешен тест на връзката.");
            }
            else {
                TVClass.showAlertError("Внимание!",
                        "Проблем при тестване на връзката с описание: " + ans);
            }
        });

        // Съхраняваме настройките на JDBC връзката в Registry базата
        buttonSave.setOnAction(event -> {
            if(textFieldHostName.getText().isEmpty()){
                TVClass.showAlertError("Внимание!","Не сте въвели стойност за Host.");
                textFieldHostName.requestFocus();
                return; // ИЗХОД
            }
            if(textFieldPort.getText().isEmpty()){
                TVClass.showAlertError("Внимание!","Не сте въвели стойност за Port.");
                textFieldPort.requestFocus();
                return; // ИЗХОД
            }
            if(textFieldOracleSID.getText().isEmpty()){
                TVClass.showAlertError("Внимание!","Не сте въвели SID на Oracle Instance.");
                textFieldOracleSID.requestFocus();
                return; // ИЗХОД
            }
            if(textFieldUserName.getText().isEmpty()){
                TVClass.showAlertError("Внимание!","Не сте въвели име на потребител.");
                textFieldUserName.requestFocus();
                return; // ИЗХОД
            }
            if(textFieldPassword.getText().isEmpty()){
                TVClass.showAlertError("Внимание!","Не сте въвели парола.");
                textFieldPassword.requestFocus();
                return; // ИЗХОД
            }

            TVClass.setRegistrySettings(textFieldHostName.getText(), textFieldPort.getText(), textFieldOracleSID.getText(),
                                        textFieldUserName.getText(), textFieldPassword.getText());

            Stage thisStage = (Stage)buttonSave.getScene().getWindow();
            TVClass.showAlertInformation("Билетен център","Успешен запис на данните.");
            thisStage.hide();
        });

        // Изход от формата
        buttonExit.setOnAction(event -> {
            Stage thisStage = (Stage)buttonExit.getScene().getWindow();
            thisStage.hide();
        });

    }

}
