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
@Named(value = "relatorioAntimicrobianoController")
@RequestScoped
public class RelatorioAntiMicrobianoController {

    private Date dataInicio;
    private Date dataFim;
    
    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;

    public void emitir() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("dt_inicio", dataInicio);
            parametros.put("dt_fim", dataFim);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/antimicrobiano.jasper",
                    this.response, parametros, "antimicrobiano.pdf");

            Connection connection = ConnectionFactory.createConnectionToOracle();

            executor.execute(connection);

            if (executor.isRelatorioGerado()) {
                facesContext.responseComplete();
            } else {
                JsfUtil.addErrorMessage("A execução do relatório não retornou dados.");
            }
            
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioAntiMicrobianoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void emitirToExcel() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("dt_inicio", dataInicio);
            parametros.put("dt_fim", dataFim);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/antimicrobiano.jasper",
                    this.response, parametros, "antimicrobiano.xls");

            Connection connection = ConnectionFactory.createConnectionToOracle();

            executor.executeToExcel(connection);

            if (executor.isRelatorioGerado()) {
                facesContext.responseComplete();
            } else {
                JsfUtil.addErrorMessage("A execução do relatório não retornou dados.");
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioAntiMicrobianoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}
