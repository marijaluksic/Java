module com.example.luksic7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens com.example.luksic10 to javafx.fxml;
    exports com.example.luksic10;
}