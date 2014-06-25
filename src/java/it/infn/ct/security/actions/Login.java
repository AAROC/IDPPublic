package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import it.infn.ct.security.entities.UserConfirmUpdate;
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class Login extends ActionSupport  {
    private String username;
    private String password;
    private String login;
    private LDAPUser user;
    private boolean expiring;
    
    @Override
    public String execute() throws Exception {
        if(ActionContext.getContext().getSession().containsKey("ldapUser"))
            user = (LDAPUser) ActionContext.getContext().getSession().get("ldapUser");
        else{
            if(user!=null){
                user.setUsername(username);
                user.setPassword(password);
            }
        }
        
        if((login==null || login.isEmpty()) && user == null)
                return "login";
        
        Map session = ActionContext.getContext().getSession();
        session.put("login","true");
        session.put("ldapUser", user);
        return "userAccess";
    }
    
    @Override
    public void validate(){
        if(ActionContext.getContext().getSession().containsKey("login"))
            return;

        if(login==null || login.isEmpty())
            return;
        if(username==null || username.length() < 1){
            addFieldError("username","Username field is empty");
        }
        else{
            user = LDAPUtils.getIfValidUser(username, password);
            if(user==null){
                addFieldError("username","Invalid username");
                addFieldError("password","Or Password is wrong");
            }
        }
    }
    
    public String adminRequest() throws Exception {
        LDAPUser user = (LDAPUser) ActionContext.getContext().getSession().get("ldapUser");
        if(user.isAdministrator()){
            return SUCCESS;
        }
        
        return ERROR;
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LDAPUser getUser() {
        return user;
    }

    public void setUser(LDAPUser user) {
        this.user = user;
    }

    public boolean isExpiring() {
        if(user==null){
            return false;
        }
        
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        List<UserConfirmUpdate> update= session.createCriteria(UserConfirmUpdate.class)
                .add(Restrictions.eq("username", user.getUsername()))
                .add(Restrictions.eq("updated", Boolean.FALSE))
                .add(Restrictions.gt("timelimit", new Date()))
                .list();
        session.close();
        if(update!=null && !update.isEmpty()){
            return true;
        }
        
        return false;
    }

    public void setExpiring(boolean expiring) {
        this.expiring = expiring;
    }
}
