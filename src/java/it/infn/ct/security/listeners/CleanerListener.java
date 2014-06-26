/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.listeners;

import it.infn.ct.security.utilities.LDAPCleaner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class CleanerListener implements ServletContextListener{
    Log _log= LogFactory.getLog(CleanerListener.class);
    
    private final ScheduledExecutorService ses= Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> schefut;

    public void contextInitialized(ServletContextEvent sce) {
        _log.info("Start the user accounts monitor and cleaner");

        SessionFactory factory= (SessionFactory) sce.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        schefut= ses.scheduleAtFixedRate(new LDAPCleaner(factory), 0, 1, TimeUnit.DAYS);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        _log.info("Stopping the user accounts monitor and cleaner");
        schefut.cancel(true);
        
    }

    
    
    
}
