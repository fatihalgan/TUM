/*
 * ParametersDaoImpl.java
 * 
 * Created on Aug 6, 2007, 6:17:49 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.reference.dao;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import mcel.tump.reference.domain.BlacklistItem;
import mcel.tump.reference.domain.City;
import mcel.tump.reference.domain.Parameters;
import mcel.tump.reference.domain.SubscriberBenefit;
import mcel.tump.util.persistence.BaseDao;
import org.hibernate.LockMode;

/**
 *
 * @author db2admin
 */
public class ParametersDaoImpl extends BaseDao implements IParametersDao {

    public Parameters findParameters() {
        List<Parameters> params = getHibernateTemplate().find("from mcel.tump.reference.domain.Parameters as params");
        Iterator<Parameters> it = params.iterator();
        if(it.hasNext()) return it.next();
        return new Parameters();
    }

    public List<City> findAllCities() {
        return getHibernateTemplate().find("from mcel.tump.reference.domain.City as city");
    }

    public City findCityById(Long id) {
        return (City)getHibernateTemplate().load(City.class, id, LockMode.NONE);
    }

    public List<SubscriberBenefit> findAllSubscriberBenefits() {
        return getHibernateTemplate().find("from mcel.tump.reference.domain.SubscriberBenefit");
    }

    public SubscriberBenefit findSubscriberBenefitById(Long id) {
        return (SubscriberBenefit)getHibernateTemplate().load(SubscriberBenefit.class, id);
    }
    
    public SubscriberBenefit findSubscriberBenefitOfAmount(BigDecimal amount) {
        List benefits = getHibernateTemplate().find("from mcel.tump.reference.domain.SubscriberBenefit as b where b.lowerLimit >= ? and b.upperLimit <= ?", new Object[]{amount, amount});
        if(benefits.size() > 0) {
            Iterator it = benefits.iterator();
            return (SubscriberBenefit)it.next();
        }
        return null;
    }
    
    public List<BlacklistItem> findAllBlacklisted() {
        return getHibernateTemplate().find("from mcel.tump.reference.domain.BlacklistItem as item order by item.account.id, item.subDealerId");
    }
}
