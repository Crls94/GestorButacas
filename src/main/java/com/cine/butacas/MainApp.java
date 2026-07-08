package com.cine.butacas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * Contiene el punto de entrada de la aplicación JavaFX y arma el "menú
 * principal" en forma de interfaz gráfica interactiva:
 *   1. Mostrar butacas de la sala  -> se ve siempre en la grilla central
 *   2. Reservar butaca             -> botón "Reservar"
 *   3. Cancelar reserva            -> botón "Cancelar reserva"
 *   4. Contar butacas libres       -> botón "Contar butacas libres"
 *   5. Salir                       -> botón "Salir"
 * Aquí se aprecia el tercer paradigma, PROGRAMACIÓN FUNCIONAL, en el uso
 * de expresiones lambda como manejadores de eventos (setOnAction(e -> ...))
 * en lugar de clases anónimas o Runnables tradicionales.
 */
public class MainApp extends Application {

    // Modelo de datos: la sala de cine (arreglo bidimensional de Butaca)
    private SalaCine sala;

    // Referencia a la butaca actualmente seleccionada por el usuario en la grilla
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;

    // Componentes de la interfaz que necesitan refrescarse dinámicamente
    private GridPane grillaButacas;
    private Label lblSeleccion;
    private Label lblContador;

    // Número de filas/columnas de la sala (coincide con el ejemplo del enunciado: 8x8)
    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;

    @Override
    public void start(Stage stage) {
        //Inicializar el modelo con el estado de ejemplo del enunciado
        sala = new SalaCine(estadoInicialDeEjemplo());

        // Construir la interfaz
        BorderPane raiz = new BorderPane();
        raiz.setPadding(new Insets(15));
        raiz.getStyleClass().add("root-pane");

        raiz.setTop(construirEncabezado());
        raiz.setCenter(construirZonaGrilla());
        raiz.setRight(construirPanelControl());

        Scene escena = new Scene(raiz, 980, 640);
        escena.getStylesheets().add(getClass().getResource("/com/cine/butacas/styles.css").toExternalForm());

        stage.setTitle("Gestión de Butacas de Cine");
        stage.setScene(escena);
        stage.show();
    }

        // Título y leyenda de colores en la parte superior de la ventana.
        private VBox construirEncabezado() {
        Label titulo = new Label("GESTIÓN DE BUTACAS DE CINE");
        titulo.getStyleClass().add("titulo-app");

        HBox leyenda = new HBox(20);
        leyenda.setAlignment(Pos.CENTER);
        leyenda.getChildren().addAll(
                crearItemLeyenda("Libre", "estado-libre"),
                crearItemLeyenda("Reservado", "estado-reservado"),
                crearItemLeyenda("Ocupado", "estado-ocupado")
        );
        leyenda.setPadding(new Insets(5, 0, 15, 0));

        VBox contenedor = new VBox(5, titulo, leyenda, new Separator());
        contenedor.setAlignment(Pos.CENTER);
        return contenedor;
    }

    private HBox crearItemLeyenda(String texto, String estiloCss) {
        Label cuadro = new Label("  ");
        cuadro.getStyleClass().addAll("celda-leyenda", estiloCss);
        Label nombre = new Label(texto);
        nombre.getStyleClass().add("texto-leyenda");
        HBox caja = new HBox(6, cuadro, nombre);
        caja.setAlignment(Pos.CENTER);
        return caja;
    }
    
    //Zona central: la representación visual de la sala (arreglo 2D de botones).
     private ScrollPane construirZonaGrilla() {
        grillaButacas = new GridPane();
        grillaButacas.setHgap(6);
        grillaButacas.setVgap(6);
        grillaButacas.setAlignment(Pos.CENTER);
        grillaButacas.setPadding(new Insets(15));

        refrescarGrilla(); // dibuja las butacas por primera vez
        ScrollPane scroll = new ScrollPane(grillaButacas);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.getStyleClass().add("scroll-grilla");
        return scroll;
    }

    //Reconstruye la grilla de butacas a partir del estado actual de "sala".
    //Recorre el arreglo bidimensional con bucles for (programación estructurada)
    //y usa lambdas para los eventos de clic (programación funcional).
    private void refrescarGrilla() {
        grillaButacas.getChildren().clear();
        // Encabezados de columna (C1, C2, ...)
        for (int c = 0; c < sala.getColumnas(); c++) {
            Label lbl = new Label("C" + (c + 1));
            lbl.getStyleClass().add("encabezado-grilla");
            grillaButacas.add(lbl, c + 1, 0);
        }
        // Recorrido del arreglo 2D de butacas
        for (int f = 0; f < sala.getFilas(); f++) {
            // Encabezado de fila (F1, F2, ...)
            Label lblFila = new Label("F" + (f + 1));
            lblFila.getStyleClass().add("encabezado-grilla");
            grillaButacas.add(lblFila, 0, f + 1);
            for (int c = 0; c < sala.getColumnas(); c++) {
                Butaca b = sala.getButaca(f, c);

                Button btnButaca = new Button(b.mostrarEstado());
                btnButaca.getStyleClass().add("celda-butaca");
                btnButaca.getStyleClass().add(claseCssPorEstado(b.getEstado()));

                // Resaltar visualmente la butaca actualmente seleccionada
                if (f == filaSeleccionada && c == columnaSeleccionada) {
                    btnButaca.getStyleClass().add("celda-seleccionada");
                }

                final int filaClic = f;
                final int columnaClic = c;

                // Programación funcional: lambda como manejador del evento clic
                btnButaca.setOnAction(evento -> seleccionarButaca(filaClic, columnaClic));

                grillaButacas.add(btnButaca, c + 1, f + 1);
            }
        }
    }

    private String claseCssPorEstado(EstadoButaca estado) {
        return switch (estado) {
            case LIBRE -> "estado-libre";
            case RESERVADO -> "estado-reservado";
            case OCUPADO -> "estado-ocupado";
        };
    }

    //Guarda la butaca elegida por el usuario y actualiza la etiqueta de selección.
    private void seleccionarButaca(int fila, int columna) {
        this.filaSeleccionada = fila;
        this.columnaSeleccionada = columna;
        lblSeleccion.setText("Butaca seleccionada: F" + (fila + 1) + "C" + (columna + 1)
                + "  (" + sala.getButaca(fila, columna).getEstado().getDescripcion() + ")");
        refrescarGrilla();
    }

    //Panel lateral derecho con las acciones del menú principal.
    private VBox construirPanelControl() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(0, 0, 0, 20));
        panel.setPrefWidth(260);
        panel.setAlignment(Pos.TOP_CENTER);

        Label instrucciones = new Label("1. Haga clic en una butaca para seleccionarla.\n"
                + "2. Use los botones para reservar, cancelar o contar.");
        instrucciones.setWrapText(true);
        instrucciones.getStyleClass().add("texto-instrucciones");

        lblSeleccion = new Label("Ninguna butaca seleccionada");
        lblSeleccion.getStyleClass().add("texto-seleccion");
        lblSeleccion.setWrapText(true);

        // Opción 2: Reservar butaca
        Button btnReservar = new Button("2. Reservar butaca");
        btnReservar.getStyleClass().add("boton-accion");
        btnReservar.setMaxWidth(Double.MAX_VALUE);
        btnReservar.setOnAction(e -> accionReservar());

        // Opción 3: Cancelar reserva
        Button btnCancelar = new Button("3. Cancelar reserva");
        btnCancelar.getStyleClass().add("boton-accion");
        btnCancelar.setMaxWidth(Double.MAX_VALUE);
        btnCancelar.setOnAction(e -> accionCancelar());

        // Opción 4: Contar butacas libres
        Button btnContar = new Button("4. Contar butacas libres");
        btnContar.getStyleClass().add("boton-accion");
        btnContar.setMaxWidth(Double.MAX_VALUE);
        btnContar.setOnAction(e -> accionContarLibres());

        lblContador = new Label("");
        lblContador.getStyleClass().add("texto-contador");
        lblContador.setWrapText(true);

        // Opción 1: Mostrar/Refrescar sala
        Button btnRefrescar = new Button("1. Refrescar sala");
        btnRefrescar.getStyleClass().add("boton-secundario");
        btnRefrescar.setMaxWidth(Double.MAX_VALUE);
        btnRefrescar.setOnAction(e -> refrescarGrilla());

        // Opción 5: Salir
        Button btnSalir = new Button("5. Salir");
        btnSalir.getStyleClass().add("boton-salir");
        btnSalir.setMaxWidth(Double.MAX_VALUE);
        btnSalir.setOnAction(e -> System.exit(0));

        panel.getChildren().addAll(
                instrucciones,
                new Separator(),
                lblSeleccion,
                btnReservar,
                btnCancelar,
                new Separator(),
                btnContar,
                lblContador,
                new Separator(),
                btnRefrescar,
                btnSalir
        );

        return panel;
    }

    // Acciones del menú (delegan la lógica a SalaCine y muestran alertas)
    private void accionReservar() {
        if (!haySeleccion()) return;
        String mensaje = sala.reservarButaca(filaSeleccionada, columnaSeleccionada);
        mostrarAlerta(mensaje.startsWith("✅") ? AlertType.INFORMATION : AlertType.WARNING, mensaje);
        refrescarGrilla();
    }

    private void accionCancelar() {
        if (!haySeleccion()) return;
        String mensaje = sala.cancelarReserva(filaSeleccionada, columnaSeleccionada);
        mostrarAlerta(mensaje.startsWith("✅") ? AlertType.INFORMATION : AlertType.WARNING, mensaje);
        refrescarGrilla();
    }

    private void accionContarLibres() {
        long libres = sala.contarButacasLibres();
        long reservadas = sala.contarButacasReservadas();
        long ocupadas = sala.contarButacasOcupadas();
        lblContador.setText("Libres: " + libres + " | Reservadas: " + reservadas + " | Ocupadas: " + ocupadas);
    }

    private boolean haySeleccion() {
        if (filaSeleccionada == -1 || columnaSeleccionada == -1) {
            mostrarAlerta(AlertType.WARNING, "⚠ Primero seleccione una butaca haciendo clic en la sala.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje);
        alerta.setHeaderText(null);
        alerta.setTitle("Gestión de Butacas");
        alerta.showAndWait();
    }

    // Estado inicial de demostración: reproduce exactamente la matriz de
    private String[][] estadoInicialDeEjemplo() {
        return new String[][]{
                {"L", "L", "L", "L", "L", "L", "L", "L"},
                {"L", "R", "L", "L", "L", "L", "R", "L"},
                {"L", "L", "L", "O", "O", "L", "L", "L"},
                {"R", "R", "L", "L", "L", "L", "R", "R"},
                {"L", "L", "L", "O", "O", "L", "L", "L"},
                {"L", "L", "O", "L", "L", "O", "L", "L"},
                {"R", "O", "L", "L", "L", "L", "O", "R"},
                {"L", "R", "O", "L", "L", "O", "R", "L"}
        };
    }
    //Punto de entrada del programa (equivalente al "main" de una app de consola).
    public static void main(String[] args) {
        launch(args);
    }
}
