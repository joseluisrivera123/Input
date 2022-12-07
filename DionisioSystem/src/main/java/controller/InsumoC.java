package controller;

import dao.InsumoImpl;
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
import model.Insumo;
import report.ReportePersona;

@Data
@Named(value = "insumoC")
@SessionScoped
public class InsumoC implements Serializable {

    int listado = 1;

    Insumo insumo;
    InsumoImpl dao;
    List<Insumo> listadoInsumo;

    public InsumoC() {
        insumo = new Insumo();
        dao = new InsumoImpl();
    }

    public void registrar() throws Exception {
        try {
            dao.registrar(insumo);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en registrar/InsumoC " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        try {
            insumo.setNombredeinsumo(caseMayuscula(insumo.getNombredeinsumo()));
            insumo.setDetalledeinsumo(caseMinuscula(insumo.getDetalledeinsumo()));
            dao.modificar(insumo);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
            limpiar();
            listar();

        } catch (Exception e) {
            System.out.println("Error en modificar/ProductoC" + e);
        }
    }

    public void eliminar() throws Exception {
        try {
            dao.eliminar(insumo);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OK", "Eliminado con éxito"));
            limpiar();
            listar();

        } catch (Exception e) {
            System.out.println("Error en eliminar/InsumoC" + e.getMessage());
        }
    }

    public String caseMinuscula(String camelcase) {
        char ch[] = camelcase.toCharArray();
        for (int i = 0; i < camelcase.length(); i++) {
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {  // Si se encuentra el primer carácter de una palabra
                if (ch[i] >= 'A' && ch[i] <= 'Z') {      // Si está en mayúsculas
                    ch[i] = (char) (ch[i] - 'A' + 'a');  // Convertir en minúsculas
                }
            } // Si aparte del primer carácter cualquiera está en mayúsculas
            else if (ch[i] >= 'A' && ch[i] <= 'Z') {     // Convertir en minúsculas
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        String st = new String(ch);
        camelcase = st;
        return camelcase;
    }

    public String caseMayuscula(String camelcase) {
        char ch[] = camelcase.toCharArray();
        for (int i = 0; i < camelcase.length(); i++) {
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {  // Si se encuentra el primer carácter de una palabra
                if (ch[i] >= 'a' && ch[i] <= 'z') {      // Si está en minúsculas
                    ch[i] = (char) (ch[i] - 'a' + 'A');  // Convertir en mayúsculas
                }
            } // Si aparte del primer carácter cualquiera está en mayúsculas
            else if (ch[i] >= 'A' && ch[i] <= 'Z') {     // Convertir en minúsculas
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        String st = new String(ch);
        camelcase = st;
        return camelcase;
    }

    public void restaurar() throws Exception {
        try {
            dao.restaurar(insumo);
            limpiar();
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
        } catch (Exception e) {
            System.out.println("error en restaurar/InsumoC " + e);
        }
    }

    public void limpiar() {
        insumo = new Insumo();
    }
    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ReportePersona reporte = new ReportePersona();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("Reportes/Insumo.jasper");
        reporte.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void listar() {
        try {
            listadoInsumo = dao.listarI(listado);
        } catch (Exception e) {
            System.out.println("Error en listarC " + e.getMessage());
        }
    }
}
