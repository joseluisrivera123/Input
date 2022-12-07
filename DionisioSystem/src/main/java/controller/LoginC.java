package controller;

import dao.LoginImpl;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Data;
import model.Login;

@Data
@Named(value = "loginC")
@SessionScoped
public class LoginC implements Serializable {
    boolean blocked = false;
    int intentos = 2;
    Login login;
    String user = "", pass = "", vendedor = "" , administrador = "";

    public LoginC() {
        login = new Login();
    }

    public void login() {
        LoginImpl dao;
        try {
            dao = new LoginImpl();
            login = dao.loginUsuario(user, pass);
            if (login != null) {
                if (login.getTipo().equals("a")) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/DionisioSystem/faces/vista/bienvenidaAdm.xhtml");
                    administrador = login.getNombre() + " , " + login.getApellido();
                    System.out.println("Soy la A : " +login.getTipo());
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/DionisioSystem/faces/vista/bienvenida.xhtml");
                    vendedor = login.getNombre() + " , " + login.getApellido();
                    System.out.println("Soy el V : " + login.getTipo());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error , Credenciales no válida ", "Usuario y/o contraseña incorrecta"));
                if(intentos == 0){
                    System.out.println("Bloqueate");
                    blocked = true;
                    java.util.concurrent.TimeUnit.SECONDS.sleep(5);
                    blocked = false ;
                    intentos = 2;
                    return;
                }
                System.out.println(intentos);
                intentos--;
            }
        } catch (Exception e) {
            System.out.println("Error en login/LoginC : " + e);
        }
    }

    public void limpiar() {
        login = new Login();
    }
}
