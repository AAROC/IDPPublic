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

package it.infn.ct.security.utilities;

import it.infn.ct.security.actions.MailException;
import it.infn.ct.security.entities.UserConfirmUpdate;
import it.infn.ct.security.entities.UserRequest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class LDAPCleaner implements Runnable{
    private Log _log= LogFactory.getLog(LDAPCleaner.class);
    private SessionFactory factory;
    private int cycle[];
    private ResourceBundle rb;
    
    public LDAPCleaner(SessionFactory factory) {
        rb= ResourceBundle.getBundle("cleaner");

        this.factory = factory;
        
        try{
            String strCycles[]= rb.getString("NotifyCycle").split(",");
            cycle= new int[strCycles.length];
            
            for(int i=0; i<strCycles.length; i++){
                cycle[i]=Integer.parseInt(strCycles[i]);
            }
        }
        catch(MissingResourceException mre){
            cycle= new int[3];
            cycle[0]=20;
            cycle[1]=10;
            cycle[2]=5;
        }
    }
    
    public void run() {
        _log.info("Users cleaning cycle start");
        firstCheck(cycle[0]);
        for(int i=1; i<cycle.length; i++){
            followingChecks(cycle[i]);
        }
        stopUsers();
        _log.info("Users cleaning cycle stop");
    }
    
    private void firstCheck(int days){
        _log.info("Search users starting the account extension cycle");
        List<LDAPUser> userLst= LDAPUtils.getIdPUserList();
        
        for(LDAPUser user: userLst){
            Calendar cal= GregorianCalendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            Calendar userCreation= GregorianCalendar.getInstance();
            userCreation.setTime(user.getCreationTime());

            if(cal.get(Calendar.DAY_OF_YEAR)==userCreation.get(Calendar.DAY_OF_YEAR)){
                _log.info("Update account procedure for the user "+user.getGivenname()+" "+user.getSurname()+"("+user.getUsername()+") started");
                
                UserConfirmUpdate ucu= new UserConfirmUpdate(user.getUsername(),cal.getTime(),false);
                
                Session ses= factory.openSession();
                ses.beginTransaction();
                if(ses.createCriteria(UserConfirmUpdate.class)
                        .add(Restrictions.eq("username", user.getUsername()))
                        .add(Restrictions.eq("updated", Boolean.FALSE))
                        .list()
                        .isEmpty())
                {
                    ses.save(ucu);
                }
                ses.getTransaction().commit();
                ses.close();

                sendUserRemainder(user, days);
                
            }
        }
        
    }
    private void followingChecks(int days){
        _log.info("Check users who have not extended yet");
        Calendar cal= GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Calendar calEnd= GregorianCalendar.getInstance();
        calEnd.setTime(cal.getTime());
        calEnd.add(Calendar.DAY_OF_YEAR, 1);
        
        Session ses= factory.openSession();
        
        
        List lstUserUpdates= ses.createCriteria(UserConfirmUpdate.class)
                .add(Restrictions.between("timelimit", cal.getTime(), calEnd.getTime()))
                .add(Restrictions.eq("updated", Boolean.FALSE))
                .list();
        
        for(UserConfirmUpdate ucu: (List<UserConfirmUpdate>) lstUserUpdates){
            UserRequest ur= LDAPUtils.getUser(ucu.getUsername());
            
            sendUserRemainder(ur, days);
        }
        
        ses.close();
        
    }
    
    private void stopUsers(){
        _log.info("Disable users who do not confirm the account extension");
        Session session = factory.openSession();
        session.beginTransaction();

        Calendar cal= GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Calendar calEnd= GregorianCalendar.getInstance();
        calEnd.setTime(cal.getTime());
        calEnd.add(Calendar.DAY_OF_YEAR, 1);
        
        Session ses= factory.openSession();
        
        List lstUserUpdates= ses.createCriteria(UserConfirmUpdate.class)
                .add(Restrictions.between("timelimit", cal.getTime(), calEnd.getTime()))
                .add(Restrictions.eq("updated", Boolean.FALSE))
                .list();
        
        for(UserConfirmUpdate ucu: (List<UserConfirmUpdate>) lstUserUpdates){
            UserRequest ur= LDAPUtils.getUser(ucu.getUsername());
            LDAPUtils.disableUser(ucu.getUsername());
            sendUserRemainder(ur, 0);
        }
        
        ses.close();
        
        

        session.getTransaction().commit();
        session.close();
        
    }
    
    private void sendUserRemainder(UserRequest ur, int days){
        javax.mail.Session session=null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            session = (javax.mail.Session) envCtx.lookup("mail/Users");

        
            Message mailMsg = new MimeMessage(session);
            mailMsg.setFrom(new InternetAddress(rb.getString("mailSender"),rb.getString("IdPAdmin")));
            
            InternetAddress mailTo[] = new InternetAddress[1];
            mailTo[0] = new InternetAddress(getGoodMail(ur), ur.getTitle()+" "+ur.getGivenname()+" "+ur.getSurname());
            mailMsg.setRecipients(Message.RecipientType.TO, mailTo);
            
            if(rb.containsKey("mailCopy") && !rb.getString("mailCopy").isEmpty()){
    
                String ccMail[] = rb.getString("mailCopy").split(";");
                InternetAddress mailCCopy[] = new InternetAddress[ccMail.length];
                for(int i=0; i<ccMail.length;i++) {
                    mailCCopy[i] = new InternetAddress(ccMail[i]);
                }

                mailMsg.setRecipients(Message.RecipientType.CC, mailCCopy);
            }
            
            if(days>0){
                mailMsg.setSubject(rb.getString("mailNotificationSubject").replace("_DAYS_", Integer.toString(days)));
                mailMsg.setText(rb.getString("mailNotificationBody").replaceAll("_USER_", ur.getTitle()+" "+ur.getGivenname()+" "+ur.getSurname()).replace("_DAYS_", Integer.toString(days)));
            }
            else{
                mailMsg.setSubject(rb.getString("mailDeleteSubject").replace("_DAYS_", Integer.toString(days)));
                mailMsg.setText(rb.getString("mailDeleteBody").replaceAll("_USER_", ur.getTitle()+" "+ur.getGivenname()+" "+ur.getSurname()).replace("_DAYS_", Integer.toString(days)));                
            }
            Transport.send(mailMsg);
         
        } catch (Exception ex) {
            _log.error("Mail resource lookup error");
            _log.error(ex.getMessage());
        }
    }
    
    private String getGoodMail(UserRequest ur) throws MailException{
        String mail= ur.getPreferredMail();
        if(rb.containsKey("mailFilter") && !rb.getString("mailFilter").isEmpty()){
            for(String filter: rb.getString("mailFilter").split(",")){
                if(mail.contains(filter)){
                    throw new MailException("Preferred mail from filtered provider");
                }

            }
        }
        
        return mail;
    }
}
