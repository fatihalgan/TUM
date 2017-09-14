package mcel.tump.util.value;

import java.io.Serializable;

public class NotificationInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3692875770041412611L;
    private String msisdn1;
    private String msisdn2;
    private String msisdn3;

    private String email1;
    private String email2;
    private String email3;

    public String getMsisdn1() {
        return msisdn1;
    }

    public void setMsisdn1(String msisdn1) {
        this.msisdn1 = msisdn1;
    }

    public String getMsisdn2() {
        return msisdn2;
    }

    public void setMsisdn2(String msisdn2) {
        this.msisdn2 = msisdn2;
    }

    public String getMsisdn3() {
        return msisdn3;
    }

    public void setMsisdn3(String msisdn3) {
        this.msisdn3 = msisdn3;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String toString() {
        return " MSISDN 1:" + getMsisdn1() + " MSISDN 2:" + getMsisdn2() + " MSISDN 3:" + getMsisdn3() + " E-Mail 1:" + getEmail1() + " E-Mail 2:" + getEmail2() + " E-Mail 3:" + getEmail3();
    }
}
