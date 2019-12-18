package TicketCenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormUsersController {

    @FXML
    private Button buttonSearch, buttonNewUser, buttonSave, buttonCancel, buttonDeleteUser, buttonExit;
    @FXML
    private TextField textFieldSearch, textFieldUserNames, textFieldLoginName, textFieldRating, textFieldNote;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private ComboBox<String> comboBoxUserRole, comboBoxUserStatus;

    @FXML
    void initialize() {

        comboBoxUserRole.getItems().addAll("Администратор","Организатор", "Разпространител");
        comboBoxUserStatus.getItems().addAll("Активен","Смяна на паролата", "Деактивиран");

        // ТЪРСЕНЕ на съществуващ ПОТРЕБИТЕЛ
        buttonSearch.setOnAction(event -> {
            if(textFieldSearch.getText().isEmpty()){
                TVClass.showAlertError("Внимание!", "Не сте задали критерии за търсене.");
                textFieldSearch.requestFocus();
                return; // Изход
            }
            String ans = TVClass.searchUsers(textFieldSearch.getText());
            if(ans.equals("no_data_found")){
                TVClass.showAlertError("Внимание!","Не е намерен потребител с тези данни.");
                this.lockAndClearControls();
                return; // изход
            } else if (ans.equals("ok"))
                this.unlockControls();
            else {
                TVClass.showAlertError("Внимание!","Проблем при търсене с описание: " + ans);
                this.lockAndClearControls();
                return; // изход
            }

            textFieldUserNames.setText(TVClass.user_names);
            textFieldLoginName.setText(TVClass.user_login_name);
            textFieldPassword.setText(TVClass.user_pass);
            textFieldRating.setText(TVClass.user_rating);
            textFieldRating.setDisable(true);
            textFieldNote.setText(TVClass.user_note);
            switch (TVClass.user_role) {
                case "0":
                    comboBoxUserRole.setValue("Администратор");
                    break;
                case "1":
                    comboBoxUserRole.setValue("Организатор");
                    break;
                case "2":
                    comboBoxUserRole.setValue("Разпространител");
                    break;
            }
            switch (TVClass.user_status) {
                case "00":
                    comboBoxUserStatus.setValue("Активен");
                    break;
                case "01":
                    comboBoxUserStatus.setValue("Смяна на паролата");
                    break;
                case "02":
                    comboBoxUserStatus.setValue("Деактивиран");
                    break;
            }
        });

        // Бутон ОТКАЗ
        buttonCancel.setOnAction(event -> {
            TVClass.user_id = null; // изчистваме търсенето
            this.lockAndClearControls();
        });

        // Бутон ИЗТРИВАНЕ на ПОТРЕБИТЕЛ
        buttonDeleteUser.setOnAction(event -> {
            if(TVClass.user_id == null){
                TVClass.showAlertError("Внимание!", "Не сте избрали потребител.");
                return; // Изход
            }
            if(TVClass.user_login_name.equals("Admin")){
                TVClass.showAlertError("Внимание!", "Не можете да изтриете администратора Admin.");
                return; // Изход
            }
            if(TVClass.showAlertYesNo("Внимание!",
                    "Сигурни ли сте, че искате да изтриете потребителя " + TVClass.user_names).equals("no")) {
                return; // Изход
            }
            String ans = TVClass.deleteUser(TVClass.user_id);
            if(ans.equals("ok")){
                TVClass.showAlertInformation("Билетен център", "Потребителят е изтрит.");
                this.lockAndClearControls();
            } else {
                TVClass.showAlertInformation("Внимание!",
                        "Проблем при изтриване на потребителя с описание: " + ans);
            }
        });

        // Бутон ЗАПИС
        buttonSave.setOnAction(event -> {
            if(textFieldUserNames.getText().isEmpty() || textFieldLoginName.getText().isEmpty() ||
                    textFieldPassword.getText().isEmpty() || comboBoxUserRole.getValue().isEmpty() ||
                    comboBoxUserStatus.getValue().isEmpty()){
                TVClass.showAlertError("Внимание!", "Не сте въвели всички данни за потребителя.");
                return; // изход
            }
            TVClass.user_names = textFieldUserNames.getText();
            TVClass.user_login_name = textFieldLoginName.getText();
            TVClass.user_pass = textFieldPassword.getText();
            TVClass.user_rating = textFieldRating.getText();
            TVClass.user_note = textFieldNote.getText();
            switch (comboBoxUserRole.getValue()) {
                case "Администратор":
                    TVClass.user_role = "0";
                    break;
                case "Организатор":
                    TVClass.user_role = "1";
                    break;
                case "Разпространител":
                    TVClass.user_role = "2";
                    break;
            }
            switch (comboBoxUserStatus.getValue()) {
                case "Активен":
                    TVClass.user_status = "00";
                    break;
                case "Смяна на паролата":
                    TVClass.user_status = "01";
                    break;
                case "Деактивиран":
                    TVClass.user_status = "02";
                    break;
            }

            String ans = TVClass.saveUserData();

            if(ans.equals("insert")){
                TVClass.showAlertInformation("Билетен център", "Потребителят е успешно създаден.");
                this.lockAndClearControls();
            } else if(ans.equals("update")){
                TVClass.showAlertInformation("Билетен център", "Данните за потребителя са актуализирани.");
            } else {
                TVClass.showAlertInformation("Внимание!","Проблем при запис на данните с описание: " + ans);
            }
        });

        // Бутон създаване на НОВ потребител
        buttonNewUser.setOnAction(event -> {
            TVClass.user_id = null; // нулираме, ако има зареден потребител
            textFieldSearch.setText("");
            this.unlockControls();
        });

        // Бурон ИЗХОД от формата
        buttonExit.setOnAction(event -> {
            Stage thisStage = (Stage) buttonExit.getScene().getWindow();
            thisStage.hide();
        });
    }

    // ЗАБРАНЯВАНЕ и ИЗЧИСТВАНЕ на контролите
    private void lockAndClearControls(){
        textFieldUserNames.setText("");
        textFieldLoginName.setText("");
        textFieldPassword.setText("");
        textFieldRating.setText("");
        textFieldNote.setText("");
        comboBoxUserRole.setValue("");
        comboBoxUserStatus.setValue("");

        textFieldUserNames.setDisable(true);
        textFieldLoginName.setDisable(true);
        textFieldPassword.setDisable(true);
        textFieldRating.setDisable(true);
        textFieldNote.setDisable(true);
        comboBoxUserRole.setDisable(true);
        comboBoxUserStatus.setDisable(true);
        buttonSave.setDisable(true);
        buttonCancel.setDisable(true);
        buttonDeleteUser.setDisable(true);

        buttonNewUser.setDisable(false); // Разрешаваме бутона за създаване на нов потребител
    }

    // РАЗРЕШАВАНЕ на контролите
    private void unlockControls(){
        textFieldUserNames.setDisable(false);
        textFieldLoginName.setDisable(false);
        textFieldPassword.setDisable(false);
        //textFieldRating.setDisable(false); // Променя се автоматично от системата, затова остава забранено
        textFieldNote.setDisable(false);
        comboBoxUserRole.setDisable(false);
        comboBoxUserStatus.setDisable(false);
        buttonSave.setDisable(false);
        buttonCancel.setDisable(false);
        buttonDeleteUser.setDisable(false);

        buttonNewUser.setDisable(true); // Забраняваме бутона за създаване на нов потребител
    }

}
