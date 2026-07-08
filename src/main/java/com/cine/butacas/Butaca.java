package com.cine.butacas;
/**
 * Representa un asiento individual de la sala de cine.
 * Aplica el paradigma de PROGRAMACIÓN ORIENTADA A OBJETOS (POO):
 *  - Encapsulamiento: los atributos son privados y se accede a ellos mediante
 *    métodos públicos (getters) y comportamientos (reservar, ocupar, liberar).
 *  - Abstracción: la clase modela un concepto real (un asiento de cine) con
 *    sus propiedades y su comportamiento.
 */
public class Butaca {
    // Atributos: posición fija de la butaca dentro de la sala (no cambian tras crearla)
    private final int fila;
    private final int columna;

    // Estado actual de la butaca (puede cambiar en el tiempo)
    private EstadoButaca estado;

    /**
     * Constructor: toda butaca nace en estado LIBRE.
     *
     * @param fila    fila de la butaca (0-index internamente)
     * @param columna columna de la butaca (0-index internamente)
     */
    public Butaca(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.estado = EstadoButaca.LIBRE;
    }

    // ---------------------------------------------------------------
    // Métodos de comportamiento (lógica de negocio de la butaca)
    // ---------------------------------------------------------------

    /**
     * Intenta reservar la butaca.
     * Regla del sistema: solo se puede reservar si está LIBRE
     * (no se puede reservar una butaca ocupada ni una ya reservada).
     * @return true si la reserva se realizó con éxito, false si no cumplía las reglas.
     */
    public boolean reservar() {
        if (this.estado == EstadoButaca.LIBRE) {
            this.estado = EstadoButaca.RESERVADO;
            return true;
        }
        return false;
    }

    /**
     * Marca la butaca como OCUPADA.
     * Regla de negocio típica: una butaca pasa a ocupada normalmente desde
     * el estado RESERVADO (por ejemplo, cuando el cliente llega a la sala).
     *
     * @return true si el cambio se realizó, false en caso contrario.
     */
    public boolean ocupar() {
        if (this.estado == EstadoButaca.RESERVADO) {
            this.estado = EstadoButaca.OCUPADO;
            return true;
        }
        return false;
    }

    /**
     * Cancela la reserva de la butaca y la devuelve a estado LIBRE.
     * Regla del sistema: solo se puede cancelar si la butaca está RESERVADA.
    * @return true si se canceló la reserva, false si no estaba reservada.
     */
    public boolean liberar() {
        if (this.estado == EstadoButaca.RESERVADO) {
            this.estado = EstadoButaca.LIBRE;
            return true;
        }
        return false;
    }

    // ---------------------------------------------------------------
    // Consultas de estado
    // ---------------------------------------------------------------

    public boolean estaLibre() {
        return this.estado == EstadoButaca.LIBRE;
    }

    public boolean estaReservada() {
        return this.estado == EstadoButaca.RESERVADO;
    }

    public boolean estaOcupada() {
        return this.estado == EstadoButaca.OCUPADO;
    }

    /**
     * Devuelve la letra que representa el estado actual (L, R u O),
     * tal como se usa en la representación visual de la sala.
     */
    public String mostrarEstado() {
        return this.estado.getLetra();
    }

    // ---------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public EstadoButaca getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        // Ej: "F3C5 [Reservado]" -> útil para depuración/logs
        return String.format("F%dC%d [%s]", fila + 1, columna + 1, estado.getDescripcion());
    }
}
