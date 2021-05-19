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
@Named(value = "imagemController")
@RequestScoped
public class ImagemController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletResponse response;

    private Date dataInicial;
    private Date dataFinal;

    private Date dataLaudoInicial;
    private Date dataLaudoFinal;


    public void tempoLaudoToExcel() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("dt_ini_realizacao", dataInicial);
        parametros.put("dt_fim_realizacao", dataFinal);
        parametros.put("dt_ini_laudo", dataLaudoInicial);
        parametros.put("dt_fim_laudo", dataLaudoFinal);

        getReport("/relatorios/imagem/tempo_laudo_imagem.jasper", parametros, "tempo_laudo.xls");
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
            Logger.getLogger(ImagemController.class.getName()).log(Level.SEVERE, null, ex);
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

    public Date getDataLaudoInicial() {
        return dataLaudoInicial;
    }

    public void setDataLaudoInicial(Date dataLaudoInicial) {
        this.dataLaudoInicial = dataLaudoInicial;
    }

    public Date getDataLaudoFinal() {
        return dataLaudoFinal;
    }

    public void setDataLaudoFinal(Date dataLaudoFinal) {
        this.dataLaudoFinal = dataLaudoFinal;
    }
}
