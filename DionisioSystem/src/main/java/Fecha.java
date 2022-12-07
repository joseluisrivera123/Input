
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Fecha {

    public void mostrarFecha() {
        
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("28/06/2022");
        System.out.println(dateFormat.format(date));

    }

    public static void main(String args[]) {

        Fecha fecha = new Fecha();
        fecha.mostrarFecha();

    }

}
