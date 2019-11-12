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
@Named(value = "entradaController")
@RequestScoped
public class EntradaController {

    private Date dataInicial;
    private Date dataFinal;
    private Integer atendimento;
    
    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;
    
    public void emitirToExcel() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("dt_inicial", dataInicial);
            parametros.put("dt_final", dataFinal);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/entradas.jasper",
                    this.response, parametros, "entradas.xls");

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
    
    
    public void emitirVisitantesAcampanhantesToExcel() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("atendimento", atendimento);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/acompanhantes_visitantes.jasper",
                    this.response, parametros, "visitantes_acompanhantes.xls");

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

    public Integer getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Integer atendimento) {
        this.atendimento = atendimento;
    }
    
    
    
}
