/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class UserSalesRecord implements Serializable {
    
    private String edgeDealerId;
    private Integer amount;
    private String username;
    private Date date;
    
    public UserSalesRecord() {
        super();
    }

    public String getEdgeDealerId() {
        return edgeDealerId;
    }

    public void setEdgeDealerId(String edgeDealerId) {
        this.edgeDealerId = edgeDealerId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
