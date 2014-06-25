/***********************************************************************
 *  Copyright (c) 2011: 
 *  Istituto Nazionale di Fisica Nucleare (INFN), Italy
 *  Consorzio COMETA (COMETA), Italy
 * 
 *  See http://www.infn.it and and http://www.consorzio-cometa.it for details on
 *  the copyright holders.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ***********************************************************************/

package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;
import it.infn.ct.security.entities.UserRequest;
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;
import it.infn.ct.security.utilities.Organization;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class RegisterUser extends ActionSupport implements Parameterizable{
    private Log log = LogFactory.getLog(AddUser.class);
    private String user_id;
    private String organizationDN;
    private String organizationUnitDN;
    private String orgname;
    private String orgdesc;
    private String orgref;
    private Map<String, String> orgs;
    private UserRequest userReq;
    
    private Map<String, String> actionParams;
    private String mailFrom;
    private String mailCC;
    private String mailSubject;
    private String mailBody;
    private String idPAdmin;
    
    
    @Override
    public String execute() throws Exception {
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        if(userReq==null){
            userReq = (UserRequest) session.get(UserRequest.class, Long.parseLong(user_id));
        }
        
        String userOrg;
        if(organizationDN.equals("newOrg")){
            
            LDAPUtils.addOrganisation( (LDAPUser) ActionContext.getContext().getSession().get("ldapUser"), new Organization(orgname, null, userReq.getCountry(), orgdesc, NONE, orgref));
            
            userOrg= LDAPUtils.getOrgDN(orgname, userReq.getCountry());
        }
        else{
            userOrg= organizationDN;
        }
        
        if(LDAPUtils.registerUser( (LDAPUser) ActionContext.getContext().getSession().get("ldapUser"), userReq, userOrg, organizationUnitDN)){
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM UserRequest u WHERE u.id = :id");
            query.setString("id",user_id);
            int r=query.executeUpdate();
            session.getTransaction().commit();
            session.close();
            sendMail(userReq);
            return SUCCESS;
        }
        
        session.close();
        return ERROR;
    }

    @Override
    public void validate() {
        if(userReq==null){
            SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
            Session session = factory.openSession();
            userReq = (UserRequest) session.get(UserRequest.class, Long.parseLong(user_id));
            session.close();
        }
        if(organizationDN.equals("newOrg")){
            String orgDN= LDAPUtils.getOrgDN(orgname, userReq.getCountry());
            if(orgDN!=null && !orgDN.isEmpty()){
                addFieldError("orgname", "An organisation with this name is already registered");
                userReq.setOrganizationDN(LDAPUtils.getOrgDN(userReq.getOrganization(), userReq.getCountry()));
                orgs= new LinkedHashMap<String, String>();

                for(Organization o: LDAPUtils.getOrgList(userReq.getCountry())){
                    orgs.put(o.getDn(), o.getKey()+" - "+o.getDescription());
                }

            }
        }
    }
    
    public String getOrganizationDN() {
        return organizationDN;
    }

    public void setOrganizationDN(String organizationDN) {
        this.organizationDN = organizationDN;
    }

    public String getOrganizationUnitDN() {
        return organizationUnitDN;
    }

    public void setOrganizationUnitDN(String organizationUnitDN) {
        this.organizationUnitDN = organizationUnitDN;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getOrgdesc() {
        return orgdesc;
    }

    public void setOrgdesc(String orgdesc) {
        this.orgdesc = orgdesc;
    }

    public String getOrgref() {
        return orgref;
    }

    public void setOrgref(String orgref) {
        this.orgref = orgref;
    }

    public Map<String, String> getOrgs() {
        return orgs;
    }

    public void setOrgs(Map<String, String> orgs) {
        this.orgs = orgs;
    }
    
    private void sendMail(UserRequest usreq) throws MailException{
        javax.mail.Session session=null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            session = (javax.mail.Session) envCtx.lookup("mail/Users");

        } catch (Exception ex) {
            log.error("Mail resource lookup error");
            log.error(ex.getMessage());
            throw new MailException("Mail Resource not available");
        }
        
        Message mailMsg = new MimeMessage(session);
        try {
            mailMsg.setFrom(new InternetAddress(mailFrom,idPAdmin));
            
            InternetAddress mailTo[] = new InternetAddress[1];
            mailTo[0] = new InternetAddress(usreq.getPreferredMail(), usreq.getTitle()+" "+usreq.getGivenname()+" "+usreq.getSurname());
            mailMsg.setRecipients(Message.RecipientType.TO, mailTo);
            
            String ccMail[] = mailCC.split(";");
            InternetAddress mailCCopy[] = new InternetAddress[ccMail.length];
            for(int i=0; i<ccMail.length;i++) {
                mailCCopy[i] = new InternetAddress(ccMail[i]);
            }

            mailMsg.setRecipients(Message.RecipientType.CC, mailCCopy);
            
            mailMsg.setSubject(mailSubject);
            
            
            mailBody = mailBody.replaceAll("_USER_", usreq.getTitle()+" "+usreq.getGivenname()+" "+usreq.getSurname());
            
            mailMsg.setText(mailBody);
            
            Transport.send(mailMsg);
            
            
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
            throw new MailException("Mail from address format not valid");
        } catch (MessagingException ex) {
            log.error(ex);
            throw new MailException("Mail message has problems");
        }
        
    }

    public void addParam(String name, String value) {
        if(actionParams==null)
            actionParams = new HashMap<String, String>();
        
        actionParams.put(name, value);
    }

    public void setParams(Map<String, String> map) {
        actionParams = map;
    }

    public Map<String, String> getParams() {
        return actionParams;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailCC() {
        return mailCC;
    }

    public void setMailCC(String mailCC) {
        this.mailCC = mailCC;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public String getIdPAdmin() {
        return idPAdmin;
    }

    public void setIdPAdmin(String idPAdmin) {
        this.idPAdmin = idPAdmin;
    }


    
}
