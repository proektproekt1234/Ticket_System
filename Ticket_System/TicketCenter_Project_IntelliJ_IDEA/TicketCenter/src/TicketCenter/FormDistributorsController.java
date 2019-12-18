package TicketCenter;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FormDistributorsController {

    @FXML
    private Button buttonExit, buttonSearch, buttonSell, buttonCancel;
    @FXML
    private TextField textFieldSearch, textFieldEventName, textFieldEventLocation, textFieldNumberOfSeats, textFieldPrice,
            textFieldPurchasingLimit, textFieldEventStatus, textFieldEventType, textFieldSeatsType, textFieldAgeRestriction,
            textFieldNumberOfTickets, textFieldSeatNumber, textFieldCustomerNames;
    @FXML
    private DatePicker datePickerEventDate;
    @FXML
    private Label labelDistributorNames;


    @FXML
    void initialize() {

        labelDistributorNames.setText("Дистрибутор  >>  " + TVClass.user_names);

        // ТЪРСЕНЕ на СЪБИТИЕ на ДИСТРИБУТОР
        buttonSearch.setOnAction(event -> {
            if(textFieldSearch.getText().isEmpty()){
                TVClass.showAlertError("Внимание!", "Не сте задали критерии за търсене.");
                textFieldSearch.requestFocus();
                return; // Изход
            }
            String ans = TVClass.searchDistributorEvents(textFieldSearch.getText());
            if(ans.equals("no_data_found")){
                TVClass.showAlertError("Внимание!","Не е намерено ваше събитие с тези данни.");
                this.lockAndClearControls();
                return; // изход
            } else if (ans.equals("ok")) {
                this.unlockControls();
            }
            else {
                TVClass.showAlertError("Внимание!","Проблем при търсене с описание: " + ans);
                this.lockAndClearControls();
                return; // изход
            }

            this.populateControls();

        });

        // Бутон ЗАПИС на продадени БИЛЕТИ
        buttonSell.setOnAction(event -> {
            if(textFieldNumberOfTickets.getText().isEmpty()){
                TVClass.showAlertError("Внимание!", "Не сте въвели броя на продадените билети.");
                textFieldNumberOfTickets.requestFocus();
                return; // изход
            }
            if(textFieldCustomerNames.getText().isEmpty()){
                TVClass.showAlertError("Внимание!", "Не сте въвели имената на клиента.");
                textFieldCustomerNames.requestFocus();
                return; // изход
            }
            try { // Проверка за коректно въведени данни/числа
                int z = Integer.parseInt(textFieldNumberOfTickets.getText());
            } catch (Exception e){
                TVClass.showAlertError("Внимание", "Некоретно въведен брой на продадените билети.");
                textFieldNumberOfTickets.requestFocus();
                return; // изход
            }
            // Проверка за надвишен лимит
            if(Integer.parseInt(textFieldNumberOfTickets.getText()) > Integer.parseInt(textFieldPurchasingLimit.getText())){
                TVClass.showAlertError("Внимание!", "Един клиент може да купи максимум " +
                                            textFieldPurchasingLimit.getText() + " билета.");
                textFieldNumberOfTickets.requestFocus();
                return; // изход
            }

            String ans = TVClass.saveSoldTickets(Integer.parseInt(textFieldNumberOfTickets.getText()),
                                                                    textFieldCustomerNames.getText());
            if(ans.equals("ok")){
                TVClass.showAlertInformation("Билетен център",
                        "Успешно продадени " + textFieldNumberOfTickets.getText() + " бр. билети.");
                textFieldSeatNumber.setText(String.valueOf(TVClass.current_ticket_number + 1));
                textFieldCustomerNames.setText("");
                textFieldNumberOfTickets.setText("");
                textFieldNumberOfTickets.requestFocus();
            } else if(ans.equals("limit")){
                TVClass.showAlertError("Внимание!","Надвишен е броят на свободните места.");
                textFieldNumberOfTickets.requestFocus();
            } else {
                TVClass.showAlertError("Внимание!","Проблем при запис на данните с описание: " + ans);
            }
        });

        // Бутон ОТКАЗ
        buttonCancel.setOnAction(event -> {
            TVClass.event_id = null; // изчистваме търсенето
            this.lockAndClearControls();
        });

        // Бурон ИЗХОД от формата
        buttonExit.setOnAction(event -> {
            Stage thisStage = (Stage) buttonExit.getScene().getWindow();
            thisStage.hide();
        });

    }

    // ЗАБРАНЯВАНЕ и ИЗЧИСТВАНЕ на контролите
    private void lockAndClearControls(){
        textFieldEventName.setText("");
        textFieldEventLocation.setText("");
        textFieldEventType.setText("");
        textFieldEventStatus.setText("");
        datePickerEventDate.setValue(null);
        textFieldSeatsType.setText("");
        textFieldAgeRestriction.setText("");
        textFieldPrice.setText("");
        textFieldPurchasingLimit.setText("");
        textFieldNumberOfSeats.setText("");
        textFieldSeatNumber.setText("");
        textFieldNumberOfTickets.setText("");
        textFieldCustomerNames.setText("");

        textFieldEventName.setDisable(true);
        textFieldEventLocation.setDisable(true);
        textFieldEventType.setDisable(true);
        textFieldEventStatus.setDisable(true);
        datePickerEventDate.setDisable(true);
        textFieldSeatsType.setDisable(true);
        textFieldAgeRestriction.setDisable(true);
        textFieldPrice.setDisable(true);
        textFieldPurchasingLimit.setDisable(true);
        textFieldNumberOfSeats.setDisable(true);
        textFieldSeatNumber.setDisable(true);
        textFieldNumberOfTickets.setDisable(true);
        textFieldCustomerNames.setDisable(true);
        buttonSell.setDisable(true);
        buttonCancel.setDisable(true);
    }

    // РАЗРЕШАВАНЕ на контролите
    private void unlockControls(){
        textFieldEventName.setText("");
        textFieldEventLocation.setText("");
        textFieldEventType.setText("");
        textFieldEventStatus.setText("");
        datePickerEventDate.setValue(null);
        textFieldSeatsType.setText("");
        textFieldAgeRestriction.setText("");
        textFieldPrice.setText("");
        textFieldPurchasingLimit.setText("");
        textFieldNumberOfSeats.setText("");
        textFieldSeatNumber.setText("");
        textFieldNumberOfTickets.setText("");
        textFieldCustomerNames.setText("");

        textFieldEventName.setDisable(false);
        textFieldEventLocation.setDisable(false);
        textFieldEventType.setDisable(false);
        textFieldEventStatus.setDisable(false);
        datePickerEventDate.setDisable(false);
        textFieldSeatsType.setDisable(false);
        textFieldAgeRestriction.setDisable(false);
        textFieldPrice.setDisable(false);
        textFieldPurchasingLimit.setDisable(false);
        textFieldNumberOfSeats.setDisable(false);
        textFieldSeatNumber.setDisable(false);
        textFieldNumberOfTickets.setDisable(false);
        textFieldCustomerNames.setDisable(false);
        buttonSell.setDisable(false);
        buttonCancel.setDisable(false);
    }

    // ПОПЪЛВАНЕ на контролите
    private void populateControls(){
        textFieldEventName.setText(TVClass.event_name);
        textFieldEventLocation.setText(TVClass.event_location);
        textFieldEventType.setText(TVClass.event_type);
        textFieldEventStatus.setText(TVClass.event_status);
        datePickerEventDate.setValue(TVClass.event_date);
        textFieldSeatsType.setText(TVClass.seats_type);
        textFieldAgeRestriction.setText(TVClass.age_restriction);
        textFieldPrice.setText(String.valueOf(TVClass.price));
        textFieldPurchasingLimit.setText(String.valueOf(TVClass.purchasing_limit));
        textFieldNumberOfSeats.setText(String.valueOf(TVClass.number_of_seats));
        textFieldSeatNumber.setText(String.valueOf(TVClass.reserved_seats));
        textFieldNumberOfTickets.setText("");
        textFieldCustomerNames.setText("");

        textFieldEventName.setEditable(false);
        textFieldEventLocation.setEditable(false);
        textFieldEventType.setEditable(false);
        textFieldEventStatus.setEditable(false);
        datePickerEventDate.setEditable(false);
        textFieldSeatsType.setEditable(false);
        textFieldAgeRestriction.setEditable(false);
        textFieldPrice.setEditable(false);
        textFieldPurchasingLimit.setEditable(false);
        textFieldNumberOfSeats.setEditable(false);
        textFieldSeatNumber.setEditable(false);

        textFieldNumberOfTickets.requestFocus();
    }
}
