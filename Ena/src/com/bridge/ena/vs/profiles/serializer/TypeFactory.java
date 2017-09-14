/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.profiles.serializer;

import com.bridge.ena.vs.profiles.*;
import com.bridge.ena.xml.Node;
import org.dom4j.Element;



/**
 *
 * @author db2admin
 */
public class TypeFactory {

    public static Node getNode(Element element) {
        if(element.getName().equals("VoucherProfiles")) {
            return new VoucherProfiles(element);
        }
        if(element.getName().equals("VoucherProfile")) {
            return new VoucherProfile(element);
        }
        return null;
    }

    public static boolean isTerminalNode(Element element) {
        if(element.getName().equals("VoucherProfiles")) return false;
        if(element.getName().equals("VocuherProfile")) return true;
        return false;
    }
}
