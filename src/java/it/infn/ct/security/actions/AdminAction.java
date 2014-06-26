package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionSupport;
import it.infn.ct.security.entities.UserReActivateRequest;
import it.infn.ct.security.entities.UserRequest;
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class AdminAction extends ActionSupport{
    private List<UserRequest> users=new ArrayList<UserRequest>();
    private Map<String, LDAPUser> actvReqs=new HashMap<String, LDAPUser>();
    
    
    public AdminAction(){
    }
    
    @Override
    public String execute() throws Exception {
        return super.execute();
    }

    public List<UserRequest> getUsers() {
        if(users!=null && !users.isEmpty()){
            return users;
        }
        List<UserRequest> usersList;
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        Query query = session.createQuery("from UserRequest as ur where ur.valid = true ");
        usersList = query.list();
        session.close();
        return usersList;
    }

    public void setUsers(List<UserRequest> users) {
        this.users = users;
    }

    public Map<String, LDAPUser> getActvReqs() {
        if(actvReqs!=null && !actvReqs.isEmpty()){
            return actvReqs;
        }
        
        SessionFactory factory = (SessionFactory) ServletActionContext.getServletContext().getAttribute("IDPPublic.hibernatefactory");
        Session session = factory.openSession();
        List<UserReActivateRequest> activations= session.createCriteria(UserReActivateRequest.class)
                .add(Restrictions.eq("open", Boolean.TRUE))
                .list();
        session.close();

        if(actvReqs==null){
            actvReqs= new HashMap<String, LDAPUser>();
        }
        for(UserReActivateRequest act: activations){
            actvReqs.put(Long.toString(act.getId()),LDAPUtils.getUser(act.getUsername()));
        }
        
        return actvReqs;
    }

    public void setActvReqs(Map<String, LDAPUser> actvReqs) {
        this.actvReqs = actvReqs;
    }
    
}
