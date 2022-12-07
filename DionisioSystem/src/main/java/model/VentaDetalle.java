
package model;

import java.util.Date;
import java.util.GregorianCalendar;
import lombok.Data;

@Data
public class VentaDetalle {
 int idVentaDetalle;
 int cantidad;
 int idVenta;
 int idProducto;
 int item;
 String detalle;
 String nombreProducto;
 double precio;
 double subtotal;
 Date fechapruebas = GregorianCalendar.getInstance().getTime();
}
