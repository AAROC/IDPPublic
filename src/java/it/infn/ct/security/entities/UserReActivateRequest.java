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

package it.infn.ct.security.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Marco Fargetta <marco.fargetta@ct.infn.it>
 */
public class UserReActivateRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    private long id;
    private String username;
    private Date applyDate;
    private String handledBy;
    private boolean open;
    private boolean approved;

    public UserReActivateRequest() {
        this.applyDate= new Date();
        this.open= true;
        this.approved= false;
    }
    
    public UserReActivateRequest(String username) {
        this.username = username;
        this.applyDate= new Date();
        this.open= true;
        this.approved= false;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
}
