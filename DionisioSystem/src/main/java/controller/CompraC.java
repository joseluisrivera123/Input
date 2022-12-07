package controller;

import dao.CompraImpl;
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
import model.Compra;
import model.CompraDetalle;
import model.Insumo;
import model.Producto;
import model.Proveedor;
import report.ReporteVenta;

@Data
@Named(value = "compraC")
@SessionScoped
public class CompraC implements Serializable {

    // variables para la tablaTemporal compra
    private double total;
    private int itemCom;
    private double subtotalCompra;

    //modelo
    Proveedor proveedor;
    Insumo insumo;
    CompraImpl dao;
    Compra compra;
    CompraDetalle comDetalle;
    //listado de modelo
    List<Proveedor> listProveedor;
    List<Insumo> listInsumo;
    List<CompraDetalle> listDetalle;
    List<Compra> listadoCompra;

    public CompraC() {

        System.out.println("Iniciando constructor");
        insumo = new Insumo();
        this.proveedor = new Proveedor();
        this.dao = new CompraImpl();
        this.compra = new Compra();
        this.comDetalle = new CompraDetalle();
        this.listDetalle = new ArrayList();
    }

    public void registroCompra() throws Exception {
        try {
            compra.setIdProveedor(proveedor.getIdProvedor());
            dao.registrar(compra);
            registroDetalle();
            limpiar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "se ha registrado"));
        } catch (Exception e) {
            System.out.println("Error al registrar CompraC: " + e.getMessage());
        }
    }

    public void registroDetalle() throws Exception {
        int numeroFilas = dao.maxCompra();
        for (int i = 0; i < listDetalle.size(); i++) {
            comDetalle = new CompraDetalle();
            comDetalle.setCantidad(listDetalle.get(i).getCantidad());
            comDetalle.setIdCompra(numeroFilas);
            comDetalle.setIdInsumo(listDetalle.get(i).getIdInsumo());
            dao.registrarCompraDetalle(comDetalle);
            dao.ActualizarStock(comDetalle);
        }
    }

    public void filtrar() throws Exception {
        try {
            System.out.println("estoy pasando por aqui");
            dao.FiltrarProveedor(proveedor);

        } catch (Exception e) {
            System.out.println("Error en FiltrarProovedorC: " + e.getMessage());
        }

    }

    public List<String> autocompleteProveedor(String query) throws Exception {
        try {
            return dao.autocompletarProveedor(query);

        } catch (Exception e) {
            System.out.println("Error en autocompletarclienteC " + e.getMessage());
            throw e;
        }
    }

    public void buscarInsumos() throws Exception {
        try {
            insumo.setNombredeinsumo(insumo.getNombredeinsumo());
            dao.mostrarDatos(insumo);
            System.out.println(insumo.getIdinsumo());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Se busco correctamente el producto"));
        } catch (Exception e) {
            System.out.println("Error al buscarInsumos: " + e.getMessage());
        }
    }
    public void verReportePDFESTT(int codigo) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ReporteVenta reporte = new ReporteVenta();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("Reportes/BoletaC.jasper");
        String numeroinformesocial = String.valueOf(codigo);
        reporte.getReportePdf(root, numeroinformesocial);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public List<String> autocompletePrueba(String query) throws Exception {
        try {

            return dao.autocompletar(query);
        } catch (Exception e) {
            System.out.println("Erro please" + e.getMessage());
            throw e;
        }
    }

    public void tablaTemporal() {
        total = 0.0;
        itemCom = itemCom + 1;
        subtotalCompra = insumo.getPreciodeinsumo()* insumo.getCantidaddeinsumo();

        comDetalle = new CompraDetalle();

        comDetalle.setItemCom(itemCom);
        comDetalle.setIdInsumo(insumo.getIdinsumo());
        comDetalle.setNombreCom(insumo.getNombredeinsumo());
        comDetalle.setPrecio(insumo.getPreciodeinsumo());
        comDetalle.setDetalle(insumo.getDetalledeinsumo());
        comDetalle.setCantidad(insumo.getCantidaddeinsumo());
        comDetalle.setSubtotalCompra(subtotalCompra);
        listDetalle.add(comDetalle);
        calcularTotal();
        insumo = new Insumo();

    }

//    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
//        int idven = dao.maxCompra();
//        System.out.println("venta " + idven);
//        reportesCompras reporte = new reportesCompras();
//        FacesContext facescontext = FacesContext.getCurrentInstance();
//        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
//        String root = servletcontext.getRealPath("Reportes/Boleta.jasper");
//        String numeroinformesocial = String.valueOf(idven);
//        reporte.getReportePdf(root, numeroinformesocial);
//        FacesContext.getCurrentInstance().responseComplete();
//    }
    public void actualizarTablaTemporal() {
        try {
            int i = 0;
            for (CompraDetalle compradetalle : listDetalle) {
                if (listDetalle.get(i).getNuevaCantidad() > 0) {
                    compradetalle.setCantidad(listDetalle.get(i).getNuevaCantidad());
                    compradetalle.setNuevaCantidad(0);
                    compradetalle.setSubtotalCompra(listDetalle.get(i).getCantidad() * listDetalle.get(i).getPrecio());
                    total = 0;
                    listDetalle.set(i, compradetalle);
                    calcularTotal();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "CANTIDADdaaa MODIFICADO"));
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            System.err.println("Error en actualizar cantidad " + e.getMessage());
        }

    }

    public void calcularTotal() {
        for (int i = 0; i < listDetalle.size(); i++) {
            total = total + listDetalle.get(i).getSubtotalCompra();
        }
    }

    public void eliminarInsumo(int Codigo) {
        int i = 0;
        try {
            for (CompraDetalle CompraDetalle : listDetalle) {
                if (CompraDetalle.getIdInsumo()== Codigo) {
                    total = total - listDetalle.get(i).getSubtotalCompra();
                    listDetalle.remove(i);
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error al eliminarProducto/CompraC: " + e);
        }
    }

    public void listarCom() {
        try {
            listadoCompra = dao.ListarCompra();
        } catch (Exception e) {
            System.out.println("Error el listarCom/CompraC : " + e);
        }
    }

    public void insumoNull() {
        if (insumo.getIdinsumo()== 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar producto"));
        } else {
            cantidadNull();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto seleccionado"));
        }
    }

    public void cantidadNull() {
        if (insumo.getCantidaddeinsumo()== 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese cantidad"));
        } else {
            insumoRepit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Se ingreso cantidad"));
        }
    }

    public void insumoRepit() {
        int indice = 0;
        if (listDetalle.isEmpty()) {
            tablaTemporal();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Insumo agregado con exito"));
        } else {
            for (CompraDetalle compradetalle : listDetalle) {
                if (compradetalle.getIdInsumo()== (insumo.getIdinsumo())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insumo ya registrado"));
                } else {
                    indice++;
                    if (indice == listDetalle.size()) {
                        tablaTemporal();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Insumo agregado con éxito"));
                        break;
                    }
                }
            }
        }
    }
    

    public void limpiar() {
        listDetalle = new ArrayList();
        insumo = new Insumo();
        comDetalle.setCantidad(0);
        proveedor = new Proveedor();
        total = 0;
    }
}
