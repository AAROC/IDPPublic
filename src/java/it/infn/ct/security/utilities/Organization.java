package it.infn.ct.security.utilities;

import java.io.Serializable;

public class Organization implements Serializable {

    private String key;
    private String country;
    private String countryCode;
    private String description;
    private String reference;
    private String dn;

    public Organization() {
    }

    public Organization(String key, String country, String countryCode, String description, String dn) {
        this.key= key;
        this.description= description;
        this.country= country;
        this.countryCode= countryCode;
        this.dn= dn;

    }
    public Organization(String key, String country, String countryCode, String description, String dn, String reference) {
        this.key= key;
        this.description= description;
        this.country= country;
        this.countryCode= countryCode;
        this.dn= dn;
        this.reference= reference;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Organization)) {
            return false;
        }
        Organization other = (Organization) object;
        if (this.key.equals(other.key)  && this.countryCode.equals(other.countryCode)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "it.infn.ct.security.entities.Organization[ key=" + key + " ]";
    }
}