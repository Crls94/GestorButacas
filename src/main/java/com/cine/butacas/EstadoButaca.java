package com.cine.butacas;
/**
 * Enum que representa los posibles estados de una butaca dentro de la sala.
 * Usar un enum (en lugar de, por ejemplo, un char o un int "mágico") es una
 * práctica de POO que aporta seguridad de tipos: el compilador impide asignar
 * un estado inválido a una butaca.
 */
public enum EstadoButaca {

    LIBRE("L", "Libre"),
    RESERVADO("R", "Reservado"),
    OCUPADO("O", "Ocupado");

    // Letra corta usada para representar el estado en la matriz de texto (L, R, O)
    private final String letra;
    // Nombre descriptivo del estado, útil para mensajes al usuario
    private final String descripcion;

    EstadoButaca(String letra, String descripcion) {
        this.letra = letra;
        this.descripcion = descripcion;
    }

    public String getLetra() {
        return letra;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
