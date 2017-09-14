package com.bridge.ena.xmlrpc.serializer;
import com.bridge.ena.xml.NonterminalNode;
import java.util.Date;
import java.util.Iterator;
import com.bridge.ena.util.DateUtils;
import java.sql.Timestamp;
import org.dom4j.Element;

public class Member implements NonterminalNode {

    private String name;
    private Object value;

    public Member() {
    }

    public Member(Element element) {
        parseChildren(element);
    }

    public Member(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            if (child.getName().equals("name")) {
                setName(child.getText().trim());
            } else if (TypeFactory.isIntrinsicType(child)) {
                parseChildren(child);
            } else if (TypeFactory.isTerminalNode(child)) {
                value = TypeFactory.getTerminalValue(child);
            } else {
                value = TypeFactory.getNode(child);
            }
        }
    }

    @Override
    public String toXML(StringBuffer buf) {
        if(value == null) return buf.toString();
        buf.append("<member>");
        buf.append("<name>");
        buf.append(getName());
        buf.append("</name>");
        buf.append("<value>");
        if (value instanceof Array) {
            buf.append(((Array) value).toXML(new StringBuffer()));
        } else if(value instanceof Struct) {
            buf.append(((Struct)value).toXML(new StringBuffer()));
        } else {
            buf.append(writePrimitive());
        }
        buf.append("</value>");
        buf.append("</member>");
        return buf.toString();
    }

    public String writePrimitive() {
        Class clazz = value.getClass();
        if (clazz.equals(String.class)) {
            return "<string>" + (String) value + "</string>";
        }
        if (clazz.equals(Integer.class)) {
            return "<i4>" + (Integer) value + "</i4>";
        }
        if (clazz.equals(Date.class) || clazz.equals(Timestamp.class) || clazz.equals(java.sql.Date.class)) {
            return "<dateTime.iso8601>" + DateUtils.convertISO8601String((Date) value) + "</dateTime.iso8601>";
        }
        if(clazz.isArray()) {
            return "<base64>" + new String((byte[])value)  + "</base64>";
        }
        if(clazz.equals(Boolean.class)) {
            return "<boolean>" + ((Boolean)value ? 1 : 0) + "</boolean>";
        }
        return null;
    }
}
