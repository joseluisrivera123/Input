package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import model.Persona;
import model.Producto;
import model.Venta;
import model.VentaDetalle;

public class VentaImpl extends Conexion implements ICRUD<Venta> {

    @Override
    public void registrar(Venta venta) throws Exception {
        try {
            String sql = "insert into VENTA"
                    + "(FECVEN,IDVEND,IDCLI)"
                    + "VALUES (?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, formato.format(venta.getFechaVenta()));
            ps.setInt(2, 3);
            ps.setInt(3, venta.getIdCliente());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrar/VentaImpl" + e.getMessage());
        }
    }

    @Override
    public void modificar(Venta generic) throws Exception {

    }

    @Override
    public void eliminar(Venta generic) throws Exception {

    }

    @Override
    public void restaurar(Venta generic) throws Exception {

    }

    public List<String> autocompletar(String consulta) throws Exception {
        Producto producto = new Producto();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from PRODUCTO WHERE NOMPRO LIKE ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                producto.setNombre(rs.getString("NOMPRO"));
                listado.add(rs.getString("NOMPRO"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en autocompletar/VentaImpl : " + e);
        }
        return listado;

    }

    public void mostrarDatos(Producto pro) throws Exception {
        try {
            String sql = "Select * from PRODUCTO where NOMPRO =?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pro.setIdproducto(rs.getInt("IDPRO"));
                pro.setNombre(rs.getString("NOMPRO"));
                pro.setPrecio(rs.getDouble("PREPRO"));
                pro.setTipo(rs.getString("TIPPRO"));
                pro.setTamaño(rs.getString("TAMPRO"));
                pro.setStock(rs.getInt("STOCKPRO"));
                pro.setEstado(rs.getString("ESTPRO"));
                pro.setDetalle(rs.getString("DETPRO"));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en mostrar datos/VentaImpl" + e);
        }
    }

    public void Vendedor(Persona persona, int idper) {
        String sql = "SELECT * FROM PERSONA WHERE TIPPER = 'v' and IDPER = ";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setInt(1, idper);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                persona.setIdpersona(rs.getInt("IDPER"));
                persona.setNombre(rs.getString("NOMPER"));
                persona.setApellidopaterno(rs.getString("APEPATPER"));
                persona.setApellidomaterno(rs.getString("APEMATPER"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("error en vendedor/VentaImpl : " + e);
        }
    }

    public void filtrarCliente(Persona persona) throws Exception {
        try {
            String sql = "select \n"
                    + "* \n"
                    + "from Persona where TIPPER = 'c' and DNIPER = '?';" + persona.getDni();
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                persona.setIdpersona(rs.getInt("IDPER"));
                persona.setNombre(rs.getString("NOMPER"));
                persona.setApellidopaterno(rs.getString("APEPATPER"));
                persona.setApellidomaterno(rs.getString("APEMATPER"));
                persona.setDni(rs.getString("DNIPER"));
                persona.setEmail(rs.getString("EMAILPER"));
                persona.setCelular(rs.getString("CELPER"));
                persona.setDireccion(rs.getString("DIRPER"));
                persona.setFechanacimiento(rs.getDate("FECNACPER"));
            }
        } catch (Exception e) {
            System.out.println("error en filtrarCliente/VentaImpl : " + e);
        }

    }

    public int ventasMaximas() throws Exception {
        /*X almacenara el resultado de la consulta al SQL Server*/
        int x = 0;
        String sql = "SELECT MAX(IDVEN) FROM VENTA";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                x = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error en ventasMaximas/vENTAiMPL" + e);
        }
        return x;
    }

    public void registrarVentaDetalle(VentaDetalle ventadetalle) throws Exception {
        String sql = "INSERT INTO VENTA_DETALLE"
                + "(CANVENDET,IDVEN,IDPRO)"
                + "VALUES (?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, ventadetalle.getCantidad());
            ps.setInt(2, ventadetalle.getIdVenta());
            ps.setInt(3, ventadetalle.getIdProducto());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrarVentaDetalle/VentaDetalleImpl " + e);
        }
    }

    public void ActualizarStock(VentaDetalle vd) {
        int cantidad = vd.getCantidad();
        int idpro = vd.getIdProducto();
        String sql = "UPDATE producto SET STOCKPRO = STOCKPRO - '" + cantidad + "' where IDPRO = '" + idpro + "'";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en ActualizarStock/VentaImpl : " + e);
        }
    }

    public List<Venta> ListarVentas() throws Exception {
        List<Venta> listadoVenta = null;
        Venta venta;
        String sql = "select * from vVentasRealizadas";
        try {

            listadoVenta = new ArrayList<>();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                venta = new Venta();
                venta.setIdVenta(rs.getInt("IDVEN"));
                venta.setCliente(rs.getString("CLIENTE"));
                venta.setVendedor(rs.getString("VENDEDOR"));
                venta.setProducto(rs.getString("NOMPRO"));
                venta.setCantidad(rs.getInt("CANVENDET"));
                venta.setPrecio(rs.getDouble("PREPRO"));
                venta.setSubtotal(rs.getDouble("TOTAL"));
                listadoVenta.add(venta);
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println("Error en ListarVentas/ventaImpl" + e);
        }
        return listadoVenta;

    }

    public boolean filtrarClientes(Persona persona) throws Exception {
        boolean verificador = false;
        try {
            String sql = "select * from persona where TIPPER = 'c' and DNIPER = " + persona.getDni();
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                verificador = true;
                persona.setIdpersona(rs.getInt("IDPER"));
                persona.setNombre(rs.getString("NOMPER"));
                persona.setApellidopaterno(rs.getString("APEPATPER"));
                persona.setApellidomaterno(rs.getString("APEMATPER"));
                persona.setDni(rs.getString("DNIPER"));
                persona.setEmail(rs.getString("EMAILPER"));
                persona.setCelular(rs.getString("CELPER"));
                persona.setDireccion(rs.getString("DIRPER"));
                persona.setEstado(rs.getString("ESTPER"));
                persona.setTipo(rs.getString("TIPPER"));
                persona.setFechanacimiento(rs.getDate("FECNACPER"));
            }
        } catch (Exception e) {
            System.out.println("error en filtrarCliente/VentaImpl : " + e);
        }
        return verificador;

    }

    DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

    public DateFormat getFormato() {
        return formato;
    }

    public void setFormato(DateFormat formato) {
        this.formato = formato;
    }
}
