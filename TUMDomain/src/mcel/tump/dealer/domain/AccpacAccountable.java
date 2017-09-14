/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

import mcel.tump.util.value.ContactPerson;
import mcel.tump.util.value.NotificationInfo;

/**
 *
 * @author db2admin
 */
public abstract class AccpacAccountable extends AbstractDealer implements Notifiable {
    
    protected ContactPerson contactPerson = new ContactPerson();
    protected NotificationInfo notificationInfo = new NotificationInfo();

    public AccpacAccountable() {
        super();
    }

    public ContactPerson getContactPerson() {
        if(contactPerson == null) contactPerson = new ContactPerson();
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public NotificationInfo getNotificationInfo() {
        if(notificationInfo == null) notificationInfo = new NotificationInfo();
        return notificationInfo;
    }

    public void setNotificationInfo(NotificationInfo notificationInfo) {
        this.notificationInfo = notificationInfo;
    }

    public abstract String getAccpacCode();

}
