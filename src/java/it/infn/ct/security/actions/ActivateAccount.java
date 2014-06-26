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
import it.infn.ct.security.entities.UserReActivateRequest;
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;
import java.io.UnsupportedEncodingException;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class ActivateAccount extends ActionSupport{
    private Log _log= LogFactory.getLog(ActivateAccount.class);
    private String id;
    private String action;
    
    private String mailFrom;
    private String mailBCC;
    private String mailSubject;
    private String mailBody;
    private String idPAdmin;

    @Override
    public String execute() throws Exception {
        LDAPUser adminUser = (LDAPUser) ActionContext.getContext().getSession().get("ldapUser");
        
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        session.beginTransaction();
        UserReActivateRequest urer= (UserReActivateRequest)session.get(UserReActivateRequest.class, Long.parseLong(id));
        if(action.equals("activate")){
            LDAPUtils.enableUser(urer.getUsername());
            urer.setApproved(true);
            _log.info("Account for the user "+urer.getUsername()+" activated by "+adminUser.getUsername());
            sendMail(LDAPUtils.getUser(urer.getUsername()), true);
        }
        if(action.equals("delete")){
            _log.info("Reject activation for the account of the user "+urer.getUsername()+" by "+adminUser.getUsername());
            sendMail(LDAPUtils.getUser(urer.getUsername()), false);
        }
        
        urer.setApproved(false);
        urer.setOpen(false);
        
        urer.setHandledBy(adminUser.getUsername());
        session.merge(urer);
        session.getTransaction().commit();
        session.close();
        
        return SUCCESS;
    }
    
    
    
    private void sendMail(LDAPUser user, boolean enabled) throws MailException{
        javax.mail.Session session=null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            session = (javax.mail.Session) envCtx.lookup("mail/Users");

        } catch (Exception ex) {
            _log.error("Mail resource lookup error");
            _log.error(ex.getMessage());
            throw new MailException("Mail Resource not available");
        }
        
        Message mailMsg = new MimeMessage(session);
        try {
            mailMsg.setFrom(new InternetAddress(mailFrom,idPAdmin));
            
            InternetAddress mailTos[] = new InternetAddress[1];
            mailTos[0] = new InternetAddress(user.getPreferredMail());
            mailMsg.setRecipients(Message.RecipientType.TO, mailTos);
            
            _log.error("mail bcc: "+mailBCC);
            String ccMail[] = mailBCC.split(";");
            InternetAddress mailCCopy[] = new InternetAddress[ccMail.length];
            for(int i=0; i<ccMail.length;i++){
                mailCCopy[i] = new InternetAddress(ccMail[i]);
            }
            
            mailMsg.setRecipients(Message.RecipientType.BCC, mailCCopy);
            
            
            mailMsg.setSubject(mailSubject);
            
            
            mailBody= mailBody.replaceAll("_USER_", user.getTitle() +" "+user.getGivenname()+" "+user.getSurname()+" ("+user.getUsername()+")");
            if(enabled){
                mailBody= mailBody.replace("_RESULT_", "accepted");
            }
            else{
                mailBody= mailBody.replace("_RESULT_", "denied");
            }
            mailMsg.setText(mailBody);

            Transport.send(mailMsg);
            
            
        } catch (UnsupportedEncodingException ex) {
            _log.error(ex);
            throw new MailException("Mail address format not valid");
        } catch (MessagingException ex) {
            _log.error(ex);
            throw new MailException("Mail message has problems");
        }
        
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailBCC() {
        return mailBCC;
    }

    public void setMailBCC(String mailBCC) {
        this.mailBCC = mailBCC;
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
