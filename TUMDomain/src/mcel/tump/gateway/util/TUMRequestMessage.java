/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

import java.util.Date;

/**
 *
 * @author db2admin
 */
public interface TUMRequestMessage {
    public String getMethodName();
    public String getUsername();
    public void setUsername(String username);
    public String getPassword();
    public void setPassword(String password);
    public void setRequestTransactionID(String requestTransactionID);
    public String getSignature();
    public void setSignature(String signature);
    public Date getRequestTimeStamp();
    public void setRequestTimeStamp(Date requestTimeStamp);
    public String getRequestDealerID();
    public void setRequestDealerID(String requestDealerID);
    public String getRequestSubDealerID();
    public void setRequestSubDealerID(String requestSubDealerID);
    public String toXML();
}
