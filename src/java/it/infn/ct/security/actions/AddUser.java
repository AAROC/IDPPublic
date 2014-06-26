package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.Parameterizable;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import it.infn.ct.security.entities.UserRequest;
import it.infn.ct.security.utilities.LDAPUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class AddUser extends ActionSupport  implements Parameterizable, ServletRequestAware{
    Log log = LogFactory.getLog(AddUser.class);
    
    private Map<String, String> actionParams;
    private String mailFrom;
//    private String mailCC;
    private String mailSubject;
    private String mailBody;
    private String idPAdmin;
    
    private UserRequest usreq;
    
    private String username;
    private String givenname;
    private String password;
    private String password2;
    private String surname;
    private String title;
    private String organization;
    private String registeredMail;
    private String otherMails;
    private String captcha;
    private String country;
    private String address;
    private String phone;
    private String orgname;
    private String description;
    
    private HttpServletRequest httpServReq;
    

    @Override
    public String execute() throws Exception {
        if("Other:".equals(organization)){
            organization=orgname;
        }
        usreq = new UserRequest(username, givenname, surname, title, organization,password, registeredMail, otherMails, country, address, phone, description);
        
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(usreq);
        session.getTransaction().commit();
        session.close();
        
        sendMail(usreq);
        
        return super.execute();
    }
        
    @Override
    public void validate(){
        
        if(username.length() < 1){
            addFieldError("username","Username field is empty");
        }
        else{
            if(!username.matches("^[0-9a-zA-Z]*$"))
                addFieldError("username","Username format invalid");
            else
                if(LDAPUtils.isCNregistered(username))
                    addFieldError("username","Username already assigned");
        }
        
        if(givenname.length() < 1){
            addFieldError("givenname","Given Name field is empty");
        }
        if(surname.length() < 1){
            addFieldError("surname","Surname field is empty");
        }
        
        //TODO: FieldError nelle select
        if(country==null || country.equals("-1")){
            addFieldError("country","Select your country");
        }
        if(organization==null || organization.equals("-1") || (organization.equals("Other:") && (orgname==null || orgname.isEmpty()))){
            addFieldError("organization","Select your organization");
        }
        try{
            if(captcha.length() < 1 || Integer.parseInt(captcha) != CaptchaAction.getRightId()){
                addFieldError("captcha", "Wrong captcha");
            }
        }
        catch(Exception e){
            addFieldError("captcha", "Wrong Captcha");
        }
        if(password.length() < 8){
            addFieldError("password","Minimum password length is 8 characters");
        }
        if(!password.equals(password2)){
           addFieldError("password2","Passwords are different");
        }
        if(registeredMail.length() < 1){
            addFieldError("registeredMail","Email Addresses field is empty");
        }
        else{
            if(registeredMail.indexOf(" ") != -1 ){
                addFieldError("registeredMail","Email addresses contain spaces");
            }
            else{
                if(LDAPUtils.isMailUsed(registeredMail)){   
                    addFieldError("registeredMail","Email already registered");
                }
                SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
                Session session = factory.openSession();
                session.beginTransaction();
                if(!session.createCriteria(UserRequest.class)
                        .add(Restrictions.eq("preferredMail", registeredMail))
                        .add(Restrictions.eq("valid", Boolean.TRUE))
                        .list()
                        .isEmpty()
                        ){
                    
                    addFieldError("registeredMail","A pending request already issued with this Email");
                }
                session.getTransaction().commit();
                session.close();

                
            }
        }

        if(otherMails.length() > 0){
            String[] mails = otherMails.split("[,\\s;]");
            for(String oMail: otherMails.split("[,\\s;]")){
                if(LDAPUtils.isMailUsed(oMail))
                addFieldError("otherMails","Email already registered");
            }

        }

    }
    
     public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtherMails() {
        return otherMails;
    }

    public void setOtherMails(String otherMails) {
        this.otherMails = otherMails;
    }

    public String getRegisteredMail() {
        return registeredMail;
    }

    @EmailValidator(message = "Valid email is requested")
    public void setRegisteredMail(String registeredMail) {
        this.registeredMail = registeredMail;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
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

//    public String getMailCC() {
//        return mailCC;
//    }
//
//    public void setMailCC(String mailCC) {
//        this.mailCC = mailCC;
//    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getIdPAdmin() {
        return idPAdmin;
    }

    public void setIdPAdmin(String idPAdmin) {
        this.idPAdmin = idPAdmin;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
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
            mailTo[0] = new InternetAddress(registeredMail, title+" "+givenname+" "+surname);
            mailMsg.setRecipients(Message.RecipientType.TO, mailTo);
            
//            String ccMail[] = mailCC.split(";");
//            InternetAddress mailCCopy[] = new InternetAddress[ccMail.length];
//            for(int i=0; i<ccMail.length;i++)
//                mailCCopy[i] = new InternetAddress(ccMail[i]);

//            mailMsg.setRecipients(Message.RecipientType.CC, mailCCopy);
            
            mailMsg.setSubject(mailSubject);
            
            
            mailBody = mailBody.replaceAll("_USER_", title+" "+givenname+" "+surname);
            
            mailBody = mailBody.replaceAll(
                    "_URL_",
                    "https://"+httpServReq.getServerName()+"/confirmRegistration.action?uid="+usreq.getUserHash());
            
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

    public void setServletRequest(HttpServletRequest hsr) {
        this.httpServReq = hsr;
    }
    
}
