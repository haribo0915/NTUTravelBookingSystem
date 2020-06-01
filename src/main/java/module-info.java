module org.example {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    exports org.oop18;
    opens org.oop18.controllers to javafx.fxml;
    opens org.oop18.entities to javafx.base;
}