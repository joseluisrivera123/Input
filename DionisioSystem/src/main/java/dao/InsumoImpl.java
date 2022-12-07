package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Insumo;

public class InsumoImpl extends Conexion implements ICRUD<Insumo> {

    @Override
    public void registrar(Insumo insumo) throws Exception {
        if (insumo.getNombredeinsumo().equals("") || insumo.getDetalledeinsumo().equals("")) {
            System.out.println("Error");
        } else {
            try {
                String sql = "INSERT INTO INSUMO"
                        + " (NOMINS , PREINS , TAMINS , STOCKINS , ESTINS  , DETINS)"
                        + " values (?,?,?,?,?,?) ";
                PreparedStatement ps = this.conectar().prepareStatement(sql);
                ps.setString(1, insumo.getNombredeinsumo());
                ps.setDouble(2, insumo.getPreciodeinsumo());
                ps.setString(3, insumo.getTamañodeinsumo());
                ps.setInt(4, insumo.getStockdeinsumo());
                ps.setString(5, "A");
                ps.setString(6, insumo.getDetalledeinsumo());
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                System.out.println("Error Registrar/InsumoImpl : " + e);
            }
        }
    }

    @Override
    public void modificar(Insumo insumo) throws Exception {
        String sql = "UPDATE INSUMO SET NOMINS=?, PREINS=?,TAMINS=?,STOCKINS=?,ESTINS=?,DETINS=? where IDINS=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, insumo.getNombredeinsumo());
            ps.setDouble(2, insumo.getPreciodeinsumo());
            ps.setString(3, insumo.getTamañodeinsumo());
            ps.setInt(4, insumo.getStockdeinsumo());
            ps.setString(5, insumo.getEstadodeinsumo());
            ps.setString(6, insumo.getDetalledeinsumo());
            ps.setInt(7, insumo.getIdinsumo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en Modificar/InsumoImpl : " + e);
        }
    }

    @Override
    public void eliminar(Insumo insumo) throws Exception {
        try {
            String sql = "update INSUMO set ESTINS='I' where IDINS=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, insumo.getIdinsumo());
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en Eliminar/InsumoImpl : " + e);
        }
    }

    @Override
    public void restaurar(Insumo insumo) throws Exception {
        try {
            String sql = "UPDATE INSUMO SET ESTINS = 'A' WHERE IDINS = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, insumo.getIdinsumo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("error en restaurar/InsumoImpl " + e.getMessage());
        }
    }
    
    public List listar() throws Exception {

        List<Insumo> listado = null;
        Insumo insumo;
        String sql = "SELECT * FROM INSUMO WHERE ESTINS = 'A' order by IDINS DESC";
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                insumo = new Insumo();
                insumo.setIdinsumo(rs.getInt("IDINS"));
                insumo.setNombredeinsumo(rs.getString("NOMINS"));
                insumo.setPreciodeinsumo(rs.getDouble("PREINS"));
                insumo.setTamañodeinsumo(rs.getString("TIPINS"));
                insumo.setStockdeinsumo(rs.getInt("TAMINS"));
                insumo.setEstadodeinsumo(rs.getString("STOCKINS"));
                insumo.setDetalledeinsumo(rs.getString("ESTINS"));
                listado.add(insumo);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en InsumoImpl/Listar: " + e.getMessage());
        }
        return listado;
    }
    
    public List<Insumo> listarI(int Listado) throws Exception {
        List<Insumo> listado = null;
        Insumo ins;
        String sql = "";
        switch (Listado) {
            case 1:
                sql = "select * from vInsumoActivo ORDER BY IDINS DESC";
                break;
            case 2:
                sql = "select * from vInsumoInactivo ORDER BY IDINS DESC";
                break;
            case 3:
                sql = "SELECT * FROM INSUMO ; ";
                break;
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ins = new Insumo();
                ins.setIdinsumo(rs.getInt("IDINS"));
                ins.setNombredeinsumo(rs.getString("NOMINS"));
                ins.setPreciodeinsumo(rs.getDouble("PREINS"));
                ins.setTamañodeinsumo(rs.getString("TAMINS"));
                ins.setStockdeinsumo(rs.getInt("STOCKINS"));
                ins.setEstadodeinsumo(rs.getString("ESTINS"));
                ins.setDetalledeinsumo(rs.getString("DETINS"));
                listado.add(ins);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en InsumoImpl/ListarI: " + e);
        }
        return listado;
    }
}
