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
public interface Notifiable {

    public NotificationInfo getNotificationInfo();
    public void setNotificationInfo(NotificationInfo notificationInfo);
    public String getDealerCode();
    public String getDealerName();
    public ContactPerson getContactPerson();
    public void setContactPerson(ContactPerson person);
}
