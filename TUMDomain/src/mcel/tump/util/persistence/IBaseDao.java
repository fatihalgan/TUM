package mcel.tump.util.persistence;

import java.io.Serializable;

import java.util.List;

public interface IBaseDao {
    public void saveOrUpdate(Object obj);
    public void delete(Object obj);
    public List FindByExample(Object o);
    public void attach(Object o);
    public void attachForUpdate(Object o);
    public void update(Object o);
    public Object merge(Object o);
    public void clearSession();
    public void initialize(Object o);
    public Object load(Class clazz, Serializable id);
    public Object get(Class clazz, Serializable id);
    public void flush();
    public void save(Object o);
}
