
package services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.Venta;
import org.primefaces.shaded.json.JSONObject;

public class api2 {

    public static JSONObject ApiDolar(Venta venta) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://api.apis.net.pe/v1/tipo-cambio-sunat")
                .header("Authorization", "Bearer f918cf38e37746c18cb7b176da9d508a643b2d8b99e20b64ed419f085ee71c17")
                .asString();
        
        JSONObject data = new JSONObject(response.getBody());
        venta.setCambio(data.getDouble("compra"));
        return data;
        
    }
    
    public static void main(String[] args) throws UnirestException {
        Venta venta;
//        JSONObject respuesta = ApiDolar(prueba);
//        System.out.println(respuesta.getDouble("compra"));
        
    }
}

