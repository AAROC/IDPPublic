/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Web application lifecycle listener.
 *
 * @author marco
 */
public class HibernateListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        SessionFactory factory;
        
        factory = new Configuration().configure().buildSessionFactory();
        sce.getServletContext().setAttribute("IDPPublic.hibernatefactory", factory);               
                
    }

    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
