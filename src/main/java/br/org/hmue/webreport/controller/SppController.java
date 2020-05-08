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
import java.util.Date;
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
@Named(value = "sppController")
@RequestScoped
public class SppController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;

    private Date dataInicial;
    private Date dataFinal;

    public void censoToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
//            parametros.put("data_inicial", dataInicial);
//            parametros.put("data_final", dataFinal);
        getReport("/relatorios/spp/censo.jasper", parametros, "censo.xls");
    }

    public void altasToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("date_inicial", dataInicial);
        parametros.put("date_final", dataFinal);
        getReport("/relatorios/spp/saidas.jasper", parametros, "saidas.xls");
    }

    public void fauToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("date_inicial", dataInicial);
        parametros.put("date_final", dataFinal);

        getReport("/relatorios/spp/fau.jasper", parametros, "fau.xls");
    }

    public void ambulatorialToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("data_inicial", dataInicial);
        parametros.put("data_final", dataFinal);

        getReport("/relatorios/spp/atend_amb.jasper", parametros, "ambulatorial.xls");
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
            Logger.getLogger(SppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

}
