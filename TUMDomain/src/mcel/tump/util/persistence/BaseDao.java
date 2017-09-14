package mcel.tump.util.persistence;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BaseDao extends HibernateDaoSupport implements IBaseDao {

    public void saveOrUpdate(Object obj) {
        getHibernateTemplate().saveOrUpdate(obj);
    }

    public void delete(Object obj) {
        getHibernateTemplate().delete(obj);
    }

    public List FindByExample(Object o) {
        return getHibernateTemplate().findByExample(o);
    }

    public void attach(Object o) {
        getHibernateTemplate().lock(o, LockMode.READ);
    }
    
    public void attachForUpdate(Object o) {
    	getHibernateTemplate().lock(o, LockMode.UPGRADE_NOWAIT);
    }
    
    public void update(Object o) {
        getHibernateTemplate().update(o);
    }
    
    public Object merge(Object o) {
       return getHibernateTemplate().merge(o);
    }
    
    private Serializable getIdentifier(Object o) {
        Class clazz = o.getClass();
        Method method;
        try {
            method = clazz.getMethod("getId", new Class[] {});
            return (Serializable)method.invoke(o, new Object[] {});
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } 
    }

    public void clearSession() {
        getHibernateTemplate().clear();
    }

    public void initialize(Object o) {
        getHibernateTemplate().initialize(o);
    }

    public Object load(Class clazz, Serializable id) {
        return getHibernateTemplate().load(clazz, id);
    }
    
    public Object get(Class clazz, Serializable id) {
        return getHibernateTemplate().get(clazz, id);
    }
    
    public void flush() {
        getHibernateTemplate().flush();
    }
    
    public void save(Object o) {
        getHibernateTemplate().save(o);
    }
}
