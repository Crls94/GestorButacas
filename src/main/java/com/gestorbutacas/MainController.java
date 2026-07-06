package com.gestorbutacas;

import com.gestorbutacas.model.SalaCine;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {
    @FXML private TextArea areaSala;
    private final SalaCine sala = new SalaCine(5, 6);

    @FXML
    private void mostrarSala() {
        areaSala.setText(sala.mostrarSala());
    }
}