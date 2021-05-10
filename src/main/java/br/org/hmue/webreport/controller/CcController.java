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
@Named(value = "ccController")
@RequestScoped
public class CcController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;

    private Date dataInicial;
    private Date dataFinal;
    private Integer centro;

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

    public Integer getCentro() {
        return centro;
    }

    public void setCentro(Integer centro) {
        this.centro = centro;
    }

      

    public void ocupacaoToExcel() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicial", dataInicial);
            parametros.put("data_final", dataFinal);

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/cc/ocupacao_sala.jasper",
                    this.response, parametros, "ocupacao_sala.xls");

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
    
    public void geraTemposDeSala() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicio", dataInicial);
            parametros.put("data_fim", dataFinal);
            parametros.put("centro_cir", centro);
            

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/cc/bloco_cirurgico.jasper",
                    this.response, parametros, "bloco_cirurgico.xls");

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
    
    public void geraCirurgiasTurnoSala() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicio", dataInicial);
            parametros.put("data_fim", dataFinal);
            parametros.put("centro_cir", centro);
            

            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/cc/cirurgias_turno_sala.jasper",
                    this.response, parametros, "cirurgias_turno_sala.xls");

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

    public void geraCirurgias() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicial", dataInicial);
            parametros.put("data_final", dataFinal);
            parametros.put("centro_cir", centro);


            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/cc/cirurgias.jasper",
                    this.response, parametros, "cirurgias.xls");

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

    public void geraProcedimentos() {
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("data_inicio", dataInicial);
            parametros.put("data_final", dataFinal);
            parametros.put("centro_cir", centro);


            ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/cc/procedimentos.jasper",
                    this.response, parametros, "procedimentos.xls");

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
}
