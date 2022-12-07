package controller;

import dao.PersonaImpl;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import lombok.Data;
import model.Persona;
import model.Ubigeo;
import report.ReportePersona;
import services.ApiMensaje;
import services.apiPersona;

@Data
@SessionScoped
@Named(value = "personaC")
public class PersonaC implements Serializable {

    ApiMensaje mensajeApi = new ApiMensaje();

    DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
    /*Variable*/
    int Listado = 1;
    /*Variable para consumir API*/
    boolean dniencontrado = true;
    boolean nombre = true;
    boolean apellido = true;
    boolean celular = true;
    boolean direccion = true;
    boolean distrito = true;

    /*Modelo*/
    Persona persona;
    Ubigeo ubigeo;

    /*Dao*/
    PersonaImpl dao;

    /*Listar*/
    List<Persona> listadoPersona;
    List<Ubigeo> listadoUbigeo;

    public PersonaC() {
        /*Instanciando los modelos*/
        persona = new Persona();
        dao = new PersonaImpl();
        ubigeo = new Ubigeo();
    }

    /*Método para registrar a las Personas*/
    public void registrar() throws Exception {
        /*Agregando variable nro*/
        String nro = persona.getCelular();
        try {
            if (persona.getDni().length() == 8 && persona.getCelular().length() == 9) {
                if (existe(persona, listadoPersona)) {
                    persona.setNombre(caseMayuscula(persona.getNombre()));
                    persona.setApellidopaterno(caseMayuscula(persona.getApellidopaterno()));
                    persona.setApellidomaterno(caseMayuscula(persona.getApellidomaterno()));
                    persona.setEmail(caseMinuscula(persona.getEmail()));
                    /*Agregando mensajeApi*/
                    mensajeApi.apiMensajeTrabajo1(nro);
                   
                    dao.registrar(persona);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
                    limpiar();
                    listarUsuario();
                } else {
                    persona.setDni("");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ADVENTENCIA", "DNI Duplicado , La persona ya se encuentra registrado"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese datos Coherentes"));
            }
        } catch (Exception e) {
            System.out.println("Error en registrar/PersonaC : " + e);
        }
    }

    /*Método para modificar los datos de la persona*/
    public void modificar() throws Exception {
        try {
            if (persona.getDni().length() == 8 && persona.getCelular().length() == 9) {
                if (updateExiste(persona, listadoPersona)) {
                    persona.setNombre(caseMayuscula(persona.getNombre()));
                    persona.setApellidopaterno(caseMayuscula(persona.getApellidopaterno()));
                    persona.setApellidomaterno(caseMayuscula(persona.getApellidomaterno()));
                    persona.setEmail(caseMinuscula(persona.getEmail()));
                    persona.setDireccion(caseMinuscula(persona.getDireccion()));
                    dao.modificar(persona);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
                    limpiar();
                    listarUsuario();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Numero de DNI repetido"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese datos coherentes"));
            }
        } catch (Exception e) {
            System.out.println("error en modificar/ClienteC : " + e);
        }
    }

    /*Método para eliminar a la persona*/
    public void eliminar() throws Exception {
        try {
            dao.eliminar(persona);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Persona Eliminado con éxito"));
            limpiar();
            listarUsuario();
        } catch (Exception e) {
            System.out.println("Error en Eliminar/PersonaC" + e);
        }
    }

    /*Método para restaurar a la persona*/
    public void restaurar() throws Exception {
        try {
            dao.restaurar(persona);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
            limpiar();
            listarUsuario();
        } catch (Exception e) {
            System.out.println("error en restaurar/ClienteC " + e);
        }
    }

    /*Método para limpiar el modelo*/
    public void limpiar() {
        persona = new Persona();

    }

    /*Método para listar a las Usuario*/
    public void listarUsuario() {
        try {
            listadoPersona = dao.listarUsuario(Listado);
        } catch (Exception e) {
            System.out.println("Error en listarC " + e);
        }
    }

    /*Método para listar a las Personas*/
    public void listarCliente() {
        try {
            listadoPersona = dao.listarCliente(Listado);
        } catch (Exception e) {
            System.out.println("Error en listarC " + e);
        }
    }

    /**/

 /*Método para filtrar a las Personas*/
    public void Filtrado() throws Exception {
        try {

            dao.filtrarPersona(persona);
        } catch (Exception e) {
            System.out.println("Error en Filtrado/PersonaC : " + e);
        }
    }

    /*Método para poner la primera letra en mayuscula*/
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

    /*Método para poner todas las letras en minusculas*/
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

    /*Validación si el cliente existe al registrar*/
    public boolean existe(Persona persona, List<Persona> listarPersona) {
        for (Persona cli : listarPersona) {
            if (persona.getDni().equals(cli.getDni())) {
                return false;
            }
        }
        return true;
    }

    /*Validación si el cliente existe al Modificar*/
    public boolean updateExiste(Persona cli, List<Persona> listaPersona) {
        for (Persona cliente : listaPersona) {
            if (cli.getDni().equals(cliente.getDni())) {
                return cli.getIdpersona() == cli.getIdpersona();
            }
        }
        return true;
    }

    /*Lista de Ubigeo*/
    public List<Ubigeo> listarUbigeo() {
        try {
            listadoUbigeo = dao.listarUbigeo();
        } catch (Exception e) {
            System.out.println("Error en listar Ubigeo" + e.getMessage());
        }
        return listadoUbigeo;
    }

    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ReportePersona reporte = new ReportePersona();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("Reportes/Cliente.jasper");
        reporte.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /*Método para registrar a las Cliente*/
    public void registrarCliente() throws Exception {
        try {
            if (persona.getDni().length() == 8 && persona.getCelular().length() == 9) {
                if (existe(persona, listadoPersona)) {
                    persona.setNombre(caseMayuscula(persona.getNombre()));
                    persona.setApellidopaterno(caseMayuscula(persona.getApellidopaterno()));
                    persona.setApellidomaterno(caseMayuscula(persona.getApellidomaterno()));
                    persona.setEmail(caseMinuscula(persona.getEmail()));
                    dao.registrarCliente(persona);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
                    limpiar();
                    listarUsuario();
                } else {
                    persona.setDni("");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ADVENTENCIA", "DNI Duplicado , La persona ya se encuentra registrado"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese datos Coherentes"));
            }
        } catch (Exception e) {
            System.out.println("Error en registrar/PersonaC : " + e);
        }
    }

    public void consumientdoApi() {
        try {
            int respuesta = apiPersona.consumiendoAPI(persona);
            System.out.print(respuesta);
            if (respuesta == 200) {
                dniencontrado = true;
                nombre = true;
                apellido = true;
                celular = false;
                direccion = false;
                distrito = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bien", "se busco correctamente"));
            } else {
                dniencontrado = false;
                nombre = false;
                apellido = false;
                celular = false;
                direccion = false;
                distrito = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Success", "No se encontro datos"));
            }
        } catch (Exception e) {
            dniencontrado = false;
            nombre = false;
            apellido = false;
            celular = false;
            direccion = false;
            
            distrito = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de RUC ingresado es inválido"));
        }

    }

}
