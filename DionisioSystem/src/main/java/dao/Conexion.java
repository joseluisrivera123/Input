/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


//
public class Conexion {
//
    public static Connection cnx = null;

    public static Connection conectar() throws Exception {

        try {
            
            String user = "PRUEBA";
            String pwd = "joserivera123";
            String driver = "oracle.jdbc.OracleDriver";
            String url = "jdbc:oracle:thin:@localhost:1521/XE";
            Class.forName(driver).newInstance();
            cnx = DriverManager.getConnection(url, user, pwd);
//            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            String url = "jdbc:sqlserver://localhost:1433;databaseName=Dionisio";
//            String user = "sa";
//            String pwd = "joserivera123";
//            Class.forName(driver);
//            cnx = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            System.out.println("Error de Conexión" + "  " + e.getMessage() + "  " + e.getStackTrace());
        }
        return cnx;
    }
    
    
    public static Connection getCnx() {
        return cnx;
    }

    public static void setCnx(Connection aCnx) {
        cnx = aCnx;
    }


    public void cerrar() throws Exception {
        if (cnx != null) {
            cnx.close();
        }
    }

    public static void main(String[] args) throws Exception {
        conectar();
        try {
            if (cnx != null) {
                System.out.println("CONEXIÓN EXITOSA");
                JOptionPane.showMessageDialog(null, "CONEXIÓN EXITOSA", "CORRECTO", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("SIN CONEXIÓN REVISA");
                JOptionPane.showMessageDialog(null, "SIN CONEXIÓN REVISA", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Error en " + e.getMessage());
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Connection getCn() {
        return cnx;
    }

    public void setCn(Connection cnx) {
        this.cnx = cnx;
    }


}
