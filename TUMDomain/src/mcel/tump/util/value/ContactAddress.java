package mcel.tump.util.value;

import java.io.Serializable;
import mcel.tump.reference.domain.City;


public class ContactAddress implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -317032794551697569L;
    private City city = new City();
    private String street;
    private String zipCode;
    private String phone;
    private String gsm;
    private String fax;
    private String email;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String toString() {
        return "City: " + getCity().getName() +
              " Street: " + getStreet() +
              " ZipCode: " + getZipCode() +
              " Phone: " + getPhone() +
              " Gsm: " + getGsm() +
              " Fax: " + getFax() +
              " Email: " + getEmail() + 
              " Address: " + getAddress();
    }
}
