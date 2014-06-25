/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.utilities;

import java.util.List;
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
public class LDAPUtilsTest {
    
    public LDAPUtilsTest() {
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
     * Test of getUserList method, of class LDAPUtils.
     */
    @Test
    public void testGetIdPUserList() {
        System.out.println("Check \t............\t LDAPUtils.getUserList()");
        List<LDAPUser> result = LDAPUtils.getIdPUserList();
        System.out.println("\t..............identified "+result.size()+" users");
        assertNotNull(result);
        assertTrue(result.size()>0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
    /**
     * Test of enableUser method, of class LDAPUtils.
     */
    @Test
    public void testEnableUser() {
        System.out.println("Check \t............\t LDAPUtils.enableUser()");
//        List<LDAPUser> result = LDAPUtils.getUserList();
        
        assertTrue(LDAPUtils.enableUser("paperino"));
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }


}