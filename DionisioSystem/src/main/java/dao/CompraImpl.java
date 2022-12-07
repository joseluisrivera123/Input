package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.Compra;
import model.CompraDetalle;
import model.Insumo;
import model.Proveedor;

public class CompraImpl extends Conexion implements ICRUD<Compra> {

    DateFormat formato = new SimpleDateFormat("dd/MM/YYYY");

    @Override
    public void registrar(Compra compra) throws Exception {
        try {
            String sql = "INSERT INTO COMPRA(FECCOMP,TIPPAGCOMP,IDPROV,IDADM) VALUES(?,?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, formato.format(compra.getFecha()));
            ps.setString(2, compra.getTipoPago());
            ps.setInt(3, compra.getIdProveedor());
            ps.setInt(4, 21);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al registrar/CompraImpl: " + e);
        }
    }

    @Override
    public void modificar(Compra generic) throws Exception {

    }

    @Override
    public void eliminar(Compra generic) throws Exception {

    }

    public int maxCompra() {
        int nroCompra = 0;
        String sql = "SELECT MAX(IDCOMP) FROM COMPRA";
        try {
            Statement vs = this.conectar().createStatement();
            ResultSet rs = vs.executeQuery(sql);
            while (rs.next()) {
                nroCompra = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error al Registrar maxCompra: " + e.getMessage());
        }
        return nroCompra;

    }

    public void registrarCompraDetalle(CompraDetalle compradetalle) throws Exception {
        String sql = "INSERT INTO COMPRA_DETALLE(CANTCOMPDET,PRECOMPDET,IDCOMP,IDINS)\n"
                + "VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, compradetalle.getCantidad());
            ps.setDouble(2, compradetalle.getPrecio());
            ps.setInt(3, compradetalle.getIdInsumo());
            System.out.println("Soy el ID del insumo : " + compradetalle.getIdInsumo());
            ps.setInt(4, compradetalle.getIdCompra());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al registrarCompra_Detalle/CompraImpl : " + e);
        }
    }

    public void FiltrarProveedor(Proveedor proveedor) throws Exception {
        try {
            String sql = "SELECT * FROM PROVEEDOR WHERE RAZSOCPROV=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, proveedor.getRazonSocial());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                proveedor.setIdProvedor(rs.getInt("IDPROV"));
                proveedor.setRuc(rs.getString("RUCPROV"));
                proveedor.setRazonSocial(rs.getString("RAZSOCPROV"));
                proveedor.setEmailProveedor(rs.getString("CORPROV"));
                proveedor.setDireccionProveedor(rs.getString("DIRPROV"));
                proveedor.setCelularProveedor(rs.getString("CELPROV"));
                proveedor.setEstado(rs.getString("ESTPROV"));
                proveedor.setUbigeoProveedor(rs.getString("CODUBI"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al filtrarProveedor/CompraImpl: " + e);
        }
    }

    public List<String> autocompletarProveedor(String consulta) throws Exception {

        Proveedor proveedor = new Proveedor();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from PROVEEDOR WHERE RAZSOCPROV LIKE ?";
        PreparedStatement ps = dao.Conexion.conectar().prepareCall(sql);
        try {
            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                listado.add(rs.getString("RAZSOCPROV"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en autocompletarProveedor/CompraImpl " + e);
        } finally {
            ps.close();
        }
        return listado;
    }

    public void mostrarDatos(Insumo insumo) throws Exception {
        try {
            String sql = "Select * from VINSUMOACTIVO where NOMINS=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, insumo.getNombredeinsumo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                insumo.setIdinsumo(rs.getInt("IDINS"));
                insumo.setNombredeinsumo(rs.getString("NOMINS"));
                insumo.setPreciodeinsumo(rs.getDouble("PREINS"));
                insumo.setTamañodeinsumo(rs.getString("TAMINS"));
                insumo.setStockdeinsumo(rs.getInt("STOCKINS"));
                insumo.setEstadodeinsumo(rs.getString("ESINS"));
                insumo.setDetalledeinsumo(rs.getString("DETINS"));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en mostrarDatos/CompraImpl" + e);
        }
    }

    public List<String> autocompletar(String consulta) throws Exception {

        Insumo insumo = new Insumo();
        List<String> listado = new ArrayList<>();
        String sql = "select * from VINSUMOACTIVO WHERE NOMINS like ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                insumo.setNombredeinsumo(rs.getString("NOMINS"));
                listado.add(rs.getString("NOMINS"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en autocompletar/CompraImpl" + e);
        }
        return listado;

    }

    public List<Compra> ListarCompra() throws Exception {
        List<Compra> listadoCompra = null;
        Compra compra;
        String sql = "select * from vcomprasrealizadas";
        try {
            listadoCompra = new ArrayList<>();
            Statement ps = this.conectar().createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                compra = new Compra();
                compra.setIdCompra(rs.getInt("IDCOMP"));
                compra.setAdministrador(rs.getString("ADMINISTRADOR"));
                compra.setProveedor(rs.getString("Proveedor"));
                compra.setInsumo(rs.getString("NOMINS"));
                compra.setCantidad(rs.getInt("CANTCOMPDET"));
                compra.setPrecio(rs.getDouble("PREINS"));
                compra.setSubtotal(rs.getDouble("TOTAL"));
                listadoCompra.add(compra);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al ListarCompra: " + e.getMessage());
        }

        return listadoCompra;
    }

    public void ActualizarStock(CompraDetalle compradetalle) {
        int cant = compradetalle.getCantidad();
//        double precioCompra = compradetalle.getPrecio();
        int idins = compradetalle.getIdInsumo();
        String sql = "UPDATE insumo SET stockins = stockins + '" + cant + "' where IDINS = '" + idins + "'";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        }catch (Exception e) {
            System.out.println("Error en ActualizarStock/CompraImpl : " + e);
        }
    }

    @Override
    public void restaurar(Compra generic) throws Exception {
    }
}
