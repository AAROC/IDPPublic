/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionSupport;
import it.infn.ct.security.utilities.LDAPUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import it.infn.ct.security.utilities.Organization;

public class OrgAction extends ActionSupport {

    private List<Organization> OrgList = new ArrayList();
    private Log log = LogFactory.getLog(OrgAction.class);
<<<<<<< HEAD
=======
    
    private String country;
>>>>>>> 45af0525f6756d510118cd1e6341e390b2e46f29

    public List getOrgList() {
        return OrgList;
    }


    public OrgAction() {
        OrgList = LDAPUtils.getOrgList();
<<<<<<< HEAD
=======
        if(country!=null && !country.isEmpty()){
            for(Organization o: OrgList){
                if(!o.getCountryCode().equals(country)){
                    OrgList.remove(o);
                }
            }
        }
>>>>>>> 45af0525f6756d510118cd1e6341e390b2e46f29
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String display() {
        return NONE;
    }
<<<<<<< HEAD
=======

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    
>>>>>>> 45af0525f6756d510118cd1e6341e390b2e46f29
}