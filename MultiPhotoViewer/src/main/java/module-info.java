module com.example.multiphotoviewer {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    
                            
    opens com.example.multiphotoviewer to javafx.fxml;
    exports com.example.multiphotoviewer;
}