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
@Named(value = "exameDiarioController")
@RequestScoped
public class ExameDiarioController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;
    
    private Date dataInicial;
    private Date dataFinal;
    private int convenio;

    public ExameDiarioController() {
        convenio = 1;
    }
    
    
    
    public void emitirToExcel() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicial", dataInicial);
            parametros.put("data_final", dataFinal);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/EXAMES_DIARIO.jasper",
                    this.response, parametros, "exames_diarios.xls");

            Connection connection = ConnectionFactory.createConnectionToOracle();

            executor.executeToExcel(connection);

            if (executor.isRelatorioGerado()) {
                facesContext.responseComplete();
            } else {
                JsfUtil.addErrorMessage("A execução do relatório não retornou dados.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioAntiMicrobianoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void emitirToExcel2() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicial", dataInicial);
            parametros.put("data_final", dataFinal);
            parametros.put("convenio", convenio);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/EXAMES_DIARIO_FILTER.jasper",
                    this.response, parametros, "exames_diarios_por_convenio.xls");

            Connection connection = ConnectionFactory.createConnectionToOracle();

            executor.executeToExcel(connection);

            if (executor.isRelatorioGerado()) {
                facesContext.responseComplete();
            } else {
                JsfUtil.addErrorMessage("A execução do relatório não retornou dados.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioAntiMicrobianoController.class.getName()).log(Level.SEVERE, null, ex);
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

    public int getConvenio() {
        return convenio;
    }

    public void setConvenio(int convenio) {
        this.convenio = convenio;
    }
    
    
}
