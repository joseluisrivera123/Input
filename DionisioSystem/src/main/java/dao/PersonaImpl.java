package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import model.Persona;
import model.Ubigeo;

public class PersonaImpl extends Conexion implements ICRUD<Persona> {

SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
//String fechaformateada = formato.format();
    /*Registrar Persona*/
    @Override
    public void registrar(Persona persona) throws Exception {
        try {
            String sql = "INSERT INTO PERSONA"
                    + " (NOMPER,APEPATPER,APEMATPER,EMAILPER,CELPER,DNIPER,DIRPER,ESTPER,FECNACPER,CLAVPER,TIPPER,CODUBI)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, persona.getNombre());
            System.out.println("nombre :" + persona.getNombre());
            ps.setString(2, persona.getApellidopaterno());
            ps.setString(3, persona.getApellidomaterno());
            ps.setString(4, persona.getEmail());
            ps.setString(5, persona.getCelular());
            ps.setString(6, persona.getDni());
            ps.setString(7, persona.getDireccion());
            ps.setString(8, "A");
            ps.setString(9, formato.format(persona.getFechanacimiento()));
            System.out.println("Soy la fecha : " + persona.getFechanacimiento());
            ps.setString(10, persona.getClave());
            ps.setString(11, persona.getTipo());
            ps.setString(12, persona.getUbigeo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrar/PersonaImpl : " + e);
        }
    }

    /*Modificar los datos de la persona*/
    @Override
    public void modificar(Persona persona) throws Exception {
        try {
            String sql = "UPDATE PERSONA SET NOMPER=?, APEPATPER=?,APEMATPER=?,EMAILPER=?,CELPER=?,DNIPER=?,DIRPER=?,FECNACPER=?,CLAVPER=?,TIPPER=?,CODUBI=? where IDPER=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellidopaterno());
            ps.setString(3, persona.getApellidomaterno());
            ps.setString(4, persona.getEmail());
            ps.setString(5, persona.getCelular());
            ps.setString(6, persona.getDni());
            ps.setString(7, persona.getDireccion());
            ps.setDate(8, (Date) persona.getFechanacimiento());
            ps.setString(9, persona.getClave());
            ps.setString(10, persona.getTipo());
            ps.setString(11, persona.getUbigeo());
            ps.setInt(12, persona.getIdpersona());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en modificar/PersonaImpl : " + e);
        }
    }

    /*Cambiar de estado a la persona de A = Activo a I=Inactivo*/
    @Override
    public void eliminar(Persona persona) throws Exception {
        try {
            String sql = "UPDATE PERSONA SET ESTPER='I' where IDPER=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, persona.getIdpersona());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en eliminar/PersonaImpl : " + e);
        }
    }

    /*Cambiar de estado a la persona de  I=Inactivo a A = Activo*/
    @Override
    public void restaurar(Persona persona) throws Exception {
        try {
            String sql = "UPDATE PERSONA SET ESTPER='A' where IDPER=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, persona.getIdpersona());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en restaurar/PersonaImpl : " + e);
        }
    }

    /*Listar a las Usuario*/
    public List<Persona> listarUsuario(int Listado) throws Exception {
        List<Persona> listado = null;
        Persona persona;
        String sql = "";
        switch (Listado) {
            case 1:
                sql = "SELECT * FROM vPersonasActivas WHERE TIPPER = 'V' ORDER BY IDPER";
                break;
            case 2:
                sql = "SELECT * FROM vPersonasInactivas WHERE TIPPER = 'V' ORDER BY IDPER";
                break;
            case 3:
                sql = "SELECT * FROM Persona WHERE TIPPER = 'V' ";
                break;
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                persona = new Persona();
                persona.setIdpersona(rs.getInt("IDPER"));
                persona.setNombre(rs.getString("NOMPER"));
                persona.setApellidopaterno(rs.getString("APEPATPER"));
                persona.setApellidomaterno(rs.getString("APEMATPER"));
                persona.setEmail(rs.getString("EMAILPER"));
                persona.setCelular(rs.getString("CELPER"));
                persona.setDni(rs.getString("DNIPER"));
                persona.setDireccion(rs.getString("DIRPER"));
                persona.setEstado(rs.getString("ESTPER"));
                persona.setFechanacimiento(rs.getDate("FECNACPER"));
                persona.setClave(rs.getString("CLAVPER"));
                persona.setTipo(rs.getString("TIPPER"));
                persona.setUbigeo(rs.getString("CODUBI"));
                listado.add(persona);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en PersonaImpl/Listar: " + e.getMessage());
        }
        return listado;
    }
    
    /*Listar a las Cliente*/
    public List<Persona> listarCliente(int Listado) throws Exception {
        List<Persona> listado = null;
        Persona persona;
        Boolean verificador  ;
        String sql = "";
        switch (Listado) {
            case 1:
                sql = "SELECT * FROM vPersonasActivas WHERE TIPPER = 'c' ORDER BY IDPER";
                break;
            case 2:
                sql = "SELECT * FROM vPersonasInactivas WHERE TIPPER = 'c' ORDER BY IDPER";
                break;
            case 3:
                sql = "SELECT * FROM Persona where TIPPER = 'C'";
                break;
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                persona = new Persona();
                persona.setIdpersona(rs.getInt("IDPER"));
                persona.setNombre(rs.getString("NOMPER"));
                persona.setApellidopaterno(rs.getString("APEPATPER"));
                persona.setApellidomaterno(rs.getString("APEMATPER"));
                persona.setEmail(rs.getString("EMAILPER"));
                persona.setCelular(rs.getString("CELPER"));
                persona.setDni(rs.getString("DNIPER"));
                persona.setDireccion(rs.getString("DIRPER"));
                persona.setEstado(rs.getString("ESTPER"));
                persona.setFechanacimiento(rs.getDate("FECNACPER"));
                persona.setTipo(rs.getString("TIPPER"));
                persona.setUbigeo(rs.getString("CODUBI"));
                listado.add(persona);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en PersonaImpl/Listar: " + e.getMessage());
        }
        return listado;
    }

    public void filtrarPersona(Persona persona) throws Exception {
        try {
            String sql = "SELECT * FROM PERSONA WHERE DNIPER =" + persona.getDni();
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                persona.setIdpersona(rs.getInt("IDPER"));
                persona.setNombre(rs.getString("NOMPER"));
                persona.setApellidopaterno(rs.getString("APEPATPER"));
                persona.setApellidomaterno(rs.getString("APEMATPER"));
                persona.setDni(rs.getString("DNIPER"));
                persona.setCelular(rs.getString("CELPER"));
                persona.setDireccion(rs.getString("DIRPER"));
                persona.setUbigeo(rs.getString("CODUBI"));
            }
        } catch (Exception e) {
            System.out.println("Error en filtrarPersona/PersonaImpl" + e);
        }
    }

    public List listarUbigeo() throws Exception {
        List<Ubigeo> listUbigeo = null;

        Ubigeo ubigeo;
        String sql = "select * from UBIGEO";
        try {
            listUbigeo = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ubigeo = new Ubigeo();
                ubigeo.setUbigeo(rs.getString("CODUBI"));
                ubigeo.setDepartamento(rs.getString("DEPUBI"));
                ubigeo.setProvincia(rs.getString("PROUBI"));
                ubigeo.setDistrito(rs.getString("DISUBI"));
                listUbigeo.add(ubigeo);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en dao listar Ubigeo" + e);
        }
        return listUbigeo;

    }
    
    public void registrarCliente(Persona persona) throws Exception {
        try {
            String sql = "INSERT INTO PERSONA"
                    + " (NOMPER,APEPATPER,APEMATPER,EMAILPER,CELPER,DNIPER,DIRPER,ESTPER,FECNACPER,CLAVPER,TIPPER,CODUBI)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellidopaterno());
            ps.setString(3, persona.getApellidomaterno());
            ps.setString(4, persona.getEmail());
            ps.setString(5, persona.getCelular());
            ps.setString(6, persona.getDni());
            ps.setString(7, persona.getDireccion());
            ps.setString(8, "A");
            ps.setString(9, formato.format(persona.getFechanacimiento()));
            ps.setString(10, persona.getClave());
            ps.setString(11, "c");
            ps.setString(12, persona.getUbigeo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrarCliente/PersonaImpl : " + e);
        }
    }
}
