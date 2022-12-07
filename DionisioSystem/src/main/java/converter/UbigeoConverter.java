
package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter("UbigeoConverter")
public class UbigeoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String tipo = "";
        if(value!=null){
            tipo = (String)value;
            switch(tipo){
                case "140402" : tipo = "CALANGO";break;
                case "140403" : tipo = "CERRO AZUL";break;
                case "140404" : tipo = "CHILCA";break;
                case "140405" : tipo = "COAYLLO";break;
                case "140406" : tipo = "IMPERIAL";break;
                case "140407" : tipo = "LUNAHUANA";break;
                case "140408" : tipo = "MALA";break;
                case "140409" : tipo = "NUEVO IMPERIAL";break;
                case "140410" : tipo = "SAN VICETE";break;
            }
        }
        return tipo;
    }
    
}
