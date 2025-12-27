module com.mycompany.tic_tac_toe_app {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.tic_tac_toe_app to javafx.fxml;
    exports com.mycompany.tic_tac_toe_app;
}
