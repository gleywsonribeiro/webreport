/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.hmue.webreport.controller;

import br.org.hmue.webreport.factory.ConnectionFactory;
import br.org.hmue.webreport.jsf.util.JsfUtil;
import br.org.hmue.webreport.service.ExecutorRelatorio;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gleywson
 */
@Named(value = "nirController")
@RequestScoped
public class NirController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;

    private String codigo;

    public void censoPorSetor() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            ExecutorRelatorio executor = null;
            if (codigo.isEmpty()) {
                executor = new ExecutorRelatorio("/relatorios/nir/censo.jasper", this.response, parametros, "censo.xls");
            } else {
                parametros.put("codigo", codigo);
                executor = new ExecutorRelatorio("/relatorios/nir/censo_setor.jasper", this.response, parametros, "censo_por_setor.xls");
            }

            Connection connection = ConnectionFactory.createConnectionToOracle();

            executor.executeToExcel(connection);

            if (executor.isRelatorioGerado()) {
                facesContext.responseComplete();
            } else {
                JsfUtil.addErrorMessage("A execução do relatório não retornou dados.");
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(NirController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
