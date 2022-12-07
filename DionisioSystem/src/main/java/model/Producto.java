package model;

import lombok.Data;

@Data
public class Producto {

    int idproducto;
    String nombre;
    double precio;
    String tipo;
    String tamaño;
    int stock;
    String estado;
    String detalle;
    int cantidad;
}
