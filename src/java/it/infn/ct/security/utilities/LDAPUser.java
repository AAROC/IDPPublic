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

import it.infn.ct.security.entities.UserRequest;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class LDAPUser extends UserRequest{
 
    private ArrayList<String> groups;
    private boolean administrator;

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        ResourceBundle rb = ResourceBundle.getBundle("ldap");
        this.groups = groups;
        if(groups.contains(rb.getString("administratorGroup")))
            administrator=true;
    }
    
    public void addGroup(String group){
        ResourceBundle rb = ResourceBundle.getBundle("ldap");
        if(groups==null)
            groups = new ArrayList<String>();
                
        groups.add(group);
        if(group.equals(rb.getString("administratorGroup")))
            administrator=true;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
    


}
