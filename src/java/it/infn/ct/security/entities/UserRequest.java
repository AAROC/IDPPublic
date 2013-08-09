package it.infn.ct.security.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class UserRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String givenname;
    private String surname;
    private String title;
    private String organization;
    private String organizationDN;
    private String organizationUnitDN;
    private String password;
    private String preferredMail;
    private String additionalMails;
    private String country;
    private String address;
    private String phone;
    private String description;
    private String userHash;
    private Date creationTime;
    private boolean valid;
    private Long id;
    

    public UserRequest() {
    }

    public UserRequest(String username, String givenname, 
            String surname, String title, String organization, 
            String password, String officialMail, String mails, String country, String address, String phone, String description) {
        
        this.username = username;
        this.givenname = givenname;
        this.surname = surname;
        this.title = title;
        this.organization = organization;
        this.password = password;
        this.preferredMail = officialMail;
        this.additionalMails = mails;
        this.country = country;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.creationTime = new Date();
        this.valid = false;
        
        userHash = UUID.randomUUID().toString();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAdditionalMails() {
        return additionalMails;
    }

    public void setAdditionalMails(String additionalMails) {
        this.additionalMails = additionalMails;
    }

    public String getPreferredMail() {
        return preferredMail;
    }

    public void setPreferredMail(String preferredMail) {
        this.preferredMail = preferredMail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getOrganizationDN() {
        if(organizationDN!=null && !organizationDN.isEmpty())
            return organizationDN;
        else
            return description+" ("+organization+")";
    }

    public void setOrganizationDN(String organizationDN) {
        this.organizationDN = organizationDN;
    }

    public String getOrganizationUnitDN() {
        return organizationUnitDN;
    }

    public void setOrganizationUnitDN(String organizationUnitDN) {
        this.organizationUnitDN = organizationUnitDN;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserRequest)) {
            return false;
        }
        UserRequest other = (UserRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.infn.ct.security.entities.Usersrequests[ id=" + id + " ]";
    }
    
}
