package com.cine.butacas;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Representa la sala completa de cine mediante un ARREGLO BIDIMENSIONAL
 * Aquí se combinan dos paradigmas adicionales sobre la base de la POO:
 *  - PROGRAMACIÓN ESTRUCTURADA: el llenado inicial de la matriz y las
 *    validaciones de fila/columna usan estructuras de control clásicas
 *    (bucles for anidados, if/else) para recorrer el arreglo 2D.
 *  - PROGRAMACIÓN FUNCIONAL: el conteo de butacas se resuelve con la
 *    Stream API (flatMap, filter, count) en lugar de un bucle manual,
 *    usando funciones puras sin efectos secundarios.
 */
public class SalaCine {
    // Arreglo bidimensional: cada celda es un objeto Butaca
    private final Butaca[][] butacas;
    private final int filas;
    private final int columnas;

    /**
     * Crea una sala de cine vacía (todas las butacas en estado LIBRE)
     * con el número de filas y columnas indicado.
     */
    public SalaCine(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.butacas = new Butaca[filas][columnas];

        // Programación estructurada: doble bucle for para inicializar el arreglo 2D
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                butacas[f][c] = new Butaca(f, c);
            }
        }
    }

    /**
     * Constructor alterno: crea la sala a partir de una matriz de letras
     * ('L', 'R', 'O'), útil para cargar un estado inicial de demostración
     */
    public SalaCine(String[][] estadosIniciales) {
        this.filas = estadosIniciales.length;
        this.columnas = estadosIniciales[0].length;
        this.butacas = new Butaca[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Butaca b = new Butaca(f, c);
                switch (estadosIniciales[f][c]) {
                    case "R" -> b.reservar();                 // LIBRE -> RESERVADO
                    case "O" -> { b.reservar(); b.ocupar(); }  // LIBRE -> RESERVADO -> OCUPADO
                    default -> { /* ya nace LIBRE, no se hace nada */ }
                }
                butacas[f][c] = b;
            }
        }
    }

    // Validación de coordenadas (programación estructurada: if / else)
    
    /**
     * Verifica que la fila y columna ingresadas por el usuario existan
     * dentro de la sala. Regla del sistema: fila y columna deben ser válidas.
     */
    public boolean esCoordenadaValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    public Butaca getButaca(int fila, int columna) {
        if (!esCoordenadaValida(fila, columna)) {
            throw new IndexOutOfBoundsException("Fila o columna fuera de rango.");
        }
        return butacas[fila][columna];
    }

    // Operaciones principales sobre la sala
    /**
     * Intenta reservar la butaca indicada.
     * @return un mensaje claro para mostrar al usuario en la interfaz.
     */
    public String reservarButaca(int fila, int columna) {
        if (!esCoordenadaValida(fila, columna)) {
            return "❌ Coordenadas inválidas. Ingrese una fila y columna dentro del rango de la sala.";
        }
        Butaca b = butacas[fila][columna];

        if (b.estaOcupada()) {
            return "❌ No se puede reservar: la butaca F" + (fila + 1) + "C" + (columna + 1) + " ya está OCUPADA.";
        }
        if (b.estaReservada()) {
            return "❌ No se puede reservar: la butaca F" + (fila + 1) + "C" + (columna + 1) + " ya está RESERVADA.";
        }

        boolean exito = b.reservar();
        return exito
                ? "✅ Butaca F" + (fila + 1) + "C" + (columna + 1) + " reservada con éxito."
                : "❌ No se pudo reservar la butaca.";
    }

    //Cancela la reserva de la butaca indicada (solo si estaba RESERVADA).
    public String cancelarReserva(int fila, int columna) {
        if (!esCoordenadaValida(fila, columna)) {
            return "❌ Coordenadas inválidas. Ingrese una fila y columna dentro del rango de la sala.";
        }
        Butaca b = butacas[fila][columna];

        if (!b.estaReservada()) {
            return "❌ No se puede cancelar: la butaca F" + (fila + 1) + "C" + (columna + 1)
                    + " no tiene una reserva activa (estado actual: " + b.getEstado().getDescripcion() + ").";
        }

        boolean exito = b.liberar();
        return exito
                ? "✅ Reserva de la butaca F" + (fila + 1) + "C" + (columna + 1) + " cancelada. Ahora está LIBRE."
                : "❌ No se pudo cancelar la reserva.";
    }

    /**
     * Marca como OCUPADA una butaca previamente reservada
     * (funcionalidad adicional útil al momento de "hacer check-in" en sala).
     */
    public String ocuparButaca(int fila, int columna) {
        if (!esCoordenadaValida(fila, columna)) {
            return "❌ Coordenadas inválidas.";
        }
        Butaca b = butacas[fila][columna];
        boolean exito = b.ocupar();
        return exito
                ? "✅ Butaca F" + (fila + 1) + "C" + (columna + 1) + " marcada como OCUPADA."
                : "❌ Solo se puede ocupar una butaca que esté RESERVADA.";
    }

    // Conteos con PROGRAMACIÓN FUNCIONAL (Stream API)
    /**
     * Cuenta las butacas libres usando streams: convierte la matriz 2D en un
     * stream plano de butacas (flatMap) y filtra por estado (filter),
     * sin usar bucles ni variables mutables (estilo funcional puro).
     */
    public long contarButacasLibres() {
        return contarPorEstado(EstadoButaca.LIBRE);
    }

    public long contarButacasReservadas() {
        return contarPorEstado(EstadoButaca.RESERVADO);
    }

    public long contarButacasOcupadas() {
        return contarPorEstado(EstadoButaca.OCUPADO);
    }

    //Método genérico funcional: cuenta cuántas butacas tienen un estado dado.
    private long contarPorEstado(EstadoButaca estadoBuscado) {
        return Arrays.stream(butacas)                 // Stream<Butaca[]>  (una por fila)
                .flatMap(Stream::of)                   // Stream<Butaca>    (todas las butacas, aplanado)
                .filter(b -> b.getEstado() == estadoBuscado) // solo las del estado buscado
                .count();                               // cantidad total
    }

    // Representación de texto de la sala (útil para consola/logs)
    /**
     * Construye una representación en texto plano de la sala,
     * recorriendo el arreglo 2D con bucles for (estilo estructurado).
     */
    public String mostrarSala() {
        StringBuilder sb = new StringBuilder();
        sb.append("     ");
        for (int c = 0; c < columnas; c++) {
            sb.append(String.format("C%-3d", c + 1));
        }
        sb.append("\n");

        for (int f = 0; f < filas; f++) {
            sb.append(String.format("F%-3d ", f + 1));
            for (int c = 0; c < columnas; c++) {
                sb.append(" ").append(butacas[f][c].mostrarEstado()).append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Getters
    public Butaca[][] getButacas() {
        return butacas;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}
