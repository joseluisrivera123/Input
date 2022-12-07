
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Login;


public class LoginImpl extends Conexion{
    public Login loginUsuario (String user , String pass) throws Exception{
        Login login = null;
        String sql = "exec spLogin ? , ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                login = new Login();
                login.setId(rs.getInt("IDPER"));
                login.setNombre(rs.getString("NOMPER"));
                login.setApellido(rs.getString("APEPER"));
                login.setTipo(rs.getString("TIPPER"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(" en LoginUsuario : " + e);
        }
        return login;
    }
}
