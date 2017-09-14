/*
 * XMLMenuDefinition.java
 *
 * Created on November 20, 2006, 9:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.view.xmlmenu;

import java.net.URL;
import mcel.tump.util.ResourceLoader;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.Attributes;

/**
 *
 * @author db2admin
 */
/**
 * Represents a hierarhical tree of menu items.
 * 
 * Menu item structure is read from an XML file complying with the
 * Menus.xsd XML schema for the "http://dna.com/menus" namespace
 * using Apache Commons Digester.
 */
public class XMLMenuDefinition {
    
    private Digester digester;
    private XMLMenuModel model;
    private static XMLMenuDefinition instance = null;    
    
    public static XMLMenuDefinition instance() {
        if(instance == null) {
            instance = new XMLMenuDefinition();
        }
        return instance;
    }
    
    /** Creates a new instance of XMLMenuDefinition */
    private XMLMenuDefinition() {
        digester = new Digester();
        digester.addRuleSet(new MenuRuleSet());
        digester.setValidating(false);
        setMenuDefinitionFile();
    }
    
    /**
     * Get an XMLMenuModel for the menu definition.
     * @return XMLMenuModel for the menu definition
     * @throws IntrospectionException
     */
    public XMLMenuModel getModel() {
        if (model == null) {
            throw new RuntimeException("Menu Definition has not been set.");
        }
        return model;
    }
    
    /**
     * Set the name of the menu definition file.
     * @param menuDefinitionFile the name of the menu definition XML file
     * @throws IOException
     * @throws SAXException
     */
    public void setMenuDefinitionFile() {
        try {
            URL url = ResourceLoader.getResource("menus.xml");
            model = (XMLMenuModel)digester.parse(url.openStream());
        } catch(Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Menu Definition has not been set.." + e.getMessage());
        }
    }
    
    /**
     * Apache Digester rule set to construct a Menu containing
     * a tree of Items from XML.
     */
    class MenuRuleSet extends RuleSetBase {
        
        /**
         * Adds digester rule set specific to this XML menu definition format.
         * @param d Digester instance to apply the rules to
         */
        public void addRuleInstances(Digester d) {
            d.addFactoryCreate("menu", new MenuFactoryCreate());
            d.addObjectCreate("menu/item", MenuItem.class);
            d.addSetProperties("menu/item");
            d.addSetNext("menu/item", "addMenuItem");
            d.addObjectCreate("*/item", MenuItem.class);
            d.addSetProperties("*/item");
            d.addSetNext("*/item", "addMenuItem");
        }
    }
    
    /**
     * Used by the MenuRuleSet to construct an instance of the private MenuItem class.
     */
    class MenuFactoryCreate extends AbstractObjectCreationFactory {
        /**
         * Returns a new instance of the private Menu class.
         * @param attributes XML attributes of the current element.
         * @return new instance of the private Menu class
         */
        public Object createObject(Attributes attributes) {
            return new XMLMenuModel();
        }
    }
    
}
