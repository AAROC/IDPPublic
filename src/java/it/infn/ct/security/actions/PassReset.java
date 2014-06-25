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
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import it.infn.ct.security.utilities.LDAPUser;
import it.infn.ct.security.utilities.LDAPUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class PassReset extends ActionSupport{
    private Log _log= LogFactory.getLog(PassReset.class);
    
    private String code;
    private String password;
    private String password2;

    public String getCode() {
        return code;
    }

    @RequiredFieldValidator(message = "Please enter the code")
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getPassword() {
        return password;
    }

    @StringLengthFieldValidator(message = "Minimum password lenght is 8 characters", minLength = "8")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    @Override
    public void validate() {
        if(!code.equals((String) ActionContext.getContext().getSession().get("passResetCode"))){
            addFieldError("code", "The code is not correct");
        }
        
        if(!password.equals(password2)){
            addFieldError("password", "Passwords do not match");
        }
    }

    @Override
    public String execute() throws Exception {
        LDAPUser user= (LDAPUser) ActionContext.getContext().getSession().get("user");
        if(LDAPUtils.resetPassword(user.getUsername(), password)){
            return SUCCESS;
        }
        else{
            return ERROR;
        }
        
    }
    
    
    
    
}
