package mcel.tump.util.value;

import java.io.Serializable;

public class ContactPerson implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5722405478754896427L;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String toString() {
        return " FirstName: " + getFirstName() +
                " LastName: " + getLastName();
            
    }
}
