package model;

import lombok.Data;

@Data
public class CompraDetalle {

    String detalle;
    int cantidad;
    int idCompra;
    int idInsumo;
    String nombreCom;
    double precio;
    int itemCom;
    double subtotalCompra;
    int nuevaCantidad;
    double nuevoPrecio;
}
