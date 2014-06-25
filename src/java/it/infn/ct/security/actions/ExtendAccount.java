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

import com.opensymphony.xwork2.ActionSupport;
import it.infn.ct.security.entities.UserConfirmUpdate;
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;
import java.io.UnsupportedEncodingException;
import java.util.List;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class ExtendAccount extends ActionSupport{
    private Log _log= LogFactory.getLog(ExtendAccount.class);
    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailBody;
    private String username;

    @Override
    public String execute() throws Exception {

        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        session.beginTransaction();
        for(UserConfirmUpdate conf: (List<UserConfirmUpdate>)session.createCriteria(UserConfirmUpdate.class)
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("updated", Boolean.FALSE))
                .list()){

            conf.setUpdated(true);
            session.save(conf);
        }
        session.getTransaction().commit();
        session.close();

        sendMail();
        
        return SUCCESS;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
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
    
    private void sendMail() throws MailException{
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
            mailMsg.setFrom(new InternetAddress(mailFrom,mailFrom));
            
            InternetAddress mailTos[] = new InternetAddress[1];
            mailTos[0] = new InternetAddress(mailTo);
            mailMsg.setRecipients(Message.RecipientType.TO, mailTos);
            
            
            mailMsg.setSubject(mailSubject);
            
            LDAPUser user= LDAPUtils.getUser(username);
            
            mailBody = mailBody.replaceAll("_USER_", user.getTitle() +" "+user.getGivenname()+" "+user.getSurname()+" ("+user.getUsername()+")");

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
    

}
