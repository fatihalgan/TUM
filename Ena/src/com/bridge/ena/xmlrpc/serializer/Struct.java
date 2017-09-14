package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.NonterminalNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

public class Struct implements NonterminalNode {

    private List<Member> members = new ArrayList<Member>();

    public Struct(Element element) {
        parseChildren(element);
    }

    public Struct() {
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) {
        String key = member.getName();
        Member mem = getMember(key);
        if(mem != null) getMembers().remove(mem);
        getMembers().add(member);
    }
    
    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            addMember((Member) TypeFactory.getNode(child));
        }
    }

    @Override
    public String toXML(StringBuffer buf) {
        buf.append("<struct>");
        for (Member member : members) {
            buf.append(member.toXML(new StringBuffer()));
        }
        buf.append("</struct>");
        return buf.toString();
    }
    
    public Object getValue(String key) {
        Iterator<Member> it = members.iterator();
        if(key.indexOf(".") != -1) throw new RuntimeException("Bad XML-RPM format: This method does not support cascaded parameters.");
        while(it.hasNext()) {
            Member member = it.next();
            if(member.getName().equals(key)) return member.getValue();
        }
        return null;
    }
    
    public Member getMember(String key) {
        Iterator<Member> it = members.iterator();
        while(it.hasNext()) {
            Member mem = it.next();
            if(mem.getName().equals(key)) return mem;
        }
        return null;
    }
    
    public Struct getValue(String key, Object value) {
        Iterator<Member> it = members.iterator();
        String prefix = key.substring(0, key.indexOf("."));
        String suffix = key.substring(key.indexOf(".") + 1, key.length());
        while(it.hasNext()) {
            Member member = it.next();
            if(member.getName().equals(prefix) && (member.getValue() instanceof Array)) {
                Array array = (Array)member.getValue();
                return array.getValue(suffix, value);
            } if(member.getName().equals(prefix) && (!(member.getValue() instanceof Array))) {
                throw new RuntimeException("Array is required here.");
            }
        }
        return null;
    }
}
