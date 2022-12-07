package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoImpl extends Conexion implements ICRUD<Producto> {

    @Override
    public void registrar(Producto producto) throws Exception {
        if (producto.getNombre().equals("") || producto.getDetalle().equals("")) {
            System.out.println("Error");
        } else {
            try {
                String sql = "INSERT INTO PRODUCTO"
                        + " (NOMPRO , PREPRO , TIPPRO, TAMPRO , STOCKPRO , ESTPRO  , DETPRO)"
                        + " values (?,?,?,?,?,?,?) ";
                PreparedStatement ps = this.conectar().prepareStatement(sql);
                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecio());
                ps.setString(3, producto.getTipo());
                ps.setString(4, producto.getTamaño());
                ps.setInt(5, producto.getStock());
                ps.setString(6, "A");
                ps.setString(7, producto.getDetalle());
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                System.out.println("Error Registrar/ProductoImpl : " + e);
            }
        }
    }

    @Override
    public void modificar(Producto producto) throws Exception {
        String sql = "UPDATE producto SET NOMPRO=?, PREPRO=?,TIPPRO=?,TAMPRO=?,STOCKPRO=?,DETPRO=? where IDPRO=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getTipo());
            ps.setString(4, producto.getTamaño());
            ps.setInt(5, producto.getStock());
            ps.setString(6, producto.getDetalle());
            ps.setInt(7, producto.getIdproducto());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en Modificar/ProductoImpl : " + e);
        }
    }

    @Override
    public void eliminar(Producto producto) throws Exception {
        try {
            String sql = "update PRODUCTO set ESTPRO='I' where IDPRO=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, producto.getIdproducto());
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en Eliminar/ProductoImpl : " + e);
        }
    }

    @Override
    public void restaurar(Producto producto) throws Exception {
        try {
            String sql = "UPDATE PRODUCTO SET ESTPRO = 'A' WHERE IDPRO = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, producto.getIdproducto());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("error en restaurar/ProductoImpl " + e);
        }
    }
    
    public List listar() throws Exception {

        List<Producto> listado = null;
        Producto producto;
        String sql = "SELECT * FROM PRODUCTO WHERE ESTPRO = 'A' order by IDPRO DESC";
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                producto = new Producto();
                producto.setIdproducto(rs.getInt("IDPRO"));
                producto.setNombre(rs.getString("NOMPRO"));
                producto.setPrecio(rs.getDouble("PREPRO"));
                producto.setTipo(rs.getString("TIPPRO"));
                producto.setTamaño(rs.getString("TAMPRO"));
                producto.setStock(rs.getInt("STOCKPRO"));
                producto.setEstado(rs.getString("ESTPRO"));
                producto.setDetalle(rs.getString("DETPRO"));
                listado.add(producto);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ProductoImpl/Listar: " + e.getMessage());
        }
        return listado;
    }
    
    public List<Producto> listarP(int Listado) throws Exception {
        List<Producto> listado = null;
        Producto pro;
        String sql = "";
        switch (Listado) {
            case 1:
                sql = "select * from vProductoActivo ORDER BY IDPRO DESC";
                break;
            case 2:
                sql = "select * from vProductoInactivo ORDER BY IDPRO DESC";
                break;
            case 3:
                sql = "SELECT * FROM PRODUCTO ; ";
                break;
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                pro = new Producto();
                pro.setIdproducto(rs.getInt("IDPRO"));
                pro.setNombre(rs.getString("NOMPRO"));
                pro.setPrecio(rs.getDouble("PREPRO"));
                pro.setTipo(rs.getString("TIPPRO"));
                pro.setTamaño(rs.getString("TAMPRO"));
                pro.setStock(rs.getInt("STOCKPRO"));
                pro.setEstado(rs.getString("ESTPRO"));
                pro.setDetalle(rs.getString("DETPRO"));
                listado.add(pro);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ProductoImpl/ListarP: " + e);
        }
        return listado;
    }

}
