package model;

import java.util.Date;
import java.util.GregorianCalendar;
import lombok.Data;

@Data
public class Compra {
    int idCompra;
    Date fecha = GregorianCalendar.getInstance().getTime();
    String tipoPago;
    int idProveedor;
    int idAdministrador;
    int cantidad;
    
    /*Variable de tipo decimal*/
    double precio;
    double subtotal;
    
    /*Variable para consumir el api*/
    double cambio;
    double convertido;
    double pago;
    double vuelto;
    
    /*Nombre(s) y apellidos*/
    String administrador;
    String proveedor;
    String insumo;
}
