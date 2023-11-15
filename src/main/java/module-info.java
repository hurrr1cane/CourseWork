module com.mhorak.coursework {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.mhorak.coursework to javafx.fxml;
    exports com.mhorak.coursework;

    opens com.mhorak.coursework.model to javafx.base;
}