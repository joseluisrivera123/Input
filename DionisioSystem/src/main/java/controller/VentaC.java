package controller;

import dao.VentaImpl;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import lombok.Data;
import model.Persona;
import model.Producto;
import model.Venta;
import model.VentaDetalle;
import model.cambio;
import report.ReporteVenta;
import services.api2;


/*@author Jesus Sulca*/
@Data
@SessionScoped
@Named(value = "ventaC")
public class VentaC implements Serializable {

    /*Variables para la tabla temporal*/
    int item;
    int idproducto;
    String nombre;
    int stock;
    double precio;
    int cantidad;
    double subtotal;
    double total;
    String detalle;

    /* Creando objeto al modelo */
    VentaDetalle ventadetalle;
    Persona persona;
    Producto producto;
    Venta venta;
    cambio api;

    /*Creando objeto al dao*/
    VentaImpl dao;

    /*Creando listas de los modelos*/
    List<Persona> listadoPersona;
    List<Producto> ListarProducto;
    List<VentaDetalle> ListarVD = new ArrayList();
    List<Venta> listarVenta = new ArrayList();

    public VentaC() {
        persona = new Persona();
        dao = new VentaImpl();
        producto = new Producto();
        ventadetalle = new VentaDetalle();
        venta = new Venta();
        api = new cambio();
    }

    public void registrar() throws Exception {
        try {
            if (ListarVD.isEmpty() || persona.getIdpersona() == 0) {
                System.out.println("Hola");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cliente no seleccionado"));
            } else {
                System.out.println("Hola");
                venta.setIdCliente(persona.getIdpersona());
                dao.registrar(venta);
                System.out.println(venta.getFechaVenta());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Venta Registrado con éxito"));
                registrarDetalle();
                LimpiarTodo();
            }

        } catch (Exception e) {
            System.out.println("Error en registrar/VentaC");
        }
    }

    public List<String> autocompletePrueba(String query) throws Exception {
        try {
            return dao.autocompletar(query);
        } catch (Exception e) {
            System.out.println("Error en ventaC/Controlador" + e.getMessage());
            throw e;
        }
    }

    public void registrarDetalle() throws Exception {
        /*venmax es el id de la cantidad final de la venta + 1*/
        int venmax = dao.ventasMaximas();
        for (int i = 0; i < ListarVD.size(); i++) {
            ventadetalle = new VentaDetalle();
            ventadetalle.setCantidad(ListarVD.get(i).getCantidad());
            ventadetalle.setIdVenta(venmax);
            ventadetalle.setIdProducto(ListarVD.get(i).getIdProducto());
            dao.registrarVentaDetalle(ventadetalle);
            dao.ActualizarStock(ventadetalle);
        }
    }

    public void LimpiarTodo() {
        producto = new Producto();
        ListarVD = new ArrayList();
        persona = new Persona();
        total = 0;
    }

    public void cargarDatos() {
        try {
            if (producto.getNombre() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor , Ingrese el nombre del Producto"));
                Limpiar();
            } else {
                dao.mostrarDatos(producto);
            }
        } catch (Exception e) {
            System.out.println("Error en CargarDatos/VentaC : " + e);
        }
    }

    public void listarVenta() {
        try {
            listarVenta = dao.ListarVentas();

        } catch (Exception e) {
            System.out.println("Error en listarVenta/VentaC : " + e);
        }
    }

    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, Exception {
        String ruta = "Reportes/BoletaDionisio.jasper";
        int idven = dao.ventasMaximas();
        ReporteVenta reporte = new ReporteVenta();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath(ruta);
        String numeroinformesocial = String.valueOf(idven);
        reporte.getReportePdf(root, numeroinformesocial);
        FacesContext.getCurrentInstance().responseComplete();
    }
           

    public void verReportePDFESTT(int codigo) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ReporteVenta reporte = new ReporteVenta();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("Reportes/BoletaV.jasper");
        String numeroinformesocial = String.valueOf(codigo);
        reporte.getReportePdf(root, numeroinformesocial);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void validadorProductoRepetido(Producto producto) {
        int indice = 0;
        int cantidad = 0;
        if (producto.getIdproducto() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "PRODUCTO NO SELECCIONADO"));
        } else {
            if (producto.getCantidad() > 0) {
                try {
                    if (ListarVD.isEmpty()) {
                        if (producto.getCantidad() > producto.getStock()) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cantidad de producto no disponible"));
                        } else {
                            tempTable();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));
                        }
                    } else {
                        for (VentaDetalle ventaDetalle : ListarVD) {
                            if (ventaDetalle.getIdProducto() == producto.getIdproducto()) {
                                cantidad = ListarVD.get(indice).getCantidad() + producto.getCantidad();
                                if (cantidad <= producto.getStock()) {
                                    subtotal = ListarVD.get(indice).getPrecio() * cantidad;
                                    total = 0;
                                    ventaDetalle.setCantidad(cantidad);
                                    ventaDetalle.setSubtotal(subtotal);
                                    ListarVD.set(indice, ventaDetalle);
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));
                                    CalcularTotalVenta();
                                    producto = new Producto();
                                    break;
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OK", "cantidad de producto no disponible"));
                                }

                            } else {
                                indice++;
                                if (indice == ListarVD.size()) {
                                    if (producto.getCantidad() > producto.getStock()) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cantidad de producto no disponible"));

                                    } else {
                                        tempTable();
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error  en validador producto repetido VentasC : " + e);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese la la cantidad de venta"));
            }
        }
    }

    public void CalcularTotalVenta() {
        for (int i = 0; i < ListarVD.size(); i++) {
            total = total + ListarVD.get(i).getSubtotal();
        }
    }

    public void tempTable() {
        total = 0.0;
        item = item + 1;
        nombre = producto.getNombre();
        detalle = producto.getDetalle();
        cantidad = producto.getCantidad();
        precio = producto.getPrecio();
        idproducto = producto.getIdproducto();
        subtotal = cantidad * precio;

        ventadetalle = new VentaDetalle();

        ventadetalle.setItem(item);
        ventadetalle.setIdProducto(idproducto);
        ventadetalle.setCantidad(cantidad);
        ventadetalle.setDetalle(detalle);
        ventadetalle.setNombreProducto(nombre);
        ventadetalle.setPrecio(precio);
        ventadetalle.setSubtotal(subtotal);
        ListarVD.add(ventadetalle);
        CalcularTotalVenta();
        producto = new Producto();
    }

    public void Limpiar() {
        producto = new Producto();
        ListarVD = new ArrayList();
        persona = new Persona();
    }

    public void Filtrado() throws Exception {
        try {
            if (persona.getDni().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Por favor ingrese un DNI"));
                Limpiar();
            } else {
                dao.filtrarCliente(persona);
                if (dao.filtrarClientes(persona)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ok", "Cliente encontrado"));

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cliente no encontrado"));
                    Limpiar();
                }
            }
        } catch (Exception e) {
            System.out.println("Error en filtrar : " + e);
        }
    }

    public void prueba() {
        try {
            api2.ApiDolar(venta);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public void convertidorSoles() {
        try {
            venta.setConvertido(venta.getCambio() * venta.getPago());
            venta.setVuelto(venta.getConvertido() - total);
        } catch (Exception e) {
            System.out.println("ConvertidorSoles/VentaC : " + e);
        }
    }

    public boolean existe(Persona persona, List<Persona> listarPersona) {
        for (Persona cli : listarPersona) {
            if (persona.getDni().equals(cli.getDni())) {
                return false;
            }
        }
        return true;
    }
}
