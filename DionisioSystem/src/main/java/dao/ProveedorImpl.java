package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Proveedor;
import model.Ubigeo;

public class ProveedorImpl extends Conexion implements ICRUD<Proveedor> {

    @Override
    public void registrar(Proveedor proveedor) throws Exception {
        try {
            String sql = "INSERT INTO PROVEEDOR"
                    + "(RUCPROV,RAZSOCPROV,CORPROV,DIRPROV,CELPROV,,CODUBI)"
                    + "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, proveedor.getRuc());
            ps.setString(2, proveedor.getRazonSocial());
            ps.setString(3, proveedor.getEmailProveedor());
            ps.setString(4, proveedor.getDireccionProveedor());
            ps.setString(5, proveedor.getCelularProveedor());
            ps.setString(6, proveedor.getUbigeoProveedor());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrar/ProveedorImpl : " + e);
        }
    }

    @Override
    public void modificar(Proveedor proveedor) throws Exception {
        try {
            String sql = "UPDATE PROVEEDOR SET RUCPROV=?,RAZSOCPROV=?, CORPROV=? ,DIRPROV=?,CELPROV=?,CODUBI=? WHERE IDPROV = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, proveedor.getRuc());
            ps.setString(2, proveedor.getRazonSocial());
            ps.setString(3, proveedor.getEmailProveedor());
            ps.setString(4, proveedor.getDireccionProveedor());
            ps.setString(5, proveedor.getCelularProveedor());
            ps.setString(6, proveedor.getUbigeoProveedor());
            ps.setInt(7, proveedor.getIdProvedor());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en modificar/ProveedorImpl : " + e);
        }
    }

    @Override
    public void eliminar(Proveedor proveedor) throws Exception {
        try {
            String sql = "update PROVEEDOR set ESTPROV= 'I' WHERE IDPROV = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, proveedor.getIdProvedor());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en eliminar/Proveedor : " + e);
        }
    }

    @Override
    public void restaurar(Proveedor proveedor) throws Exception {
        String sql = "update proveedor set ESTPROV = A where IDPROV=?";
        PreparedStatement ps = this.conectar().prepareStatement(sql);
        try {
            ps.setInt(1, proveedor.getIdProvedor());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en restaurar/ProveedorImpl : " + e);
        }
    }

    public List<Proveedor> listar(int caso) throws Exception {
        List<Proveedor> lista = null;
        ResultSet rs;
        String sql = "";
        switch (caso) {
            case 1:
                sql = "SELECT * FROM PROVEEDOR WHERE ESTPROV = 'A'";
                break;
            case 2:
                sql = "SELECT * FROM PROVEEDOR WHERE ESTPROV = 'I'";
                break;
            case 3:
                sql = "SELECT * FROM PROVEEDOR";
                break;
            default:
                System.out.println("error en lisar/ProveedorImpl");
        }
        lista = new ArrayList<>();
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor pro = new Proveedor();
                pro.setIdProvedor(rs.getInt("IDPROV"));
                pro.setRuc(rs.getString("RUCPROV"));
                pro.setRazonSocial(rs.getString("RAZSOCPROV"));
                pro.setEmailProveedor(rs.getString("CORPROV"));
                pro.setDireccionProveedor(rs.getString("DIRPROV"));
                pro.setCelularProveedor(rs.getString("CELPROV"));
                pro.setUbigeoProveedor(rs.getString("CODUBI"));
                lista.add(pro);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error listar/ProveedorImpl: " + e);
        }
        return lista;
    }
    
    public List<String> autocompleteUbigeo(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        String sql = "select top 10 concat(DEPUBI,',', PROUBI,',',DISUBI) AS UBICACION "
                + "from UBIGEO WHERE DISUBI LIKE ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("UBICACION"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteUbigeo/ProveedorImpl: " + e);
        }
        return lista;
    }
    
    public String obtenercodigoUbi(String cadenaubi) throws SQLException, Exception {
        String sql = "select CODUBI FROM UBIGEO WHERE concat(DEPUBI, ', ', PROUBI, ', ',DISUBI) = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, cadenaubi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("CODUBI");
            }
            return rs.getString("CODUBI");
        } catch (Exception e) {
            System.out.println("Error en obtenercodigoUbi/ProveedorImpl : " + e);
            throw e;
        }

    }
    
    
    public List listaUbigeo() {
        List<Ubigeo> listarUbi = null;
        String sql = "select * from UBIGEO";
        try {
            listarUbi = new ArrayList<>();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Ubigeo ubi = new Ubigeo();
                ubi.setUbigeo(rs.getString("CODUBI"));
                ubi.setDepartamento(rs.getString("DEPUBI"));
                ubi.setProvincia(rs.getString("PROUBI"));
                ubi.setDistrito(rs.getString("DISUBI"));
                listarUbi.add(ubi);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error al listarUbigeo/ProovedorImpl : " + e);
        }
        return listarUbi;
    }
    
    
    
    
    

}
