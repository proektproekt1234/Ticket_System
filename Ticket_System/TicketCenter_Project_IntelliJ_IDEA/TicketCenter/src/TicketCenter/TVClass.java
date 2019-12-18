package TicketCenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;
import java.sql.*;

public class TVClass {

    static final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    static String hostName, oraclePort, oracleSID, oracleUserName, oraclePassWord;
    static String user_id, user_login_name, user_pass, user_names, user_rating, user_status, user_role, user_note;
    static String event_id, event_organizer_id, event_organizer_names, event_name, event_type, event_location, event_status;
    static String seats_id, seats_type, age_restriction;
    static int number_of_seats, price, reserved_seats, purchasing_limit, current_ticket_number, next_ticket_number;
    static ObservableList<String> distribNames;
    static boolean successfullyLoggedIn = false;
    static Date status_date;
    static LocalDate event_date;
    static Preferences userPref;


    public TVClass(){

    }

    // Съхраняване в Registry базата в HKEY_CURRENT_USER\Software\JavaSoft\Prefs\(TicketCenter)
    public static void setRegistrySettings(String _hostName, String _oraclePort, String _oracleSID,
                                           String _oracleUserName, String _oraclePassWord) {
        TVClass.hostName = _hostName;
        TVClass.oraclePort = _oraclePort;
        TVClass.oracleSID = _oracleSID;
        TVClass.oracleUserName = _oracleUserName;
        TVClass.oraclePassWord = _oraclePassWord;

        userPref = Preferences.userNodeForPackage(TVClass.class);
        userPref.put("hostName", TVClass.hostName);
        userPref.put("oraclePort", TVClass.oraclePort);
        userPref.put("oracleSID", TVClass.oracleSID);
        userPref.put("oracleUserName", TVClass.oracleUserName);
        userPref.put("oraclePassWord", TVClass.oraclePassWord);
    }

    // Извличане от Registry базата от HKEY_CURRENT_USER\Software\JavaSoft\Prefs\(TicketCenter)
    public static boolean getRegistrySettings() {

        userPref = Preferences.userNodeForPackage(TVClass.class);
        TVClass.hostName = userPref.get("hostName",null);
        TVClass.oraclePort = userPref.get("oraclePort",null);
        TVClass.oracleSID = userPref.get("oracleSID",null);
        TVClass.oracleUserName = userPref.get("oracleUserName",null);
        TVClass.oraclePassWord = userPref.get("oraclePassWord",null);

        if (TVClass.hostName == null) {
            return false; // Липсват настройки в Regitry базата
        }
        else {
            return true;
        }
    }

    // Login в системата
    public static String sysLogin(String _userName, String _passWord) {
        Connection con;
        String sqlStr;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            Statement stmt = con.createStatement();
            sqlStr = "select USER_ID, USER_NAMES, USER_RATING, STATUS, STATUS_DATE, USER_ROLE \n" +
                     "from USERS \n" +
                     "where LOGIN_NAME = '" + _userName + "'\n" +
                     "and USER_PASS = '" + _passWord + "'";
            ResultSet rs = stmt.executeQuery(sqlStr);
            if (rs.next()) {
                user_id = rs.getString(1);
                user_names = rs.getString(2);
                user_rating = rs.getString(3);
                user_status = rs.getString(4);
                status_date = rs.getDate(5);
                user_role = rs.getString(6);
            }
            rs.close();
            stmt.close();
            con.close();
            rs = null;
            stmt = null;
            con = null;

            if (user_id == null) {
                TVClass.showAlertError("Внимание!","Грешно потребителско име или парола.");
                return "no_data_found";
            }
            if (user_status.equals("02")) { // STATUS = "02" -> Деактивиран акаунт
                TVClass.showAlertError("Внимание!","Деактивиран потребител от дата: " +
                                        myDateFormat.format(status_date) + " г.");
                return "deactivated";
            }

            successfullyLoggedIn = true;
            TVClass.showAlertInformation("Билетен център","Здравейте " + TVClass.user_names + "\n\n" +
                                        "Успешно се логнахте в системата.");
            return "ok";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Търсене на ПОТРЕБИТЕЛИ
    public static String searchUsers(String _searchString) {
        Connection con;
        String sqlStr;
        user_id = null; // нулираме предходни търсения
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            Statement stmt = con.createStatement();
            sqlStr = "select USER_ID, LOGIN_NAME, USER_PASS, USER_NAMES, USER_RATING, STATUS, STATUS_DATE, USER_ROLE, NOTE \n" +
                    "from USERS \n" +
                    "where (lower(LOGIN_NAME) like '%" + _searchString.toLowerCase() + "%' or lower(USER_NAMES) like '%" +
                            _searchString.toLowerCase() + "%')\n" +
                    "and rownum < 2";
            ResultSet rs = stmt.executeQuery(sqlStr);
            if (rs.next()) {
                user_id = rs.getString(1);
                user_login_name = rs.getString(2);
                user_pass = rs.getString(3);
                user_names = rs.getString(4);
                user_rating = rs.getString(5);
                user_status = rs.getString(6);
                status_date = rs.getDate(7);
                user_role = rs.getString(8);
                user_note = rs.getString(9);
            }
            rs.close();
            stmt.close();
            con.close();
            rs = null;
            stmt = null;
            con = null;

            if (user_id == null) {
                return "no_data_found";
            }

            return "ok";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Търсене на СЪБИТИЯ от ОРГАНИЗАТОР
    public static String searchOrganizerEvents(String _searchString) {
        Connection con;
        String sqlStr;
        event_id = null; // нулираме предходни търсения
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            Statement stmt = con.createStatement();
            // Търсене на данни за събитието
            sqlStr = "select v.EVENT_ID, (select u.USER_NAMES from USERS u where u.USER_ID = v.ORGANIZER_USER_ID),\n" +
                            "v.EVENT_NAME, v.EVENT_TYPE, v.EVENT_DATE, v.EVENT_LOCATION, v.EVENT_STATUS,\n" +
                            "s.SEATS_ID, s.SEATS_TYPE, s.NUMBER_OF_SEATS, s.PRICE, s.AGE_RESTRICTION, s.PURCHASING_LIMIT\n" +
                    "from EVENTS v, SEATS s\n" +
                    "where v.EVENT_ID = s.EVENT_ID\n" +
                    "and (lower(v.EVENT_NAME) like '%" + _searchString.toLowerCase() + "%' or lower(v.EVENT_LOCATION) like '%" +
                    _searchString.toLowerCase() + "%')" + " and rownum < 2";
            ResultSet rs = stmt.executeQuery(sqlStr);
            if (rs.next()) {
                event_id = rs.getString(1);
                event_organizer_names = rs.getString(2);
                event_name = rs.getString(3);
                event_type = rs.getString(4);
                event_date = rs.getDate(5).toLocalDate();
                event_location = rs.getString(6);
                event_status = rs.getString(7);
                seats_id = rs.getString(8);
                seats_type = rs.getString(9);
                number_of_seats = rs.getInt(10);
                price = rs.getInt(11);
                age_restriction = rs.getString(12);
                purchasing_limit = rs.getInt(13);
            }
            if(event_id != null) {
                // Търсене на дистрибуторите на събитието
                sqlStr = "select u.USER_NAMES\n" +
                            "from DISTRIBUTORS d, USERS u\n" +
                            "where d.USER_ID = u.USER_ID\n" +
                            "and d.EVENT_ID = " + event_id;
                rs = stmt.executeQuery(sqlStr);
                List<String> distList = new ArrayList<>();
                distribNames = FXCollections.observableArrayList("Проблем при зареждане на дистрибуторите");
                while (rs.next()) {
                    distList.add(rs.getString(1));
                }
                if (distList.size() == 0) {
                    distribNames = FXCollections.observableArrayList("Не са намерени дистрибутори");
                } else {
                    distribNames = FXCollections.observableArrayList(distList);
                }
            }
            rs.close();
            stmt.close();
            con.close();
            rs = null;
            stmt = null;
            con = null;

            if (event_id == null) {
                return "no_data_found";
            }

            return "ok";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Търсене на СЪБИТИЯ на ДИСТРИБУТОР
    public static String searchDistributorEvents(String _searchString) {
        Connection con;
        String sqlStr;
        event_id = null; // нулираме предходни търсения
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            Statement stmt = con.createStatement();
            // Търсене на данни за събитието
            sqlStr = "select v.EVENT_ID, u.USER_NAMES, v.EVENT_NAME, v.EVENT_TYPE, v.EVENT_DATE, v.EVENT_LOCATION,\n" +
                            "v.EVENT_STATUS, s.SEATS_ID, s.SEATS_TYPE, s.NUMBER_OF_SEATS, s.PRICE, nvl(s.RESERVED_SEATS,0),\n" +
                            "s.AGE_RESTRICTION, s.PURCHASING_LIMIT, nvl(max(t.SEAT_NUMBER),0)\n" +
                    "from USERS u, EVENTS v, SEATS s, TICKETS t, DISTRIBUTORS d\n" +
                    "where u.USER_ID = v.ORGANIZER_USER_ID\n" +
                    "and v.EVENT_ID = s.EVENT_ID\n" +
                    "and v.EVENT_ID = d.EVENT_ID\n" +
                    "and d.USER_ID =  " + TVClass.user_id + "\n" +
                    "and v.EVENT_ID = t.EVENT_ID(+)\n" +
                    "and (lower(v.EVENT_NAME) like '%" + _searchString.toLowerCase() + "%' or lower(v.EVENT_LOCATION) like '%" +
                        _searchString.toLowerCase() + "%')\n" +
                    "group by v.EVENT_ID, u.USER_NAMES, v.EVENT_NAME, v.EVENT_TYPE, v.EVENT_DATE, v.EVENT_LOCATION,\n" +
                            "v.EVENT_STATUS, s.SEATS_ID, s.SEATS_TYPE, s.NUMBER_OF_SEATS, s.PRICE, nvl(s.RESERVED_SEATS,0),\n" +
                            "s.AGE_RESTRICTION, s.PURCHASING_LIMIT";
            ResultSet rs = stmt.executeQuery(sqlStr);
            if (rs.next()) {
                event_id = rs.getString(1);
                event_organizer_names = rs.getString(2);
                event_name = rs.getString(3);
                event_type = rs.getString(4);
                event_date = rs.getDate(5).toLocalDate();
                event_location = rs.getString(6);
                event_status = rs.getString(7);
                seats_id = rs.getString(8);
                seats_type = rs.getString(9);
                number_of_seats = rs.getInt(10);
                price = rs.getInt(11);
                reserved_seats = rs.getInt(12);
                age_restriction = rs.getString(13);
                purchasing_limit = rs.getInt(14);
                current_ticket_number = rs.getInt(15);
            }
            rs.close();
            stmt.close();
            con.close();
            rs = null;
            stmt = null;
            con = null;

            if (event_id == null) {
                return "no_data_found";
            }

            return "ok";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // ИЗТРИВАНЕ на ПОТРЕБИТЕЛ
    public static String deleteUser(String _user_id){
        Connection con;
        String sqlStr, answer = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            sqlStr = "delete USERS where USER_ID = " + _user_id;
            stmt.executeQuery(sqlStr);
            con.commit();

            stmt.close();
            con.close();
            stmt = null;
            con = null;

            return "ok";
        }
        catch(Exception e){
            return e.getMessage();
        }
    };

    // ИЗТРИВАНЕ на СЪБИТИЕ
    public static String deleteEvent(String _event_id){
        Connection con;
        String sqlStr, answer = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            sqlStr = "delete TICKETS where EVENT_ID = " + _event_id;
            stmt.executeQuery(sqlStr);
            sqlStr = "delete DISTRIBUTORS where EVENT_ID = " + _event_id;
            stmt.executeQuery(sqlStr);
            sqlStr = "delete SEATS where EVENT_ID = " + _event_id;
            stmt.executeQuery(sqlStr);
            sqlStr = "delete EVENTS where EVENT_ID = " + _event_id;
            stmt.executeQuery(sqlStr);
            con.commit();

            stmt.close();
            con.close();
            stmt = null;
            con = null;

            return "ok";
        }
        catch(Exception e){
            return e.getMessage();
        }
    };

    // СЪХРАНЯВАНЕ на данните за ПОТРЕБИТЕЛ
    public static String saveUserData(){
        String insertUpdateUser = null;
        Connection con;
        String sqlStr, answer = null;
        if(TVClass.user_note == null){TVClass.user_note = "";}
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            sqlStr = "select 1 from USERS where USER_ID = " + TVClass.user_id;
            ResultSet rs = stmt.executeQuery(sqlStr);
            if(rs.next()){
                // Актуализиране на съществуващ потребител
                insertUpdateUser = "update";
                con.setAutoCommit(false);
                sqlStr = "update USERS\n" +
                            "set USER_NAMES = '" + TVClass.user_names + "',\n" +
                                "LOGIN_NAME = '" + TVClass.user_login_name + "',\n" +
                                "USER_PASS = '" + TVClass.user_pass + "',\n" +
                                //"USER_RATING = " + TVClass.user_rating + ",\n" + // Променя се автоматично от системата
                                "STATUS = '" + TVClass.user_status + "',\n" +
                                "STATUS_DATE = sysdate,\n" +
                                "USER_ROLE = '" + TVClass.user_role + "',\n" +
                                "NOTE = '" + TVClass.user_note + "'\n" +
                                "where USER_ID = " + TVClass.user_id;
            }
            else { // Създаване на НОВ потребител
                insertUpdateUser = "insert";
                sqlStr = "insert into USERS (USER_NAMES, LOGIN_NAME, USER_PASS, USER_RATING, CREATION_DATE," +
                                                "STATUS, STATUS_DATE, USER_ROLE, NOTE) values(\n" +
                            // USER_ID се популира от before_insert тригер
                            "'" + TVClass.user_names + "',\n" +
                            "'" + TVClass.user_login_name + "',\n" +
                            "'" + TVClass.user_pass + "',\n" +
                            "null,\n" + // Рейтинга се променя автоматично от системата
                            "sysdate,\n" +
                            "'" + TVClass.user_status + "',\n" +
                            "sysdate,\n" +
                            "'" + TVClass.user_role + "',\n" +
                            "'" + TVClass.user_note + "')";
            }
            stmt.executeQuery(sqlStr);
            con.commit();

            stmt.close();
            con.close();
            stmt = null;
            con = null;

            if (insertUpdateUser.equals("insert")) {
                return "insert";
            } else {
                return "update";
            }
        }
        catch(Exception e){
            return e.getMessage();
        }
    };

    // СЪХРАНЯВАНЕ на данните за СЪБИТИЕ
    public static String saveEventData(ObservableList<String> _selectedDistributors){
        String insertUpdateEvent = null;
        Connection con;
        String sqlStr, answer = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            sqlStr = "select 1 from EVENTS where EVENT_ID = " + TVClass.event_id;
            ResultSet rs = stmt.executeQuery(sqlStr);
            if(rs.next()){ // Актуализиране на съществуващо събитие
                insertUpdateEvent = "update";
                con.setAutoCommit(false);
                sqlStr = "update EVENTS\n" +
                        "set EVENT_NAME = '" + TVClass.event_name + "',\n" +
                        "EVENT_TYPE = '" + TVClass.event_type + "',\n" +
                        "EVENT_DATE = to_date('" + TVClass.event_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                                                "','dd.mm.yyyy'),\n" +
                        "EVENT_LOCATION = '" + TVClass.event_location + "',\n" +
                        "EVENT_STATUS = '" + TVClass.event_status + "',\n" +
                        "EVENT_STATUS_DATE = sysdate\n" +
                        "where EVENT_ID = " + TVClass.event_id;
                stmt.executeQuery(sqlStr);
                // Актуализиране на местата на събитието
                sqlStr = "update SEATS\n" +
                        "set SEATS_TYPE = '" + TVClass.seats_type + "',\n" +
                        "NUMBER_OF_SEATS = '" + TVClass.number_of_seats + "',\n" +
                        "PRICE = '" + TVClass.price + "',\n" +
                        "AGE_RESTRICTION = '" + TVClass.age_restriction + "',\n" +
                        "PURCHASING_LIMIT = '" + TVClass.purchasing_limit + "'\n" +
                        "where EVENT_ID = " + TVClass.event_id;
                stmt.executeQuery(sqlStr);
                // Актуализиране на дистрибуторите в таблицата DISTRIBUTORS
                sqlStr = "delete DISTRIBUTORS where EVENT_ID = " + TVClass.event_id;
                stmt.executeQuery(sqlStr);
                for(int i=0; i<_selectedDistributors.size(); i++) {
                    sqlStr = "insert into DISTRIBUTORS values(\n" +
                                "DISTRIBUTORS_DISTRIB_ID_SЕQ.NEXTVAL, " + TVClass.event_id + ",\n" +
                                "(select USER_ID from USERS where USER_NAMES = '" +
                                                                _selectedDistributors.get(i) + "' and rownum < 2))";
                    stmt.executeQuery(sqlStr);
                }
            }
            else { // Създаване на НОВО събитие
                insertUpdateEvent = "insert";
                // Insert на данните за събитието
                sqlStr = "insert into EVENTS (ORGANIZER_USER_ID, EVENT_NAME, EVENT_TYPE, EVENT_DATE," +
                                                "EVENT_LOCATION, EVENT_STATUS, EVENT_STATUS_DATE) values(\n" +
                        // EVENT_ID се популира от before_insert тригер
                        TVClass.user_id + ",\n" +
                        "'" + TVClass.event_name + "',\n" +
                        "'" + TVClass.event_type + "',\n" +
                        "to_date('" + TVClass.event_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "','dd.mm.yyyy'),\n" +
                        "'" + TVClass.event_location + "',\n" +
                        "'" + TVClass.event_status + "', sysdate)";
                stmt.executeQuery(sqlStr);
                // Insert данни за местата в таблицата SEATS
                sqlStr = "insert into SEATS values(\n" +
                            "SEATS_SEATS_ID_SЕQ.NEXTVAL, EVENTS_EVENT_ID_SЕQ.CURRVAL, '" + TVClass.seats_type + "',\n" +
                            TVClass.number_of_seats + ", " + TVClass.price + "," + TVClass.reserved_seats +
                            ",'" + TVClass.age_restriction + "', " + TVClass.purchasing_limit + ")";
                stmt.executeQuery(sqlStr);
                // Insert на избраните дистрибутори в таблицата DISTRIBUTORS
                for(int i=0; i<_selectedDistributors.size(); i++) {
                    sqlStr = "insert into DISTRIBUTORS values(\n" +
                                        "DISTRIBUTORS_DISTRIB_ID_SЕQ.NEXTVAL, EVENTS_EVENT_ID_SЕQ.CURRVAL,\n" +
                                        "(select USER_ID from USERS where USER_NAMES = '" +
                                            _selectedDistributors.get(i) + "' and rownum < 2))";
                    stmt.executeQuery(sqlStr);
                }
            }
            con.commit();

            stmt.close();
            con.close();
            stmt = null;
            con = null;

            if (insertUpdateEvent.equals("insert")) {
                return "insert";
            } else {
                return "update";
            }
        }
        catch(Exception e){
            return e.getMessage();
        }
    };

    // СЪХРАНЯВАНЕ на продадени БИЛЕТИ
    public static String saveSoldTickets(int _numberOfTickets, String _customerName){
        Connection con;
        String sqlStr, answer = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            next_ticket_number = TVClass.current_ticket_number + _numberOfTickets;

            sqlStr = "select 1 from SEATS where EVENT_ID = " + TVClass.event_id + " and NUMBER_OF_SEATS < " + next_ticket_number;
            ResultSet rs = stmt.executeQuery(sqlStr);
            if(rs.next()){
                // Т.е. надвишаваме броя на свободните места/билети
                stmt.close();
                con.close();
                stmt = null;
                con = null;
                return "limit"; // Изход
            } else {
                // Съхраняваме продадените билети
                sqlStr = "Insert into TICKETS values (TICKETS_TICKET_ID_SEQ.NEXTVAL, " + TVClass.event_id + ",\n" +
                                            TVClass.seats_id + "," + TVClass.user_id + ",'" + _customerName + "'," +
                                            _numberOfTickets + "," + next_ticket_number + ")";
                stmt.executeQuery(sqlStr);
                // Отразяваме броя на купените билети
                sqlStr = "Update SEATS set RESERVED_SEATS = nvl(RESERVED_SEATS,0) + " + _numberOfTickets + "\n" +
                        "where EVENT_ID = " + TVClass.event_id;
                stmt.executeQuery(sqlStr);
                // Увеличваме рейтинга на Дистрибутора
                sqlStr = "Update USERS set USER_RATING = nvl(USER_RATING,0) + " + _numberOfTickets +
                        "\n where USER_ID = " + TVClass.user_id;
                stmt.executeQuery(sqlStr);
                con.commit();
                // Актуализираме поредният номер на билета
                sqlStr = "select RESERVED_SEATS from SEATS where EVENT_ID = " + TVClass.event_id;
                rs = stmt.executeQuery(sqlStr);
                if (rs.next()) {
                    current_ticket_number = rs.getInt(1);
                }
                stmt.executeQuery(sqlStr);
            }

            stmt.close();
            con.close();
            stmt = null;
            con = null;

            return "ok";
        }
        catch(Exception e){
            return e.getMessage();
        }
    };

    // Зареждане на активни ДИСТРИБУТОРИ
    public static ObservableList<String> loadActiveDistributors(){
        ObservableList<String> distributors;
        Connection con;
        String sqlStr;
        List<String> distList = new ArrayList<>();
        distributors = FXCollections.observableArrayList("Проблем при зареждане на дистрибуторите");
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + TVClass.hostName + ":" + TVClass.oraclePort + ":" + TVClass.oracleSID;
            con = DriverManager.getConnection(dbURL, TVClass.oracleUserName, TVClass.oraclePassWord);
            Statement stmt = con.createStatement();
            sqlStr = "select USER_NAMES from USERS where USER_ROLE = '2' and STATUS = '00'";
            ResultSet rs = stmt.executeQuery(sqlStr);
            while (rs.next()){
                distList.add(rs.getString(1));
            }
            stmt.close();
            con.close();
            stmt = null;
            con = null;

            if(distList.size() == 0) {
                distributors = FXCollections.observableArrayList("Не са намерени дистрибутори");
            } else {
                distributors = FXCollections.observableArrayList(distList);
            }
            return distributors;
        }
        catch(Exception e){
            TVClass.showAlertError("Внимание!","Проблем при зареждане на дистрибуторите с описание: " +
                                e.getMessage());
            return distributors;
        }
    };

    // ТЕСТВАНЕ на JDBC връзката към Oracle базата
    public static String testJdbcConnection(String _hostName, String _oraclePort, String _oracleSID,
                                            String _userName,String _passWord){
        Connection con;
        String sqlStr, answer = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@" + _hostName + ":" + _oraclePort + ":" + _oracleSID;
            con = DriverManager.getConnection(dbURL, _userName, _passWord);
            Statement stmt = con.createStatement();
            sqlStr = "select 1 from USERS where LOGIN_NAME = 'Admin'";
            ResultSet rs = stmt.executeQuery(sqlStr);
            if(rs.next()){
                answer = rs.getString(1);
            }
            rs.close();
            stmt.close();
            con.close();
            rs = null;
            stmt = null;
            con = null;

            if(answer == null) {
                return "no_data_found";
            } else {
                return "ok";
            }
        }
        catch(Exception e){
            return e.getMessage();
        }
    };

    public static void showAlertInformation(String alertTitle, String alertText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle(alertTitle);
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.showAndWait();
    }
    public static void showAlertError(String alertTitle, String alertText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle(alertTitle);
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.showAndWait();
    }
    public static String showAlertYesNo(String alertTitle, String alertText){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertText);
        //alert.setContentText(alertText);
        ButtonType da_Button = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType ne_Button = new ButtonType("Не", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(da_Button, ne_Button);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == da_Button) {
            return "yes";
        } else { // type == ne_Button
            return "no";
        }
    }

}
