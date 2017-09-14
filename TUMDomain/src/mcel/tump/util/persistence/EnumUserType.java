/*
 * EnumUserType.java
 *
 * Created on February 4, 2007, 11:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.usertype.UserType;
import org.hibernate.HibernateException;

/**
 *
 * @author db2admin
 */
public class EnumUserType<E extends Enum<E>> implements UserType {

    private Class<E> clazz = null;
    private static final int[] SQL_TYPES = {Types.VARCHAR};
    
    protected EnumUserType(Class<E> c) {
        this.clazz = c;
    }
    
    public int[] sqlTypes() {
        return SQL_TYPES;
    }
    
    public Class<E> returnedClass() { 
        return clazz; 
    } 
    
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) 
        throws HibernateException, SQLException {
        String name = resultSet.getString(names[0]);
        E result = null;
        if(!resultSet.wasNull()) {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }
    
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) 
        throws HibernateException, SQLException {
        if(value == null) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, ((Enum)value).name());
        }
    }
    
    public Object deepCopy(Object value) throws HibernateException { 
        return value; 
    } 
 
    public boolean isMutable() { 
        return false; 
    } 
 
    public Object assemble(Serializable cached, Object owner) throws HibernateException {  
         return cached;
    } 

    public Serializable disassemble(Object value) throws HibernateException { 
        return (Serializable)value; 
    } 
 
    public Object replace(Object original, Object target, Object owner) throws HibernateException { 
        return original; 
    } 
    
    public int hashCode(Object x) throws HibernateException { 
        return x.hashCode(); 
    } 
    
    public boolean equals(Object x, Object y) throws HibernateException { 
        if (x == y) 
            return true; 
        if (null == x || null == y) 
            return false; 
        return x.equals(y); 
    } 
}
