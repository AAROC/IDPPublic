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
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class ModifyUser extends ActionSupport{
    private String password;
    private String newPass;
    private String newPassAgain;
    private String newMail;
    private String addMail;
    
    @Override
    public String execute() throws Exception {
        boolean upDone = false;
        if(ActionContext.getContext().getSession().containsKey("ldapUser")){
            LDAPUser user = (LDAPUser) ActionContext.getContext().getSession().get("ldapUser");
            try{
                if(newPass!=null)
                    upDone = LDAPUtils.updatePassword(user,newPass);

                if(newMail!=null)
                    upDone = LDAPUtils.addMail(user,newMail);
            }
            catch(Exception ex){
                return ERROR;
            }
            if(upDone)
                return SUCCESS;
            else
                return ERROR;
        }
        else{
            return ERROR;
        }
    }

    @Override
    public void validate() {
        if(password!=null && !password.isEmpty() && (newPass==null || newPass.isEmpty())){
            addFieldError("newPass", "Password field is empty");
        }
        
        if(addMail!=null && !addMail.isEmpty() && (newMail==null || newMail.isEmpty())){
            addFieldError("newMail", "Mail field is empty");
        }
        
        if(newPass!=null && !newPass.isEmpty() && !newPass.equals(newPassAgain))
            addFieldError("newPass", "Passwords are different");
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPassAgain() {
        return newPassAgain;
    }

    public void setNewPassAgain(String newPassAgain) {
        this.newPassAgain = newPassAgain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddMail() {
        return addMail;
    }

    public void setAddMail(String addMail) {
        this.addMail = addMail;
    }

    public String getNewMail() {
        return newMail;
    }

    public void setNewMail(String newMail) {
        this.newMail = newMail;
    }
    
    

}
