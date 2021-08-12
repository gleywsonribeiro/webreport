/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.hmue.webreport.controller;

import br.org.hmue.webreport.factory.ConnectionFactory;
import br.org.hmue.webreport.jsf.util.JsfUtil;
import br.org.hmue.webreport.service.ExecutorRelatorio;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gleywson
 */
@Named(value = "assistenciaController")
@RequestScoped
public class AssistenciaController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;

    private String mesAno;


    public void grauComplexidadeToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("mes_ano", mesAno);
        getReport("/relatorios/assistencia/grau_complexidade.jasper", parametros, "grau de complexidade.xls");
    }


    
    private void getReport(String caminho, Map parametros, String nomeRelatorioGerado) {
        try {

            ExecutorRelatorio executor = new ExecutorRelatorio(caminho,
                    this.response, parametros, nomeRelatorioGerado);

            Connection connection = ConnectionFactory.createConnectionToOracle();

            executor.executeToExcel(connection);

            if (executor.isRelatorioGerado()) {
                facesContext.responseComplete();
            } else {
                JsfUtil.addErrorMessage("A execução do relatório não retornou dados.");
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(AssistenciaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMesAno() {
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }
}
