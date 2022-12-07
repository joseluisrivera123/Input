package services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiMensaje {

    public static JSONObject apiMensajeTrabajo1(String nro) throws UnirestException {
        System.out.println(nro);
        String autenticacion = "cm9iZXJ0LnNhbmNoZXpAdmFsbGVncmFuZGUuZWR1LnBlOkFFMDY0NkQ0LTlCOTctNDVDQi01RDA4LTlGQjcwRjhBODJFNA==";
        String RapiKey = "de2d27930fmshaf200f274efcda3p1289d6jsn430402e6c47e";
        HttpResponse<String> response = Unirest.post("https://clicksend.p.rapidapi.com/sms/send")
                .header("content-type", "application/json")
                .header("Authorization", "Basic "+ autenticacion)
                .header("Content-Type", "application/json")
                .header("X-RapidAPI-Key", RapiKey)
                .header("X-RapidAPI-Host", "clicksend.p.rapidapi.com")
                .body("{\n  \"messages\": [\n    {\n    "
                        + "  \"source\": \"mashape\",\n    "
                        + "  \"from\": \"Test\",\n    "
                        + "  \"body\": \"esta registrado a dionisio!"
                        + "¡Disfrute la tienda!\",\n  "
                        + "    \"to\": \"+51" + nro + "\",\n     "
                        + " \"schedule\": \"1452244637\",\n    "
                        + "  \"custom_string\": \"this is a test\"\n   "
                        + " }\n  ]\n}")
                .asString();

        JSONObject cadenaJson = new JSONObject(response.getBody());
        JSONObject cadena = cadenaJson.getJSONObject("data");
        JSONArray mensajes = cadena.getJSONArray("messages");
        JSONObject cuerpo = mensajes.getJSONObject(0);

        return cuerpo;

    }

    public static void main(String[] args) throws Exception {
        
        JSONObject json = ApiMensaje.apiMensajeTrabajo1("+51992884795");
        System.out.println("mensaje que se envio es " + json.getString("body"));

    }
}