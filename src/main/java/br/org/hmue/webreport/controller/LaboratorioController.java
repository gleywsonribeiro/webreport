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
@Named(value = "laboratorioController")
@RequestScoped
public class LaboratorioController {
 @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;
    
    private Date dataInicial;
    private Date dataFinal;
    private String mesAno;
    
    
    public void tempoExameToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("data_inicial", dataInicial);
        parametros.put("data_final", dataFinal);

        execute("/relatorios/laboratorio/exames_urgentes.jasper", "exames_urgentes.xls", parametros);
    }

    public void exames() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("data_inicial", dataInicial);
        parametros.put("data_final", dataFinal);

        execute("/relatorios/laboratorio/exames_realizados.jasper", "exames.xls", parametros);
    }
    
    private void execute(String caminho, String arquivoSaida, Map parametros) {
        try {
            ExecutorRelatorio executor = new ExecutorRelatorio(caminho,
                    this.response, parametros, arquivoSaida);

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

    public String getMesAno() {
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }
}
