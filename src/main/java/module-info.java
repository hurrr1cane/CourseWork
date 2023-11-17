//module com.mhorak.coursework {
//    requires javafx.controls;
//    requires javafx.fxml;
//
//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires org.kordamp.bootstrapfx.core;
//    requires lombok;
//
//    opens com.mhorak.coursework to javafx.fxml;
//    exports com.mhorak.coursework.app;
//
//    opens com.mhorak.coursework.model to javafx.base;
//    exports com.mhorak.coursework.controller;
//    opens com.mhorak.coursework.controller to javafx.fxml;
//    exports com.mhorak.coursework.tool;
//    opens com.mhorak.coursework.tool to javafx.fxml;
//    exports com.mhorak.coursework;
//    opens com.mhorak.coursework.app to javafx.fxml;
//}


module com.mhorak.coursework {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.mhorak.coursework to javafx.fxml;
    exports com.mhorak.coursework;

    opens com.mhorak.coursework.controller to javafx.fxml;
    exports com.mhorak.coursework.controller;

    opens com.mhorak.coursework.exception to javafx.fxml;
    exports com.mhorak.coursework.exception;

    opens com.mhorak.coursework.model to javafx.base;
    exports com.mhorak.coursework.model;

    opens com.mhorak.coursework.tool to javafx.fxml;
    exports com.mhorak.coursework.tool;


}
