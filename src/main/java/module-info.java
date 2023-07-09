module com.example.phanmemqldt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    opens com.example.phanmemqldt.data to javafx.base;





    opens com.example.phanmemqldt to javafx.fxml;

    exports com.example.phanmemqldt;
}

