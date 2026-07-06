// Butaca.java
package com.gestorbutacas.model;

public class Butaca {
    public enum Estado { LIBRE, RESERVADO, OCUPADO }

    private int fila;
    private int columna;
    private Estado estado;

    public Butaca(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.estado = Estado.LIBRE;
    }

    public boolean reservar() {
        if (estado == Estado.LIBRE) {
            estado = Estado.RESERVADO;
            return true;
        }
        return false;
    }

    public boolean ocupar() {
        if (estado != Estado.OCUPADO) {
            estado = Estado.OCUPADO;
            return true;
        }
        return false;
    }

    public boolean liberar() {
        if (estado == Estado.RESERVADO) {
            estado = Estado.LIBRE;
            return true;
        }
        return false;
    }

    public Estado estado() { return estado; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }

    public String mostrarEstado() {
        switch (estado) {
            case LIBRE: return "L";
            case RESERVADO: return "R";
            case OCUPADO: return "O";
            default: return "?";
        }
    }
}