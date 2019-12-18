package TicketCenter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

public class FormEventsController {

    @FXML
    private Button buttonSearch, buttonEventCreation, buttonSave, buttonCancel, buttonDelete,
                    buttonLoadDistributors, buttonExit;
    @FXML
    private TextField textFieldSearch, textFieldEventName, textFieldOrganizer, textFieldEventLocation,
                        textFieldNumberOfSeats, textFieldPrice, textFieldPurchasingLimit;
    @FXML
    private ComboBox<String> comboBoxEventType, comboBoxEventStatus, comboBoxSeatsType, comboBoxAgeRestriction;
    @FXML
    private ListView<String> listViewDistributors;
    @FXML
    private DatePicker datePickerEventDate;


    @FXML
    void initialize() {

        comboBoxEventType.getItems().addAll("Концерт","Театър","Кино","Спортно събитие");
        comboBoxEventStatus.getItems().addAll("Предстоящо","Отменено","Състояло се");
        comboBoxSeatsType.getItems().addAll("Стандартно","Балкон", "VIP");
        comboBoxAgeRestriction.getItems().addAll("14 години","16 години", "Без ограничение");

        listViewDistributors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // ТЪРСЕНЕ на СЪБИТИЕ на ОРГАНИЗАТОР
        buttonSearch.setOnAction(event -> {
            if(textFieldSearch.getText().isEmpty()){
                TVClass.showAlertError("Внимание!", "Не сте задали критерии за търсене.");
                textFieldSearch.requestFocus();
                return; // Изход
            }
            String ans = TVClass.searchOrganizerEvents(textFieldSearch.getText());
            if(ans.equals("no_data_found")){
                TVClass.showAlertError("Внимание!","Не е намерено събитие с тези данни.");
                this.lockAndClearControls();
                return; // изход
            } else if (ans.equals("ok")) {
                this.unlockControls();
                listViewDistributors.setItems(TVClass.distribNames);
                listViewDistributors.setOrientation(Orientation.VERTICAL);
            }
            else {
                TVClass.showAlertError("Внимание!","Проблем при търсене с описание: " + ans);
                this.lockAndClearControls();
                return; // изход
            }

            textFieldEventName.setText(TVClass.event_name);
            textFieldOrganizer.setText(TVClass.user_names);
            textFieldEventLocation.setText(TVClass.event_location);
            comboBoxEventType.setValue(TVClass.event_type);
            comboBoxEventStatus.setValue(TVClass.event_status);
            datePickerEventDate.setValue(TVClass.event_date);
            comboBoxSeatsType.setValue(TVClass.seats_type);
            textFieldNumberOfSeats.setText(String.valueOf(TVClass.number_of_seats));
            textFieldPrice.setText(String.valueOf(TVClass.price));
            comboBoxAgeRestriction.setValue(String.valueOf(TVClass.age_restriction));
            textFieldPurchasingLimit.setText(String.valueOf(TVClass.purchasing_limit));
        });

        // Бутон ЗАПИС на събитие
        buttonSave.setOnAction(event -> {
            if(textFieldEventName.getText().isEmpty() || textFieldEventLocation.getText().isEmpty() ||
                    textFieldEventLocation.getText().isEmpty() || comboBoxEventType.getValue().isEmpty() ||
                    comboBoxEventStatus.getValue().isEmpty() || datePickerEventDate.getValue() == null ||
                    comboBoxSeatsType.getValue().isEmpty() || textFieldNumberOfSeats.getText().isEmpty() ||
                    textFieldPrice.getText().isEmpty() || comboBoxAgeRestriction.getValue().isEmpty() ||
                    textFieldPurchasingLimit.getText().isEmpty() ){
                TVClass.showAlertError("Внимание!", "Не сте въвели всички данни за събитието.");
                return; // изход
            }
            try { // Проверка за коректно въведени данни/числа
                int z = Integer.parseInt(textFieldNumberOfSeats.getText());
                z = Integer.parseInt(textFieldPrice.getText());
                z = Integer.parseInt(textFieldPurchasingLimit.getText());
            } catch (Exception e){
                TVClass.showAlertError("Внимание", "Некоретно въведени числени стойности");
                return; // изход
            }
            TVClass.event_organizer_id = TVClass.user_id;
            TVClass.event_name = textFieldEventName.getText();
            TVClass.event_type = comboBoxEventType.getValue();
            TVClass.event_date = datePickerEventDate.getValue();
            TVClass.event_location = textFieldEventLocation.getText();
            TVClass.event_status = comboBoxEventStatus.getValue();
            TVClass.seats_type = comboBoxSeatsType.getValue();
            TVClass.number_of_seats = Integer.parseInt(textFieldNumberOfSeats.getText());
            TVClass.price = Integer.parseInt(textFieldPrice.getText());
            TVClass.age_restriction = comboBoxAgeRestriction.getValue();
            TVClass.purchasing_limit = Integer.parseInt(textFieldPurchasingLimit.getText());

            ObservableList<String> selectedDistributors = listViewDistributors.getSelectionModel().getSelectedItems();
            if(selectedDistributors.size() == 0){
                TVClass.showAlertError("Внимание!", "Не сте избрали нито един дистрибутор.");
                return; // изход
            }

            String ans = TVClass.saveEventData(selectedDistributors);

            if(ans.equals("insert")){
                TVClass.showAlertInformation("Билетен център", "Събитието е успешно създадено.");
                this.lockAndClearControls();
            } else if(ans.equals("update")){
                // Презареждаме в случай, че са променени дистрибуторите
                String zz = TVClass.searchOrganizerEvents(textFieldEventName.getText());
                listViewDistributors.setItems(TVClass.distribNames);
                listViewDistributors.setOrientation(Orientation.VERTICAL);
                TVClass.showAlertInformation("Билетен център", "Данните за събитието са актуализирани.");
            } else {
                TVClass.showAlertInformation("Внимание!","Проблем при запис на данните с описание: " + ans);
            }
        });

        // Бурон ИЗХОД от формата
        buttonExit.setOnAction(event -> {
            Stage thisStage = (Stage) buttonExit.getScene().getWindow();
            thisStage.hide();
        });

        // Бутон създаване на НОВО събитие
        buttonEventCreation.setOnAction(event -> {
            TVClass.event_id = null; // нулираме, ако има заредено събитие
            textFieldSearch.setText("");

            this.unlockControls();
            ObservableList<String> distributors = TVClass.loadActiveDistributors();
            listViewDistributors.setItems(distributors);
            listViewDistributors.setOrientation(Orientation.VERTICAL);

            textFieldOrganizer.setText(TVClass.user_names);
            textFieldOrganizer.setEditable(false);
        });

        // Бутон ИЗТРИВАНЕ на СЪБИТИЕ
        buttonDelete.setOnAction(event -> {
            if(TVClass.event_id == null){
                TVClass.showAlertError("Внимание!", "Не сте избрали събитие.");
                return; // изход
            }
            if(TVClass.showAlertYesNo("Внимание!",
                    "Сигурни ли сте, че искате да изтриете събитието: " + TVClass.event_name).equals("no")) {
                return; // Изход
            }
            String ans = TVClass.deleteEvent(TVClass.event_id);
            if(ans.equals("ok")){
                TVClass.showAlertInformation("Билетен център", "Събитието е изтрито.");
                this.lockAndClearControls();
            } else {
                TVClass.showAlertInformation("Внимание!",
                        "Проблем при изтриване на събитието с описание: " + ans);
            }
        });

        // Бутон зареждане на всички налични Дистрибутори
        buttonLoadDistributors.setOnAction(event -> {
            ObservableList<String> distributors = TVClass.loadActiveDistributors();
            listViewDistributors.setItems(distributors);
            listViewDistributors.setOrientation(Orientation.VERTICAL);
        });

        // Бутон ОТКАЗ
        buttonCancel.setOnAction(event -> {
            TVClass.event_id = null; // изчистваме търсенето
            this.lockAndClearControls();
        });

    }


    // ЗАБРАНЯВАНЕ и ИЗЧИСТВАНЕ на контролите
    private void lockAndClearControls(){
        textFieldEventName.setText("");
        textFieldOrganizer.setText("");
        textFieldEventLocation.setText("");
        comboBoxEventType.setValue("");
        comboBoxEventStatus.setValue("");
        listViewDistributors.getItems().clear();
        comboBoxSeatsType.setValue("");
        textFieldNumberOfSeats.setText("");
        textFieldPrice.setText("");
        comboBoxAgeRestriction.setValue("");
        textFieldPurchasingLimit.setText("");
        datePickerEventDate.setValue(null);

        textFieldEventName.setDisable(true);
        textFieldOrganizer.setDisable(true);
        textFieldEventLocation.setDisable(true);
        comboBoxEventType.setDisable(true);
        comboBoxEventStatus.setDisable(true);
        listViewDistributors.setDisable(true);
        datePickerEventDate.setDisable(true);
        buttonSave.setDisable(true);
        buttonCancel.setDisable(true);
        buttonDelete.setDisable(true);
        buttonLoadDistributors.setDisable(true);
        comboBoxSeatsType.setDisable(true);
        textFieldNumberOfSeats.setDisable(true);
        textFieldPrice.setDisable(true);
        comboBoxAgeRestriction.setDisable(true);
        textFieldPurchasingLimit.setDisable(true);
    }

    // РАЗРЕШАВАНЕ на контролите
    private void unlockControls(){
        textFieldEventName.setText("");
        textFieldOrganizer.setText("");
        textFieldEventLocation.setText("");
        comboBoxEventType.setValue("");
        comboBoxEventStatus.setValue("");
        listViewDistributors.getItems().clear();
        comboBoxSeatsType.setValue("");
        textFieldNumberOfSeats.setText("");
        textFieldPrice.setText("");
        comboBoxAgeRestriction.setValue("");
        textFieldPurchasingLimit.setText("");
        datePickerEventDate.setValue(null);

        textFieldEventName.setDisable(false);
        textFieldOrganizer.setDisable(false);
        textFieldEventLocation.setDisable(false);
        comboBoxEventType.setDisable(false);
        comboBoxEventStatus.setDisable(false);
        listViewDistributors.setDisable(false);
        buttonSave.setDisable(false);
        buttonCancel.setDisable(false);
        buttonDelete.setDisable(false);
        buttonLoadDistributors.setDisable(false);
        datePickerEventDate.setDisable(false);
        comboBoxSeatsType.setDisable(false);
        textFieldNumberOfSeats.setDisable(false);
        textFieldPrice.setDisable(false);
        comboBoxAgeRestriction.setDisable(false);
        textFieldPurchasingLimit.setDisable(false);
    }

}
