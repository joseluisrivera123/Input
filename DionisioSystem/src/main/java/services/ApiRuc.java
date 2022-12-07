/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.io.IOException;
import model.Proveedor;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Usuario
 */
public class ApiRuc {

    public static Boolean disabled = true;
    
    public static void BuscadorRucProveedor(Proveedor proveedor) throws Exception {
        String ruc = proveedor.getRuc();
        String token = "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imp1bGlvLnF1aXNwZUB2YWxsZWdyYW5kZS5lZHUucGUifQ.6M-P2QMMvKFZEeMvTUXvkOooM02N_pWqt0OdlaYW3PM";
        String enlace = "https://dniruc.apisperu.com/api/v1/ruc/" + ruc + token;
        try {
            URL url = new URL(enlace);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            if (root.isJsonObject()) {
                JsonObject rootobj = root.getAsJsonObject();

                String razon = rootobj.get("razonSocial").getAsString();
                String direccion = rootobj.get("direccion").getAsString();
                String ubigeo = rootobj.get("ubigeo").getAsString();

                System.out.println(razon + "\n" + direccion + "\n" + ubigeo + "\n");

                proveedor.setRazonSocial(razon);
                proveedor.setDireccionProveedor(direccion);
                proveedor.setUbigeoProveedor(ubigeo);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "RUC no encontrado"));
        }
    }

    public static void main(String[] args) throws Exception {
        Proveedor pro = new Proveedor();
        pro.setRuc("20491265737");
        BuscadorRucProveedor(pro);
    }

}
