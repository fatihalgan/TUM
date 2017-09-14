/*
 * ObjectDataProvider.java
 * 
 * Created on Aug 1, 2007, 6:24:07 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.view.providers;
import java.io.Serializable;
import mcel.tump.util.view.beans.SelectableDataItem;

/**
 *
 * @author db2admin
 */
public class ObjectDataProvider<E extends SelectableDataItem> implements Serializable {

    private E item;

    public E getItem() {
        return item;
    }

    public void setItem(E item) {
        this.item = item;
    }
    
    public ObjectDataProvider() {
        super();
    }
    
    public ObjectDataProvider(E item)  {
        super();
        this.item = item;
    }
}
