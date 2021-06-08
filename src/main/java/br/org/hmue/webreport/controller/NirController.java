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
    private String mesAno;

    public void censoPorSetor() {
        Map<String, Object> parametros = new HashMap<String, Object>();

        if (codigo.isEmpty()) {
            execute("/relatorios/nir/censo.jasper", "censo.xls", parametros);
        } else {
            parametros.put("codigo", codigo);
            execute("relatorios/nir/cesnso_setor.jasper", "censo_por_setor.xls", parametros);
        }
    }


    public void metaClinica() {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("mes_ano", mesAno);
        execute("/relatorios/nir/meta_clinica.jasper", "meta_clinica.xls", parametros);
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

    public String getMesAno() {
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
