// SalaCine.java
package com.gestorbutacas.model;

public class SalaCine {
    private Butaca[][] butacas;
    private int filas;
    private int columnas;

    public SalaCine(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.butacas = new Butaca[filas][columnas];
        for (int f = 0; f < filas; f++)
            for (int c = 0; c < columnas; c++)
                butacas[f][c] = new Butaca(f, c);
    }

    public String mostrarSala() {
        StringBuilder sb = new StringBuilder();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                sb.append(butacas[f][c].mostrarEstado()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean reservarButaca(int fila, int columna) {
        if (!validarPosicion(fila, columna)) return false;
        return butacas[fila][columna].reservar();
    }

    public boolean cancelarReserva(int fila, int columna) {
        if (!validarPosicion(fila, columna)) return false;
        return butacas[fila][columna].liberar();
    }

    public int contarButacasLibres() {
        int contador = 0;
        for (int f = 0; f < filas; f++)
            for (int c = 0; c < columnas; c++)
                if (butacas[f][c].estado() == Butaca.Estado.LIBRE) contador++;
        return contador;
    }

    private boolean validarPosicion(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }
}