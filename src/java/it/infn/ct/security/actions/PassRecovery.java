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
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import it.infn.ct.security.entities.UserRequest;
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
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class PassRecovery extends ActionSupport{
    private Log _log= LogFactory.getLog(PassRecovery.class);
    private LDAPUser user;
    private String email;
    private String mailFrom;
    private String mailSubject;
    private String mailBody;
    
    public String getEmail() {
        return email;
    }

    @RequiredStringValidator(message = "Please enter your e-mail address.")
    @EmailValidator(message = "Please enter a valid e-mail address.")    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
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
    
    @Override
    public String execute() throws Exception {
        String code= RandomStringUtils.randomAlphanumeric(12);
        if(user==null){
            user= LDAPUtils.findUserByMail(email);
        }
        ActionContext.getContext().getSession().put("passResetCode", code);
        ActionContext.getContext().getSession().put("user", user);
        try{
            sendMail(code);
        } catch(MailException me){
            _log.error(me);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public void validate() {
        if(email!=null){
            if(getFieldErrors().isEmpty() && !LDAPUtils.isMailUsed(email)){
                addFieldError("email", "No Users associated with the provided e-mail");
                return;
            }

            if(getFieldErrors().isEmpty()){
                user= LDAPUtils.findUserByMail(email);
                if(!user.getPreferredMail().equals(email)){
                    addFieldError("email", "The provided e-mail is not the principal address");

                }

            }
        }
    }
    
    
    
    private void sendMail(String code) throws MailException{
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
            String title= user.getTitle()==null? "" : user.getTitle();
            
            mailMsg.setFrom(new InternetAddress(mailFrom));
            
            InternetAddress mailTo[] = new InternetAddress[1];
            mailTo[0] = new InternetAddress(user.getPreferredMail(), title + user.getGivenname()+" "+user.getSurname());
            mailMsg.setRecipients(Message.RecipientType.TO, mailTo);
            
            
            mailMsg.setSubject(mailSubject);
            
            mailBody= mailBody.replaceAll("_USER_", title+" "+user.getGivenname()+" "+user.getSurname());
            mailBody= mailBody.replaceAll("_CODE_", code);
            
            mailMsg.setText(mailBody);
            
            Transport.send(mailMsg);
            
            
        } catch (UnsupportedEncodingException ex) {
            _log.error(ex);
            throw new MailException("Mail from address format not valid");
        } catch (MessagingException ex) {
            _log.error(ex);
            throw new MailException("Mail message has problems");
        }
        
    }    
    
}
