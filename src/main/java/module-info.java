module com.yobel.optimus {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Java SE (javax.print, etc.)
    requires java.desktop;

    // Librerías externas
    requires com.google.gson;          // Gson 2.10.1
    requires okhttp3;                  // OkHttp 4.12.0
    requires com.google.zxing;         // ZXing core 3.5.3
    requires com.google.zxing.javase;  // ZXing javase 3.5.3

    // Abre paquetes a JavaFX (FXML usa reflexión)
    opens com.yobel.optimus to javafx.graphics, javafx.fxml;
    opens com.yobel.optimus.controller to javafx.fxml;

    // 🔑 Abre modelos a Gson (reflexión a campos privados)
    opens com.yobel.optimus.model.entity   to com.google.gson;
    opens com.yobel.optimus.model.response to com.google.gson;
    opens com.yobel.optimus.model.request  to com.google.gson;
    // si tienes más (dto, vo, etc.) ábrelos también:
    // opens com.yobel.optimus.model.dto to com.google.gson;

    // Exporta si necesitas
    exports com.yobel.optimus;
}