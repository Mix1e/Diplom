module com.diplom.styleidentifier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires quifft;
    requires com.google.gson;

    opens com.diplom.styleidentifier to javafx.fxml;
    exports com.diplom.styleidentifier;
}