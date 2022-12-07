/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import dao.Conexion;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperRunManager;


public class ReporteVenta extends Conexion{

    public void getReportePdf(String root, String numeroacta) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            File reportfile = new File(root);
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("IDVEN", new String(numeroacta));
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.conectar());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}