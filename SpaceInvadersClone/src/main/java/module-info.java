module com.example.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    requires javafx.media;


    opens com.example.spaceinvaders to javafx.fxml;
    exports com.example.spaceinvaders;
}