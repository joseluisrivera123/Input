
package services;

import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "validarCelular")
public class ValidadorCelular implements Validator {
    
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String validadorCelular = o.toString().trim();
        if(validadorCelular.length()!=0 && validadorCelular.length()<9){
            String plantilla = "^\\9\\?\\?\\?\\?\\?\\?\\?\\?$";
            boolean bool = Pattern.matches(plantilla, validadorCelular);
            
            if(!bool){
                throw new ValidatorException(new FacesMessage("Ejemplo 9######## "));
            }
        }
    }
    
}
