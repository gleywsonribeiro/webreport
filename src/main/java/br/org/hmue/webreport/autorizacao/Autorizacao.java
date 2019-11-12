/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.hmue.webreport.autorizacao;


import br.org.hmue.webreport.modelo.Usuario;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gleywson
 */
public class Autorizacao implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String currentPage = fc.getViewRoot().getViewId();

        boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        Usuario currentUser = (Usuario) session.getAttribute("currentUser");

        if (!isLoginPage && currentUser == null) {
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "loginPage");
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
