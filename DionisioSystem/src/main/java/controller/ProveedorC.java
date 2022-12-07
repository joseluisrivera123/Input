package controller;

import dao.ProveedorImpl;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import lombok.Data;
import model.Proveedor;
import model.Ubigeo;
import report.ReportePersona;
import report.ReporteProveedor;
import services.ApiRuc;

@Data
@Named(value = "proveedorC")
@SessionScoped
public class ProveedorC implements Serializable {
    
    int Listado = 1;
    int caso = 3;
    Proveedor proveedor;
    ProveedorImpl dao;
    Ubigeo ubigeo;
    List<Proveedor> listadoProveedor;
    List<Ubigeo> listUbigeo;

    //variables de txt
    boolean rucencontrado = true;
    boolean seleccionarubigeo = true;
    boolean nombre = true;
    boolean apellido = true;
    boolean celular = true;
    private boolean disabled = true;
    private ApiRuc rucApi;
    private boolean direccion = true;
    private boolean provincia = true;
   private boolean distrito = true;
    

    public ProveedorC() {
        proveedor = new Proveedor();
        dao = new ProveedorImpl();
        ubigeo = new Ubigeo();
    }

    public void registrar() throws Exception {
        try {
            if (proveedor.getRuc().length() == 11 && proveedor.getCelularProveedor().length() == 9) {
                if (existe(proveedor, listadoProveedor)) {
                    dao.registrar(proveedor);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "se ha registrado"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "RUC repetido"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cantidad de digitos invalido en RUC o celular"));
            }
        } catch (Exception e) {
        }
    }

    public boolean existe(Proveedor modelo, List<Proveedor> listadoPro) {
        for (Proveedor prov : listadoPro) {
            if (modelo.getRuc().equals(prov.getRuc())) {
                return false;
            }
        }
        return true;
    }

    public void modificar() throws Exception {
        try {
            if (modificarExiste(proveedor, listadoProveedor)) {
                if (proveedor.getRuc().length() == 11 && proveedor.getCelularProveedor().length() == 9) {
                    dao.modificar(proveedor);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Se ha modificado"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Digistos invalidos en RUC o celular"));

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Numero de RUC repetido"));

            }
        } catch (Exception e) {
            System.out.println("Error al modificarC: " + e.getMessage());
        }
    }

    public boolean modificarExiste(Proveedor modelo, List<Proveedor> listadoPro) {
        for (Proveedor prove : listadoPro) {
            if (modelo.getRuc().equals(prove.getRuc())) {
                return modelo.getIdProvedor() == prove.getIdProvedor();
            }
        }
        return true;

    }

    public void eliminar() throws Exception {
        dao.eliminar(proveedor);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Se ha eliminado"));
    }

     public void BuscadorRucProveedor() throws Exception {
        try {
            rucApi.BuscadorRucProveedor(proveedor);
            if (rucApi.disabled == true) {
                this.disabled = true;
            } else {
                this.disabled = false;
                limpiar();
            }
        } catch (Exception e) {
            System.out.println("Error en buscarPorDNI_C " + e.getMessage());
        }
    }
    
    public void habilitar() {

        try {
            dao.restaurar(proveedor);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Producto habilitado"));
        } catch (Exception e) {
            System.out.println("Error en habilitar productosC " + e.getMessage());
        }
    }

    public List<Ubigeo> listarUbig() {
        try {
            listUbigeo = dao.listaUbigeo();
        } catch (Exception e) {
            System.out.println("Error al listUbigeoC: " + e.getMessage());
        }
        return listUbigeo;
    }

    public List<String> completUbi(String query) throws SQLException, Exception {
        ProveedorImpl proimpl = new ProveedorImpl();
        return proimpl.autocompleteUbigeo(query);
    }
    
    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ReportePersona reporte = new ReportePersona();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("Reportes/Proveedor.jasper");
        reporte.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }

//    public void limpiar() {
//        pro = new Proveedor();
//    }
        
        /*Método para restaurar a la persona*/
    public void restaurar() throws Exception {
        try {
            dao.restaurar(proveedor);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("error en restaurar/ProveedorC " + e);
        }
    }

    public void limpiar() {
        proveedor = new Proveedor();
        nombre = true;
        apellido = true;
        celular = true;
    }

    public void listar() {
        try {
            listadoProveedor = dao.listar(caso);

        } catch (Exception e) {
            System.out.println("Error al listarC: " + e.getMessage());
        }
    }

    public void buscarApi() {
        try {
            ApiRuc.BuscadorRucProveedor(proveedor);
        } catch (Exception e) {
            System.out.println("Error al buscar ApiC :" + e.getMessage());
        }
    }
//    public void activarInput() {
//        try {
//            int respuests = ApiRuc.BuscadorRucProveedor(proveedor);
//            System.out.println(respuests);
//            if (respuests == 200) {
//                rucencontrado = true;
//                nombre = false;
//                apellido = false;
//                celular = false;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bien", "se busco correctamente"));
//            } else {
//                rucencontrado = false;
//                seleccionarubigeo = false;
//                nombre = false;
//                apellido = false;
//                celular = false;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Success", "No se encontro datos"));
//            }
//        } catch (Exception e) {
//            rucencontrado = false;
//            seleccionarubigeo = false;
//            nombre = false;
//            apellido = false;
//            celular = false;
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de RUC ingresado es inválido"));
//        }
//
//    }
}
