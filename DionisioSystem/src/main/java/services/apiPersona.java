 
package services;


import model.Persona;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;


public class apiPersona {
    public static int consumiendoAPI(Persona persona) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://apiperu.dev/api/dni/" + persona.getDni())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 3f6398fff7fc4e4ac6c01003c93b5111464018f574e13a0cd597a195f2252c50")
                .asString();

        //lineas añadidas al OKhttp
        if (response.getStatus() == 200) {
            JSONObject cadenaJson = new JSONObject(response.getBody());
            JSONObject cadena = cadenaJson.getJSONObject("data");
            persona.setNombre(cadena.getString("nombres"));
            persona.setApellidopaterno(cadena.getString("apellido_paterno")); 
            persona.setApellidomaterno(cadena.getString("apellido_materno"));
        }
        System.out.println("nombre :" + persona.getNombre());
        System.out.println("apelldiopaterno :" + persona.getApellidopaterno());
        System.out.println("apellidomaterno :" + persona.getApellidomaterno());
        return response.getStatus();
    }
}
