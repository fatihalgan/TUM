/*
 * MonetaryAmountUserType.java
 *
 * Created on 03 Subat 2007 Cumartesi, 23:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import mcel.tump.util.value.Money;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Hibernate;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author db2admin
 */
public class MonetaryAmountUserType implements CompositeUserType {
    
    public int[] sqlTypes() {
        return new int[] {Hibernate.BIG_DECIMAL.sqlType()}; 
    }
    
    public Class returnedClass() {return Money.class;}
    
    public boolean isMutable() {return false;}
    
    public Object deepCopy(Object value) {return value;}
    
    public Serializable disassemble(Object value, SessionImplementor session) {
        return (Serializable)value;
    }
    
    public Object assemble(Serializable cached, SessionImplementor session, Object owner) {
        return cached;
    }
    
    public Object replace(Object original, Object target, SessionImplementor session, Object owner) {
        return original;
    }
    
    public boolean equals(Object x, Object y) {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);
    }

    public int hashCode(Object x) {
        return x.hashCode();
    }
    
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session,
        Object owner) throws SQLException {

        BigDecimal value = resultSet.getBigDecimal( names[0] );
        //String cur = resultSet.getString( names[0] ); 
        if (resultSet.wasNull()) return null;
        return new Money(value);
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index,
        SessionImplementor session) throws SQLException {

        if (value==null) {
            statement.setNull(index+1, Hibernate.BIG_DECIMAL.sqlType());
        } else {
            Money amount = (Money) value;
            //String currencyCode = amount.getCurrencySymbol();
            //statement.setString( index, currencyCode );
            statement.setBigDecimal( index, amount.getAmount());
        }
    }
    
    public String[] getPropertyNames() {
	return new String[] {"amount" };
    }

    public Type[] getPropertyTypes() {
	return new Type[] { Hibernate.BIG_DECIMAL };
    }

    public Object getPropertyValue(Object component, int property) {
        Money monetaryAmount = (Money) component;
        if (property == 0)
            //return monetaryAmount.getCurrencySymbol();
        //else
            return monetaryAmount.getAmount();
        return null;
    }
    
    public void setPropertyValue(Object component, int property, Object value) {
        throw new UnsupportedOperationException("Money is immutable");
    }
}
