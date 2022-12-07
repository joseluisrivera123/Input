package model;

import java.util.Date;
import lombok.Data;

@Data
public class Persona {

    int idpersona;
    String nombre;
    String apellidopaterno;
    String apellidomaterno;
    String email;
    String celular;
    String dni;
    String direccion;
    String estado;
    Date fechanacimiento;
    String clave;
    String tipo;
    String ubigeo;
    String mensaje;
}
