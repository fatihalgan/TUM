/*
 * IParametersService.java
 * 
 * Created on Aug 6, 2007, 6:23:46 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.reference.service;

import java.util.List;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.reference.domain.BlacklistItem;
import mcel.tump.reference.domain.City;
import mcel.tump.reference.domain.Parameters;
import mcel.tump.reference.domain.SubscriberBenefit;

/**
 *
 * @author db2admin
 */
public interface IParametersService {
    public List<City> getAllCities();
    public City getCity(Long id);
    public Parameters getParameters();
    public void saveParameters(Parameters params);
    public List<SubscriberBenefit> getAllSubscriberBenefits();
    public SubscriberBenefit getSubscriberBenefit(Long id);
    public void saveSubscriberBenefit(SubscriberBenefit benefit);
    public void updateSubscriberBenefit(SubscriberBenefit benefit);
    public void deleteSubscriberBenefit(SubscriberBenefit benefit);
    public void addToBlacklist(Long accountId, String subDealerId);
    public void addToBlacklist(BlacklistItem item);
    public void removeFromBlacklist(BlacklistItem item);
    public List<BlacklistItem> getBlacklist();
    public boolean isDealerInBlacklist(AbstractDealer dealer);
}
