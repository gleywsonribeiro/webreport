/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.hmue.webreport.jsf.util.view;


import br.org.hmue.webreport.jsf.util.JsfUtil;
import br.org.hmue.webreport.service.NegocioException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author gleyw
 */
public class JsfExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    public JsfExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable exception = context.getException();
            NegocioException negocioException = getNegocioException(exception);
            
            boolean tratado = false;

            try {
                if (exception instanceof ViewExpiredException) {
                    tratado = true;
                    JsfUtil.redirect("/");
                } else if (negocioException != null) {
                    tratado = true;
                    JsfUtil.addErrorMessage(negocioException.getMessage());
                } else {
                    tratado = true;
                    //log deve ficar aqui -> log.error("Erro de sistema: " + exception.getMessage(), exception);
                    JsfUtil.redirect("/erro.xhtml");
                }
            } finally {
                if (tratado) {
                    events.remove();
                }
            }
        }

        getWrapped().handle();
    }

    private NegocioException getNegocioException(Throwable exception) {
        if(exception instanceof NegocioException) {
            return (NegocioException)exception;
        } else if(exception.getCause() != null) {
            return getNegocioException(exception.getCause());
        }
        
        return null;
    }

}
