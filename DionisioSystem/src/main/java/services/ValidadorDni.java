
package services;

import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "validarDni")
public class ValidadorDni implements Validator {
    
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String validadorDNI = o.toString().trim();
        if(validadorDNI.length()!=0 && validadorDNI.length()<8){
            String plantilla = "^\\?\\?\\?\\?\\?\\?\\?\\?$";
            boolean bool = Pattern.matches(plantilla, validadorDNI);
            
            if(!bool){
                throw new ValidatorException(new FacesMessage("Escriba 8 Digítos"));
            }
        }
    }
    
}
