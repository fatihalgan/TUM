package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.Node;
import com.bridge.ena.util.DateUtils;
import org.dom4j.Element;

public class TypeFactory {

    public static Node getNode(Element element) {
        if (element.getName().equals("methodCall")) {
            return new MethodCall(element);
        }
        if (element.getName().equals("methodResponse")) {
            return new MethodResponse(element);
        }
        if (element.getName().equals("fault")) {
            return new Fault(element);
        }
        if (element.getName().equals("struct")) {
            return new Struct(element);
        }
        if (element.getName().equals("member")) {
            return new Member(element);
        }
        if (element.getName().equals("array")) {
            return new Array(element);
        }
        return null;
    }

    public static boolean isIntrinsicType(Element element) {
        if (element.getName().equals("params")) {
            return true;
        }
        if (element.getName().equals("param")) {
            return true;
        }
        if (element.getName().equals("value")) {
            return true;
        }
        return false;
    }

    public static boolean isTerminalNode(Element element) {
        if (element.getName().equals("name")) {
            return true;
        }
        if (element.getName().equals("methodName")) {
            return true;
        }
        if (element.getName().equals("string")) {
            return true;
        }
        if (element.getName().equals("int") || element.getName().equals("i4")) {
            return true;
        }
        if (element.getName().equals("dateTime.iso8601")) {
            return true;
        }
        if(element.getName().equals("base64")) {
            return true;
        }
        if(element.getName().equals("boolean")) {
            return true;
        }
        return false;
    }

    public static Object getTerminalValue(Element element) {
        if (element.getName().equals("string")) {
            return new String(element.getText());
        }
        if (element.getName().equals("int") || element.getName().equals("i4")) {
            return new Integer(element.getText().trim());
        }
        if (element.getName().equals("dateTime.iso8601")) {
            return DateUtils.convertISO8601DateExact(element.getText());
        }
        if(element.getName().equals("base64")) {
            return element.getText().getBytes();
        }
        if(element.getName().equals("boolean")) {
        	String val = element.getText().trim();
        	if(val == null) return false;
        	if(val.equals("1")) return new Boolean(true);
            if(val.equals("true")) return new Boolean(true);
        	return new Boolean(false);
        }
        return null;
    }
}
