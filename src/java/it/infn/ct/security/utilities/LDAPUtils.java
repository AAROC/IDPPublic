/**
 * *********************************************************************
 * Copyright (c) 2011: Istituto Nazionale di Fisica Nucleare (INFN), Italy
 * Consorzio COMETA (COMETA), Italy
 *
 * See http://www.infn.it and and http://www.consorzio-cometa.it for details on
 * the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **********************************************************************
 */
package it.infn.ct.security.utilities;

import it.infn.ct.security.entities.UserRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class LDAPUtils {

    private static Log _log= LogFactory.getLog(LDAPUtils.class);
    
    private static DirContext getContext() throws NamingException {
        ResourceBundle rb = ResourceBundle.getBundle("ldap");
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, rb.getString("url"));
        env.put(Context.SECURITY_PRINCIPAL, rb.getString("rootDN"));
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        return new InitialDirContext(env);
    }

    private static DirContext getAuthContext(String userCN, String password, boolean dedicatedAdminUser) throws NamingException {
        ResourceBundle rb = ResourceBundle.getBundle("ldap");
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, rb.getString("url"));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        if(dedicatedAdminUser){
            env.put(Context.SECURITY_PRINCIPAL, rb.getString("bindDN"));
            env.put(Context.SECURITY_CREDENTIALS, rb.getString("bindPass"));
        }
        else{
            env.put(Context.SECURITY_PRINCIPAL, "cn=" + userCN + "," + rb.getString("peopleRoot"));
            env.put(Context.SECURITY_CREDENTIALS, password);
        }

        return new InitialDirContext(env);
        
    }
    
    private static DirContext getAuthContext(String userCN, String password) throws NamingException {
        return getAuthContext(userCN, password, false);
    }
    
    private static DirContext getMainAuthContext() throws NamingException {
        return getAuthContext(null, null, true);
    }

    public static LDAPUser findUserByMail(String mail){
        NamingEnumeration results = null;
        DirContext ctx = null;
        LDAPUser user= null;
        try {
            ctx = getContext();
            SearchControls controls = new SearchControls();
            String retAttrs[] = {"cn"};
            controls.setReturningAttributes(retAttrs);
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            results = ctx.search(
                   rb.getString("peopleRoot"), "(mail=" + mail + ")", controls);
            if (results.hasMore()) {
                SearchResult searchResult =
                        (SearchResult) results.next();
                Attributes attributes =
                        searchResult.getAttributes();
                user = new LDAPUser();

                if(attributes.get("cn")!=null)
                    user= getUser((String) attributes.get("cn").get());
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            _log.error(e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }
        return user;
        
    }
    
    public static boolean isMailUsed(String mail) {
        boolean registered= false;
        NamingEnumeration results = null;
        DirContext ctx = null;
        try {
            ctx = getContext();
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            results = ctx.search(
                   rb.getString("peopleRoot"), "(mail=" + mail + ")", controls);
            if (results.hasMore()) {
                registered = true;
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            registered = true;
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }
        return registered;
    }
    
    
    public static boolean isCNregistered(String cn) {
        boolean registered = false;
        NamingEnumeration results = null;
        DirContext ctx = null;
        try {
            ctx = getContext();
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            results = ctx.search(
                   rb.getString("peopleRoot"), "(cn=" + cn + ")", controls);
            if (results.hasMore()) {
                registered = true;
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            registered = true;
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }


        return registered;
    }

    public static List<Organization> getOrgList() {
        return getOrgList(null);
        
    }
    
    public static List<Organization> getOrgList(String country) {
        List<Organization> OrgList = new ArrayList<Organization>();
        NamingEnumeration resultCountries = null;
        DirContext ctx = null;
        try {
            ctx = getContext();
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            String filter;
            if(country==null){
                filter="(objectclass=country)";
            }
            else{
                filter="(&(objectclass=country)(c="+country+"))";
            }
            resultCountries = ctx.search(
                    rb.getString("organisationsRoot"), 
                    filter, controls);

            while (resultCountries.hasMore()) {
                SearchResult searchResult =
                        (SearchResult) resultCountries.next();
                Attributes attributes =
                        searchResult.getAttributes();
                String countryCode = (String) attributes.get("c").get();
                String countryName = (String) attributes.get("co").get();
                
                NamingEnumeration resultsOrgs= ctx.search(
                        "c="+countryCode+","+rb.getString("organisationsRoot"),
                        "(objectclass=organization)", 
                        controls);
                while(resultsOrgs.hasMore()){
                    SearchResult srOrg=(SearchResult) resultsOrgs.next();
                    Attributes orgAttrs= srOrg.getAttributes();
                    String description = "";
                    if ((orgAttrs.get("description")) != null) {
                        description = (String) orgAttrs.get("description").get();
                    }
                    
                    OrgList.add(new Organization((String) orgAttrs.get("o").get(), countryName, countryCode, description, srOrg.getNameInNamespace()));
                }
                resultsOrgs.close();
                
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultCountries != null) {
                try {
                    resultCountries.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }

        Collections.sort(OrgList, new Comparator<Organization>(){

            public int compare(Organization o1, Organization o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });

        return OrgList;

    }

    public static String getOrgDN(String organisation, String countryCode) {
        NamingEnumeration results = null;
        DirContext ctx = null;
        String dn=null;
        try {
            ctx = getContext();
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String retAttrs[] = {"dn"};
            controls.setReturningAttributes(retAttrs);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            results = ctx.search(
                    "c="+countryCode+","+rb.getString("organisationsRoot"), "(&(objectclass=organization)(o="+organisation+"))", controls);

            if (results.hasMore()) {
                SearchResult searchResult =
                        (SearchResult) results.next();
                dn=searchResult.getNameInNamespace()    ;
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }
        
        return dn;
    }

    
    
    
    public static List<LDAPUser> getIdPUserList() {
        List<LDAPUser> users = new ArrayList<LDAPUser>();
        NamingEnumeration results = null;
        DirContext ctx;
        ResourceBundle rb = ResourceBundle.getBundle("ldap");
        try {
            ctx= getMainAuthContext();
            String attrs[]={"uniqueMember"};
            Attribute userGAttr= ctx.getAttributes(rb.getString("usersGroup"), attrs).get("uniqueMember");
            for(int i=0; i<userGAttr.size(); i++){
                String userDN= (String)userGAttr.get(i);
                users.add(getUser(userDN.substring(userDN.indexOf("=")+1,userDN.indexOf(","))));

            }
            
        } catch (NamingException ex) {
            _log.error(ex);
        }
        
        
        
        return users;
    }
    
    public static LDAPUser getIfValidUser(String cn, String password) {
        LDAPUser user = null;
        NamingEnumeration results = null;
        DirContext ctx = null;
        try {
            ctx = getAuthContext(cn, password);
            SearchControls controls = new SearchControls();
            String retAttrs[] = {"cn","sn","givenName","title","registeredAddress","mail","memberOf","createTimestamp"};
            controls.setReturningAttributes(retAttrs);
            controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            results = ctx.search(
                    rb.getString("peopleRoot"), "(cn=" + cn + ")", controls);
            if (results.hasMore()) {
                SearchResult searchResult =
                        (SearchResult) results.next();
                Attributes attributes =
                        searchResult.getAttributes();
                user = new LDAPUser();

                if(attributes.get("cn")!=null)
                    user.setUsername((String) attributes.get("cn").get());
                if(attributes.get("sn")!=null)
                    user.setSurname((String) attributes.get("sn").get());
                if(attributes.get("givenName")!=null)
                    user.setGivenname((String) attributes.get("givenName").get());
                if(attributes.get("title")!=null)
                    user.setTitle((String) attributes.get("title").get());
                if(attributes.get("registeredAddress")!=null)
                    user.setPreferredMail((String) attributes.get("registeredAddress").get(0));
                if(attributes.get("mail")!=null){
                    String mails = "";
                    for(int i=0; i<attributes.get("mail").size(); i++){
                        if(i!=0)
                            mails=mails+", ";
                        mails=mails+(String)attributes.get("mail").get(i);
                    }
                    user.setAdditionalMails(mails);
                }
                if(attributes.get("memberOf")!=null){
                    for(int i=0; i< attributes.get("memberOf").size(); i++){
                        user.addGroup((String) attributes.get("memberOf").get(i));
                    }
                }
                if(attributes.get("createTimestamp")!=null){
                    String time=(String)attributes.get("createTimestamp").get();
                    DateFormat ldapData= new SimpleDateFormat("yyyyMMddHHmmss");
                    user.setCreationTime(ldapData.parse(time));
                }
                
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            _log.error(e);
        } catch (ParseException ex) {
            _log.error(ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }


        return user;
    }
    
    
    public static boolean enableUser(String cn) {
        return toggleUserIDPGroup(cn, true);
    }
    
    public static boolean disableUser(String cn) {
        return toggleUserIDPGroup(cn, false);
        
    }
    
    private static boolean toggleUserIDPGroup(String cn, boolean activate){
        ResourceBundle rb = ResourceBundle.getBundle("ldap");
        String userDN= "cn="+cn+","+rb.getString("peopleRoot");
        String idpUser= rb.getString("usersGroup");
                
        DirContext ctx = null;
        try {
            ctx= getMainAuthContext();
            
            ModificationItem modAttrs[] = new ModificationItem[1];
            String attrsList[]={"uniqueMember"};
            Attributes attributes= ctx.getAttributes(idpUser, attrsList);
            
            Attribute att= attributes.get("uniqueMember");
            if(activate){
                att.add(userDN);
            }
            else{
                att.remove(userDN);
            }
            
            modAttrs[0]= new ModificationItem(DirContext.REPLACE_ATTRIBUTE, att);
            ctx.modifyAttributes(idpUser, modAttrs);
            return true;
        } catch (NamingException ex) {
            _log.error(ex);
        }
        
        return false;
        
    }
    
    public static LDAPUser getUser(String cn) {
        LDAPUser user = null;
        NamingEnumeration results = null;
        DirContext ctx = null;
        try {
            ctx = getContext();
            SearchControls controls = new SearchControls();
            String retAttrs[] = {"cn","sn","givenName","title","registeredAddress","mail","memberOf", "createTimestamp"};
            controls.setReturningAttributes(retAttrs);
            controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            ResourceBundle rb = ResourceBundle.getBundle("ldap");

            results = ctx.search(
                    rb.getString("peopleRoot"), "(cn=" + cn + ")", controls);
            if (results.hasMore()) {
                SearchResult searchResult =
                        (SearchResult) results.next();
                Attributes attributes =
                        searchResult.getAttributes();
                user = new LDAPUser();

                if(attributes.get("cn")!=null)
                    user.setUsername((String) attributes.get("cn").get());
                if(attributes.get("sn")!=null)
                    user.setSurname((String) attributes.get("sn").get());
                if(attributes.get("givenName")!=null)
                    user.setGivenname((String) attributes.get("givenName").get());
                if(attributes.get("title")!=null)
                    user.setTitle((String) attributes.get("title").get());
                if(attributes.get("registeredAddress")!=null)
                    user.setPreferredMail((String) attributes.get("registeredAddress").get(0));
                if(attributes.get("mail")!=null){
                    String mails = "";
                    for(int i=0; i<attributes.get("mail").size(); i++){
                        if(i!=0)
                            mails=mails+", ";
                        mails=mails+(String)attributes.get("mail").get(i);
                    }
                    user.setAdditionalMails(mails);
                }
                if(attributes.get("memberOf")!=null){
                    for(int i=0; i< attributes.get("memberOf").size(); i++){
                        user.addGroup((String) attributes.get("memberOf").get(i));
                    }
                }
                
                if(attributes.get("createTimestamp")!=null){
                    String time=(String)attributes.get("createTimestamp").get();
                    DateFormat ldapData= new SimpleDateFormat("yyyyMMddHHmmss");
                    user.setCreationTime(ldapData.parse(time));
                }
                
            }
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            _log.error(e);
        } catch (ParseException ex) {
            _log.error(ex);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }


        return user;
    }
    
    
    public static boolean registerUser(LDAPUser lus, UserRequest userReq){
        return registerUser(lus, userReq, null, null);
    }
    public static boolean registerUser(LDAPUser lus, UserRequest userReq, String OrgDN){
        return registerUser(lus, userReq, OrgDN, null);
    }
    public static boolean registerUser(LDAPUser lus, UserRequest userReq, String OrgDN, String OrgUDN){
        boolean registration=false;
        DirContext ctx = null;
        try {
            ctx = getAuthContext(lus.getUsername(), lus.getPassword());
                        
            Attributes attrsBag = new BasicAttributes();

            Attribute oc = new BasicAttribute("objectClass");
            oc.add("inetOrgPerson");
            oc.add("organizationalPerson");
            oc.add("person");
            oc.add("top");
            attrsBag.put(oc);
            
            Attribute sn = new BasicAttribute("sn", userReq.getSurname());
            attrsBag.put(sn);
            
            Attribute cn = new BasicAttribute("cn", userReq.getUsername());
            attrsBag.put(cn);
            
            Attribute dispName = new BasicAttribute("displayName", userReq.getUsername());
            attrsBag.put(dispName);
            
            Attribute uPass = new BasicAttribute("userPassword", userReq.getPassword());
            attrsBag.put(uPass);
            
            Attribute regAdd = new BasicAttribute("registeredAddress", userReq.getPreferredMail());
            attrsBag.put(regAdd);
            
            if(userReq.getTitle()!=null && !userReq.getTitle().isEmpty()){
                Attribute title = new BasicAttribute("title", userReq.getTitle());
                attrsBag.put(title);
            }
            
            Attribute gName = new BasicAttribute("givenName", userReq.getGivenname());
            attrsBag.put(gName);
            
            Attribute inits = new BasicAttribute("initials", userReq.getGivenname().substring(0, 1).toUpperCase()+userReq.getSurname().substring(0, 1).toUpperCase());
            attrsBag.put(inits);
            
            Attribute mails = new BasicAttribute("mail");
            mails.add(userReq.getPreferredMail());
            for(String adMail: userReq.getAdditionalMails().split("[,\\s;]"))
                if(!adMail.isEmpty())
                    mails.add(adMail.trim());
            attrsBag.put(mails);
            
            Attribute org = new BasicAttribute("o", OrgDN);
            attrsBag.put(org);
            
            if(OrgUDN!=null && !OrgUDN.isEmpty()){
                Attribute orgU = new BasicAttribute("ou", OrgUDN);
                attrsBag.put(orgU);
            }
                        
            ResourceBundle rb = ResourceBundle.getBundle("ldap");
            ctx.createSubcontext("cn="+userReq.getUsername()+","+rb.getString("peopleRoot"), attrsBag);
            
            ModificationItem[] modItems = new ModificationItem[1]; 
            modItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("uniqueMember", "cn="+userReq.getUsername()+","+rb.getString("peopleRoot")));

            ctx.modifyAttributes(rb.getString("usersGroup"), modItems);

            registration=true;
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            _log.error(e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }
        
        return registration;
    }
    
    public static boolean resetPassword(String cn, String newPassword){
        DirContext ctx = null;
        try {
            ctx = getMainAuthContext();
            
            ModificationItem[] modItems = new ModificationItem[1]; 
            modItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", newPassword));

            ResourceBundle rb = ResourceBundle.getBundle("ldap");
            
            ctx.modifyAttributes("cn="+cn+","+rb.getString("peopleRoot"), modItems);
        } catch (NamingException ex) {
            _log.error(ex);
            return false;
        }

        
        return true;
    }
    
    public static boolean updatePassword(LDAPUser user, String newPassword){
        DirContext ctx = null;
        try {
            ctx = getAuthContext(user.getUsername(), user.getPassword());
            
            ModificationItem[] modItems = new ModificationItem[1]; 
            modItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", newPassword));

            ResourceBundle rb = ResourceBundle.getBundle("ldap");
            
            ctx.modifyAttributes("cn="+user.getUsername()+","+rb.getString("peopleRoot"), modItems);
        } catch (NamingException ex) {
            _log.error(ex);
            return false;
        }

        
        return true;
    }
    
    public static boolean addMail(LDAPUser user, String newMail){
        DirContext ctx = null;
        try {
            ctx = getAuthContext(user.getUsername(), user.getPassword());
            
            ModificationItem[] modItems = new ModificationItem[1]; 
            modItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("mail", newMail));

            ResourceBundle rb = ResourceBundle.getBundle("ldap");
            
            ctx.modifyAttributes("cn="+user.getUsername()+","+rb.getString("peopleRoot"), modItems);
        } catch (NamingException ex) {
            _log.error(ex);
            return false;
        }

        
        return true;
    }

    public static boolean addOrganisation(LDAPUser lus, Organization org){
        boolean registration=false;
        DirContext ctx = null;
        try {
            ctx = getAuthContext(lus.getUsername(), lus.getPassword());
                        
            Attributes attrsBag = new BasicAttributes();

            Attribute oc = new BasicAttribute("objectClass");
            oc.add("organization");
            oc.add("top");
            attrsBag.put(oc);
            
            Attribute o = new BasicAttribute("o", org.getKey());
            attrsBag.put(o);
            
            Attribute description = new BasicAttribute("description", org.getDescription());
            attrsBag.put(description);
            
            if(org.getReference()!=null && !org.getReference().isEmpty()){
                Attribute registeredAddr = new BasicAttribute("registeredAddress", org.getReference());
                attrsBag.put(registeredAddr);
            }            
            
                        
            ResourceBundle rb = ResourceBundle.getBundle("ldap");
            ctx.createSubcontext("o="+org.getKey()+",c="+org.getCountryCode()+","+rb.getString("organisationsRoot"), attrsBag);
            

            registration=true;
        } catch (NameNotFoundException ex) {
            _log.error(ex);
        } catch (NamingException e) {
            _log.error(e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // Never mind this.
                }
            }
        }
        
        return registration;
        
    }
}
