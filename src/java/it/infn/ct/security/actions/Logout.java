package it.infn.ct.security.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;

public class Logout extends ActionSupport  {


    @Override
    public String execute() throws Exception {
        Map session = ActionContext.getContext().getSession();
        session.remove("login");
        session.remove("ldapUser");
        return super.execute();
    }
}