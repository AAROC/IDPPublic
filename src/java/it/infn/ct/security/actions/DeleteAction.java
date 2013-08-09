package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionSupport;
import it.infn.ct.security.entities.UserRequest;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DeleteAction extends ActionSupport{
    private String id;
    
    @Override
    public String execute() throws Exception {
        SessionFactory factory =new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM UserRequest u WHERE u.id = :id");
        query.setString("id",id);
        int r=query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        //return super.execute();
        return "success";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
