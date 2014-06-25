/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.utilities;

import it.infn.ct.security.actions.MailException;
import it.infn.ct.security.entities.UserRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class LDAPCleanerTest {
    LDAPCleaner lClean= new LDAPCleaner(new Configuration().configure().buildSessionFactory());
//    LDAPCleaner lClean= new LDAPCleaner(null);
    public LDAPCleanerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class LDAPCleaner.
     */
    @Test
    public void testRun() {
        System.out.println("Check 	............	LDAPCleaner");
        Thread instance= new Thread(lClean);
        instance.start();
        try {
            assertTrue("Cleaner started",instance.isAlive());
            instance.join();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(LDAPCleanerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
/*    @Test
    public void testMail(){
        System.out.println("Check 	............	LDAPCleaner.getGoodMail()");
        UserRequest ur= new UserRequest();
        
        ur.setPreferredMail("fmarco76@paperino.com");
        try {
            
            assertEquals(ur.getPreferredMail(), lClean.getGoodMail(ur));
            System.out.println(lClean.getGoodMail(ur));
        } catch (MailException ex) {
            fail("Exception with mail");
//            Logger.getLogger(LDAPCleanerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
*/
}