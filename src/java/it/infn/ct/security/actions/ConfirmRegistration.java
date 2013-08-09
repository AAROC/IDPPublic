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
import it.infn.ct.security.entities.UserRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class ConfirmRegistration extends ActionSupport{

    private String uid;
    private String mailFrom;
    private String mailTo;

            
    @Override
    public String execute() throws Exception {
        String returnValue = ERROR;
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        
        Session session = factory.openSession();
        Query hbQuery = session.createQuery("from UserRequest as ur where ur.userHash= :uid ");
        hbQuery.setParameter("uid", uid);
        session.beginTransaction();
        UserRequest ur = (UserRequest) hbQuery.uniqueResult();
        if(ur!=null)
            if((new Date() ).getTime() - ur.getCreationTime().getTime() < 48*60*60*1000){
                ur.setValid(true);
                
                 sendMail(ur.getGivenname()+" "+ur.getSurname(),ur.getPreferredMail(),ur.getOrganization(),ur.getCountry());
                
                returnValue = SUCCESS;
            }
        session.getTransaction().commit();
        session.close();

        return returnValue;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
    private void sendMail(String name, String mail, String org, String country){
        javax.mail.Session session=null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            session = (javax.mail.Session) envCtx.lookup("mail/Users");

        } catch (Exception ex) {
        }
        
        Message mailMsg = new MimeMessage(session);
        try {
            mailMsg.setFrom(new InternetAddress(mailFrom));
            
            InternetAddress mailToUsr[] = new InternetAddress[1];
            mailToUsr[0] = new InternetAddress(mailTo);
            mailMsg.setRecipients(Message.RecipientType.TO, mailToUsr);

            mailMsg.setSubject("New user registration on IDPOPEN");
            
            mailMsg.setText("A new registration performed on IDPOPEN,\n\n"
                    + "the user "+name+" ("+mail+") from "+org+" ("+country+")\n\n"
                    + "is waiting for authorisation");
            
            Transport.send(mailMsg);
            
            
        } catch (MessagingException ex) {
        }

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
}
