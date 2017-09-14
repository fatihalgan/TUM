/*
 * IParametersDao.java
 * 
 * Created on Aug 6, 2007, 6:12:24 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.reference.dao;

import java.math.BigDecimal;
import java.util.List;
import mcel.tump.reference.domain.BlacklistItem;
import mcel.tump.reference.domain.City;
import mcel.tump.reference.domain.Parameters;
import mcel.tump.reference.domain.SubscriberBenefit;
import mcel.tump.util.persistence.IBaseDao;

/**
 *
 * @author db2admin
 */
public interface IParametersDao extends IBaseDao {
    public Parameters findParameters();
    public List<City> findAllCities();
    public City findCityById(Long id);
    public List<SubscriberBenefit> findAllSubscriberBenefits();
    public SubscriberBenefit findSubscriberBenefitById(Long id);
    public List<BlacklistItem> findAllBlacklisted();
    public SubscriberBenefit findSubscriberBenefitOfAmount(BigDecimal amount);
}
