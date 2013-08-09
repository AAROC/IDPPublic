package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionSupport;
import it.infn.ct.security.entities.UserRequest;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AdminAction extends ActionSupport{
    private List<UserRequest> users=new ArrayList();
    
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
    
    
}
