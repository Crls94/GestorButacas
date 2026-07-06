// App.java
package com.gestorbutacas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/gestorbutacas/main.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Gestión de Butacas de Cine");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}