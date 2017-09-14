/*
 * XMLMenuModel.java
 *
 * Created on November 20, 2006, 9:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.view.xmlmenu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author db2admin
 */
public class XMLMenuModel {
    
    private List<MenuItem> items = null;
    /** Creates a new instance of XMLMenuModel */
    public XMLMenuModel() {
        super();
    }
    
    public void addMenuItem(MenuItem item) {
         if (items == null) {
            items = new ArrayList<MenuItem>();
        }
        items.add(item);
    }
    
    public XMLMenuModel(List items) {
        this.setItems(items);
    }

    public List<MenuItem> getItems() {
        if(items == null || items.size() == 0)
            items = new ArrayList();
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("var MENU_ITEMS = [");
        for(MenuItem menu : items) {
            if(menu.isRendered())
                buf.append(menu.toString());
        }
        buf.append("];");
        return buf.toString();
    }
    
}
